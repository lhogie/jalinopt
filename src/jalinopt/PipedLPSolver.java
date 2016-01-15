package jalinopt;

import java.io.IOException;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;


/**
 * Modelisation of an LP solvers that is implemented through a call to an
 * external process.
 * 
 * @author lhogie
 * 
 */

public abstract class PipedLPSolver extends LPSolver
{
    public static final Directory lpDirectory = new Directory(Directory.getSystemTempDirectory(), "jalinopt/lp");
    public static boolean DEBUG = false;

    protected static RegularFile createLPFile(String text) throws IOException
    {
	if (!lpDirectory.exists())
	{
	    lpDirectory.mkdirs();
	}

	RegularFile lpFile = RegularFile.createTempFile(lpDirectory, "", ".pl");
	lpFile.setContent(text.getBytes());
	return lpFile;
    }

    public Result solve_debug(LP lp)
    {
	DEBUG = true;
	Result r = solve(lp);
	DEBUG = false;
	return r;
    }

    @Override
    public Result solve(LP lp)
    {
	if (DEBUG)
	{
	    System.out.println(TextUtilities.repeat('*', 80));
	    System.out.println("Linear program: ");
	    System.out.println(TextUtilities.repeat('*', 80));
	    System.out.println(lp.toString());
	    System.out.println(TextUtilities.repeat('*', 80));
	}

	try
	{
	    String solverOutput = callSolverProcessOn(lp);

	    if (DEBUG)
	    {
		System.out.println(TextUtilities.repeat('*', 80));
		System.out.println("Solver output: ");
		System.out.println(TextUtilities.repeat('*', 80));
		System.out.println(solverOutput);
		System.out.println(TextUtilities.repeat('*', 80));
	    }

	    return parse(solverOutput, lp);
	}
	catch (IOException e)
	{
	    throw new IllegalStateException(e);
	}
    }

    /**
     * Parses the output of the solver
     * 
     * @param solverOutput
     *            the text given by the solver
     * @param lp
     * @return the result object or null if no result could be found
     * @throws SolverException
     */
    private Result parse(String solverOutput, LP lp)
    {
	if (hasFoundSolution(solverOutput))
	{
	    Result r = new Result(lp);
	    r.setObjective(extractObjectiveValue(solverOutput));
	    extractVariablesValue(solverOutput, lp, r);
	    return r;
	}
	else
	{
	    return null;
	}
    }

    protected abstract boolean hasFoundSolution(String solverOutput);

    protected abstract double extractObjectiveValue(String solverOutput);

    protected abstract void extractVariablesValue(String solverOutput, LP lp, Result r);

    protected abstract String callSolverProcessOn(LP lp) throws IOException;

}
