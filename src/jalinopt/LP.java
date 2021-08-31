package jalinopt;

import jalinopt.Variable.TYPE;
import toools.exceptions.NotYetImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Defines what a linear problem (linear model) is.
 * 
 * @author lhogie
 * 
 */

public class LP
{

	public enum OptimizationType
	{
		MIN, MAX
	}

	private final LinearExpression objective;
	private OptimizationType optimizationType;
	private final List<Constraint> constraints = new ArrayList<Constraint>();
	private final Map<String, Variable> name_variable = new HashMap();

	// private final List<Variable> variables = new ArrayList();

	/**
	 * Creates a new empty LP
	 */
	public LP()
	{
		this.objective = new LinearExpression();
	}

	/**
	 * Retrieves the objective of this LP
	 * 
	 * @return a linear expression
	 */
	public LinearExpression getObjective()
	{
		return objective;
	}

	/**
	 * Sets the type of optimization
	 * 
	 */
	public void setOptimizationType(OptimizationType optimizationType)
	{
		this.optimizationType = optimizationType;
	}

	/**
	 * Retrieves the set of variables implied in this LP.
	 */

	public Collection<Variable> getVariables()
	{
		return this.name_variable.values();
	}

	/**
	 * Looks up a variable by its name.
	 * 
	 * @param variableNumber
	 *            the name of a variable implied in this LP
	 * @return the variable, or null if it could not be found
	 */
	public Variable getVariableByName(long variableNumber)
	{
		return getVariableByName("x" + variableNumber);
	}

	public Variable getVariableByName(String variableName)
	{
		Variable v = name_variable.get(variableName);

		if (v == null)
		{
			name_variable.put(variableName, v = new Variable(variableName));
		}

		return v;
	}

	/**
	 * Retrieves the type of optimization for this LP, min or max.
	 */
	public OptimizationType getOptimizationType()
	{
		return optimizationType;
	}

	/**
	 * Retrieves the list of contrainsts applying to this LP.
	 */
	public List<Constraint> getConstraints()
	{
		return constraints;
	}

	@Override
	public String toString()
	{
		return toCplex();
	}

	/**
	 * Computes a CPLEX PL text representing this LP.
	 */
	public String toCplex()
	{
		StringBuilder b = new StringBuilder();

		b.append(getOptimizationType() == OptimizationType.MIN ? "Minimize" : "Maximize");
		b.append('\n');

		b.append("\tobj: ");
		b.append(getObjective());
		b.append('\n');

		b.append('\n');
		b.append("Subject To\n");
		List<Constraint> constraints = getConstraints();
		int numberOfConstraints = constraints.size();

		for (int i = 0; i < numberOfConstraints; ++i)
		{
			Constraint c = constraints.get(i);
			b.append('\t');
			b.append(c);
			b.append('\n');
		}

		b.append('\n');
		b.append("General\n");
		b.append('\t');

		for (Variable variable : getVariables())
		{
			if (variable.getType() == Variable.TYPE.INTEGER)
			{
				b.append('\n');
				b.append('\t');
				b.append(variable.getName());
			}
		}

		b.append('\n');
		b.append("Binary\n");

		for (Variable variable : getVariables())
		{
			if (variable.getType() == Variable.TYPE.BOOLEAN)
			{
				b.append('\n');
				b.append('\t');
				b.append(variable.getName());
			}
		}

		b.append('\n');
		b.append("End");

		return b.toString().replace('*', ' ');
	}

	/**
	 * Computes a CPLEX PL text representing this LP.
	 */
	public String toLpSolve()
	{
		StringBuilder b = new StringBuilder();

		b.append(getOptimizationType() == OptimizationType.MIN ? "min" : "max");
		b.append(':');
		b.append(' ');
		b.append(getObjective());
		b.append(';');

		for (Constraint c : getConstraints())
		{
			b.append('\n');
			b.append(c);
			b.append(';');
		}

		for (Variable variable : getVariables())
		{
			if (variable.getType() == Variable.TYPE.INTEGER)
			{
				b.append('\n');
				b.append("int ");
				b.append(variable.getName());
				b.append(';');
			}
		}

		for (Variable variable : getVariables())
		{
			if (variable.getType() == Variable.TYPE.BOOLEAN)
			{
				b.append('\n');
				b.append("bin ");
				b.append(variable.getName());
				b.append(';');
			}
		}
		return b.toString();
	}

	public Constraint addConstraint()
	{
		Constraint c = new Constraint();
		this.constraints.add(c);
		return c;
	}

	/**
	 * This this LP using the default solver.

	 */
	public Result solve()
	{
		return solve(LPSolver.getDefaultSolver());
	}

	/**
	 * Solves this problem using the given solver.
	 */
	public Result solve(LPSolver solver)
	{
		return solver.solve(this);
	}

	/**
	 * Adds a contrainst to this problem.
	 */
	public Constraint addConstraint(String operator, double rightOperand)
	{
		Constraint c = addConstraint();
		c.setOperator(operator);
		c.setRightHandSide(rightOperand);
		return c;
	}

	public String toCanonicalForm()
	{
		StringBuilder b = new StringBuilder();

		b.append("Maximize ");

		if (optimizationType == OptimizationType.MIN)
		{
			objective.invert();
			b.append(objective);
			objective.invert();
		}
		else
		{
			b.append(objective);
		}

		if ( ! constraints.isEmpty())
		{
			b.append('\n');
			b.append("\nSubject to ");
			Iterator<Constraint> i = constraints.iterator();

			b.append(i.next());

			while (i.hasNext())
			{
				b.append("\nAnd " + i.next());
			}
		}

		return b.toString();
	}

	/**
	 * Creates an example LP.
	 * 
	 */
	public static LP createExample()
	{
		LP lp = new LP();
		lp.setOptimizationType(OptimizationType.MIN);

		// define the objective
		LinearExpression objective = lp.getObjective();
		objective.addTerm(3, lp.getVariableByName("x"));
		objective.addTerm(2, lp.getVariableByName("y"));

		// 4x + 1y < 15
		Constraint c1 = lp.addConstraint();
		c1.getLeftHandSide().addTerm(4, lp.getVariableByName("x"));
		c1.getLeftHandSide().addTerm(1, lp.getVariableByName("y"));
		c1.setOperator("<");
		c1.setRightHandSide(15);

		// short way to define a constraint (5y > 10)
		Constraint c2 = lp.addConstraint(">", 10);
		c2.addTerm(5, lp.getVariableByName("y"));

		// define the type for variable
		lp.getVariableByName("y").setType(TYPE.INTEGER);

		return lp;
	}

	public static int var2i(Variable v)
	{
		return Integer.valueOf(v.getName().substring(1));
	}

	public static LP parseCplex(String cplexText)
	{
		throw new NotYetImplementedException();
	}

	public void addVariable(Variable v)
	{
		if ( ! name_variable.containsKey(v.getName()))
		{
			name_variable.put(v.getName(), v);
			// variables.add(v);
		}
	}

}
