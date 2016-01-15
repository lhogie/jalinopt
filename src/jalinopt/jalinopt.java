package jalinopt;

import java.util.Collection;

import toools.io.file.RegularFile;
import java4unix.AbstractShellScript;
import java4unix.ArgumentSpecification;
import java4unix.CommandLine;
import java4unix.License;
import java4unix.OptionSpecification;


public class jalinopt extends AbstractShellScript
{

    @Override
    protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected void declareArguments(Collection<ArgumentSpecification> argumentSpecifications)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public int runScript(CommandLine cmdLine) throws Throwable
    {
	String filename = cmdLine.findArguments().get(0);
	RegularFile f = new RegularFile(filename);
	String cplexText = new String(f.getContent());
	LP lp = LP.parseCplex(cplexText);
	LPSolver solver = LPSolver.getDefaultSolver();
	Result r = solver.solve(lp);

	if (r != null)
	{
	    printMessage("No result");
	}
	else
	{
	    printMessage(r);
	}
	return 0;
    }

    @Override
    public String getApplicationName()
    {
	return "jalinopt";
    }

    @Override
    public String getAuthor()
    {
	return "Luc Hogie";
    }

    @Override
    public License getLicence()
    {
	return null;
    }

    @Override
    public String getYear()
    {
	return "2012";
    }

    @Override
    public String getShortDescription()
    {
	return "A solver for linear programs";
    }

}
