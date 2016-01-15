package jalinopt.cbc;

import jalinopt.LP;
import jalinopt.PipedLPSolver;
import jalinopt.Result;

import java.io.IOException;

import toools.NotYetImplementedException;
import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.net.NetUtilities;

public class Cbc extends PipedLPSolver
{

    public RegularFile PATH_TO_CBC;

    @Override
    protected boolean hasFoundSolution(String solverOutput)
    {
	throw new NotYetImplementedException();
    }

    @Override
    protected double extractObjectiveValue(String solverOutput)
    {
	throw new NotYetImplementedException();
    }

    @Override
    protected void extractVariablesValue(String solverOutput, LP lp, Result r)
    {
	throw new NotYetImplementedException();
    }

    @Override
    protected String callSolverProcessOn(LP lp) throws IOException
    {
	ensureCBCisAvailable();
	throw new NotYetImplementedException();
    }

    private void ensureCBCisAvailable() throws IOException
    {
	String name = "Cbc-2.7.6";
	String filename = name + ".zip";
	Directory jalinoptDirectory = new Directory(Directory.getHomeDirectory(), ".jalinopt");
	RegularFile archiveFile = new RegularFile(jalinoptDirectory, filename);
	Directory cbcDirectory = new Directory(archiveFile.getParent(), name);
	System.out.println(cbcDirectory);

	if (!cbcDirectory.exists())
	{
	    if (!jalinoptDirectory.exists())
		jalinoptDirectory.mkdirs();

	    System.out.println("downloading CBC source code");
	    archiveFile.setContent(NetUtilities.retrieveURLContent("http://www.coin-or.org/download/source/Cbc/"
		    + filename));

	    System.out.println("unzipping CBC source code");
	    Proces.exec("unzip", archiveFile.getParent(), filename);
	    archiveFile.delete();
	}

	PATH_TO_CBC = new RegularFile(cbcDirectory, "Cbc/src/cbc");

	if (!PATH_TO_CBC.exists())
	{

	    System.out.println("configuring");
	    Proces.exec("./configure", cbcDirectory);

	    // if configuration succeeded
	    if (new RegularFile(cbcDirectory, "Makefile").exists())
	    {
		System.out.println("compiling");
		Proces.exec("make", cbcDirectory);
	    }
	}

    }
}
