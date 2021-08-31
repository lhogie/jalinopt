package jalinopt.cplex;

import jalinopt.LP;
import jalinopt.PipedLPSolver;
import jalinopt.Result;
import jalinopt.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import toools.text.TextUtilities;


/**
 * Communication protocol which CPLEX processes. Nothing is said about where the
 * process runs (eg. locally or remotely). The main role of this class is to
 * parse the output of the CPLEX process.
 * 
 * @author lhogie
 * 
 */

public abstract class AbstractCPLEX extends PipedLPSolver
{
    public METHOD method = METHOD.optimize;

    public enum METHOD
    {
	mipopt, netopt, optimize, primopt, tranopt
    }

    @Override
    protected boolean hasFoundSolution(String solverOutput)
    {
	return solverOutput.indexOf("Objective =") > 0;
    }

    @Override
    protected void extractVariablesValue(String solverOutput, LP lp, Result r)
    {
	Collection<String> variablesListed = new HashSet();
	int pos = solverOutput.indexOf("Variable Name");

	if (pos >= 0)
	{
	    List<String> lines = TextUtilities.splitInLines(solverOutput.substring(pos));

	    for (String line : lines.subList(1, lines.size() - 1))
	    {
		if (line.startsWith("All other variables matching '*' are "))
		{
		    break;
		}
		else
		{
		    String[] tokens = line.trim().split(" +");
		    String variableName = tokens[0];
		    variablesListed.add(variableName);
		    double value = Double.valueOf(tokens[1]);
		    lp.getVariableByName(variableName).setValue(value);
		}
	    }
	}

	// all variable non-listed by CPLEX are assumed to be zero
	for (Variable v : lp.getVariables())
	{
	    if (!variablesListed.contains(v.getName()))
	    {
		v.setValue(0);
	    }
	}
    }

    @Override
    protected double extractObjectiveValue(String solverOutput)
    {
	int start = solverOutput.indexOf("Objective =") + "Objective =".length();
	int end = solverOutput.indexOf('\n', start);
	return Double.valueOf(solverOutput.substring(start, end));
    }
}
