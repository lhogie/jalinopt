package jalinopt;

import jalinopt.Variable.TYPE;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

public class ApacheSolver extends LPSolver
{

    @Override
    public Result solve(LP lp)
    {
	for (Variable v : lp.getObjective().getVariables())
	{
	    if (v.getType() != TYPE.REAL)
	    {
		throw new IllegalArgumentException(
			"the Apache solver only support real variables (integers and boolean variables are not supported");
	    }
	}

	LinearObjectiveFunction objective = createObjective(lp);
	Collection<LinearConstraint> constraints = createConstraints(lp);

	try
	{
	    RealPointValuePair solution = new SimplexSolver()
		    .optimize(objective, constraints, GoalType.MINIMIZE, false);
	    Result r = new Result(lp);

	    for (int i = 0; i < lp.getObjective().getTerms().size(); ++i)
	    {
		lp.getObjective().getTerms().get(i).getVariable().setValue(solution.getPoint()[i]);
	    }

	    r.setObjective(solution.getValue());
	    return r;
	}
	catch (OptimizationException e)
	{
	    throw new IllegalStateException(e);
	}
    }

    private Collection<LinearConstraint> createConstraints(LP lp)
    {
	Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

	for (Constraint c : lp.getConstraints())
	{
	    double[] coefs = createCoefficients(lp.getObjective());
	    constraints.add(new LinearConstraint(coefs, getOperator(c.getOperator()), c.getRightHandSide()));
	}

	return constraints;
    }

    private LinearObjectiveFunction createObjective(LP lp)
    {
	return new LinearObjectiveFunction(createCoefficients(lp.getObjective()), 0);
    }

    private double[] createCoefficients(LinearExpression le)
    {
	double[] coefs = new double[le.getTerms().size()];

	for (int i = 0; i < le.getTerms().size(); ++i)
	{
	    coefs[i] = le.getTerms().get(i).getCoefficient();
	}

	return coefs;
    }

    private Relationship getOperator(String operator)
    {
	if (operator.equals("="))
	{
	    return Relationship.EQ;
	}
	else if (operator.equals("<="))
	{
	    return Relationship.LEQ;
	}
	else if (operator.equals(">="))
	{
	    return Relationship.GEQ;
	}
	else
	{
	    throw new IllegalStateException("unknown operator " + operator);
	}
    }
}
