package jalinopt.cplex;

import jalinopt.LP;

import java.io.IOException;

import toools.extern.Proces;

public class LocalCPLEX extends AbstractCPLEX
{
    private String exe;

    public LocalCPLEX()
    {
	this("cplex");
    }

    public LocalCPLEX(String executableFileName)
    {
	this.exe = executableFileName;
    }

    @Override
    protected String callSolverProcessOn(LP lp) throws IOException
    {
	String stdin = "read " + createLPFile(lp.toCplex()).getPath() + "\nlp\n" + method.name()
		+ "\ndisplay solution variables *\nquit";
//	System.out.println("*** " + stdin);
	return new String(Proces.exec(exe, stdin.getBytes()));
    }
    
    @Override
    public String toString()
    {
	return "cplex on the local computer";
    }

}
