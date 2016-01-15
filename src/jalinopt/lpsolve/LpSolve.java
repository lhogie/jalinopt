package jalinopt.lpsolve;

import jalinopt.Constraint;
import jalinopt.LP;
import jalinopt.LP.OptimizationType;
import jalinopt.LinearExpression;
import jalinopt.PipedLPSolver;
import jalinopt.Result;
import jalinopt.Variable.TYPE;

import java.io.IOException;

import toools.extern.Proces;
import toools.extern.ProcessOutput;
import toools.text.TextUtilities;

public class LpSolve extends PipedLPSolver
{

    @Override
    protected boolean hasFoundSolution(String solverOutput)
    {
	return solverOutput.contains("Value of objective function:");
    }

    @Override
    protected double extractObjectiveValue(String solverOutput)
    {
	int a = solverOutput.indexOf("Value of objective function:");
	a += "Value of objective function:".length();
	int b = solverOutput.indexOf("\n", a);
	return Double.valueOf(solverOutput.substring(a, b));
    }

    @Override
    protected void extractVariablesValue(String solverOutput, LP lp, Result r)
    {
	int a = solverOutput.indexOf("Actual values of the variables:");
	a += "Value of objective function:".length();
	int b = solverOutput.indexOf("\n", a);

	for (String line : TextUtilities.splitInLines(solverOutput.substring(b + 1)))
	{
	    String[] e = line.trim().split("[ \\t]+");
	    String variable = e[0];
	    String value = e[1];
	    lp.getVariableByName(variable).setValue(Double.valueOf(value));
	}
    }

    @Override
    protected String callSolverProcessOn(LP lp) throws IOException
    {
	ProcessOutput r = Proces.rawExec("lp_solve", lp.toLpSolve().getBytes());

	if (r.getReturnCode() == 0)
	{
	    return new String(r.getStdout());
	}
	else
	{
	    return new String(r.getStderr());
	}
    }

    public static void main(String[] args)
    {
	LP lp = new LP();
	lp.setOptimizationType(OptimizationType.MAX);

	// define the objective: x + y
	LinearExpression objective = lp.getObjective();
	objective.addTerm(1, lp.getVariableByName("x"));
	objective.addTerm(1, lp.getVariableByName("y"));

	Constraint c1 = new Constraint();
	c1.getLeftHandSide().addTerm(1, lp.getVariableByName("x"));
	c1.setOperator("<=");
	c1.setRightHandSide(7);
	lp.getConstraints().add(c1);

	Constraint c2 = new Constraint();
	c2.getLeftHandSide().addTerm(1, lp.getVariableByName("x"));
	c2.setOperator(">=");
	c2.setRightHandSide(8);
	lp.getConstraints().add(c2);

	// define the type for variable
	lp.getVariableByName("x").setType(TYPE.INTEGER);

	Result r = new LpSolve().solve_debug(lp);

	System.out.println(r);
    }

    @Override
    public String toString()
    {
	return "LPSolve on the local computer";
    }

}
