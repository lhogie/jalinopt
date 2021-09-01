package jalinopt;

import j4u.CommandLine;
import j4u.CommandLineApplication;
import j4u.License;
import toools.io.file.RegularFile;

public class jalinopt extends CommandLineApplication
{

	public jalinopt(RegularFile launcher)
	{
		super(launcher);
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		String filename = cmdLine.findParameters().get(0);
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
