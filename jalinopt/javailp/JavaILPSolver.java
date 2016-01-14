package jalinopt.javailp;

import jalinopt.Constraint;
import jalinopt.LP;
import jalinopt.LPSolver;
import jalinopt.LinearExpression;
import jalinopt.Result;
import jalinopt.Term;
import jalinopt.Variable;
import jalinopt.LP.OptimizationType;
import jalinopt.Variable.TYPE;
import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class JavaILPSolver extends LPSolver
{

    @Override
    public Result solve(LP lp)
    {
	SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
	factory.setParameter(Solver.VERBOSE, 0);
	factory.setParameter(Solver.TIMEOUT, 100);
	Solver solver = factory.get();
	return translate(solver.solve(translate(lp)), lp);
    }

    private Result translate(net.sf.javailp.Result result, LP lp)
    {
	Result r = new Result(lp);

	for (Variable v : lp.getVariables())
	{
	    if (v.getType() == TYPE.BOOLEAN)
	    {
		v.setValue(result.getBoolean(v.getName()) ? 1 : 0);
	    }
	    else
	    {
		Number n = result.get(v.getName());
		v.setValue(n.doubleValue());
	    }
	}

	r.setObjective(result.getObjective().doubleValue());
	return r;
    }

    private Problem translate(LP lp)
    {
	Problem problem = new Problem();

	// translate objective
	Linear objective = translate(lp.getObjective());

	if (lp.getOptimizationType() == OptimizationType.MAX)
	{
	    problem.setObjective(objective, OptType.MAX);
	}
	else if (lp.getOptimizationType() == OptimizationType.MIN)
	{
	    problem.setObjective(objective, OptType.MIN);
	}

	// translate constraints
	for (Constraint c : lp.getConstraints())
	{
	    Linear constraint = translate(c.getLeftHandSide());
	    problem.add(constraint, c.getOperator(), c.getRightHandSide());
	}

	for (Variable v : lp.getVariables())
	{
	    if (v.getType() == TYPE.BOOLEAN)
	    {
		problem.setVarType(v.getName(), Boolean.class);
	    }
	    else if (v.getType() == TYPE.INTEGER)
	    {
		problem.setVarType(v.getName(), Integer.class);
	    }
	    else if (v.getType() == TYPE.REAL)
	    {
		problem.setVarType(v.getName(), Double.class);
	    }
	}

	return problem;
    }

    private Linear translate(LinearExpression ll)
    {
	Linear l = new Linear();

	for (Term t : ll.getTerms())
	{
	    l.add(t.getCoefficient(), t.getVariable().getName());
	}

	return l;
    }
}
