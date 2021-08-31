package jalinopt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jalinopt.cplex.CPLEX_SSH;
import jalinopt.cplex.LocalCPLEX;
import jalinopt.lpsolve.LpSolve;
import toools.extern.Proces;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

/**
 * The basic definition of a LP solver.
 * 
 * It is an object able to solve and LP.
 * 
 * @author lhogie
 * 
 */

public abstract class LPSolver
{

	public abstract Result solve(LP lp);

	private static LPSolver defaultSolver;

	static
	{
		defaultSolver = findSolver();
		System.out.println("Default LP solver is " + defaultSolver.getClass().getName());
	}

	public static LPSolver getDefaultSolver()
	{
		return defaultSolver;
	}

	public static void setDefaultSolver(LPSolver defaultSolver)
	{
		LPSolver.defaultSolver = defaultSolver;
	}

	private static LPSolver findSolver()
	{
		if (Proces.commandIsAvailable("cplex"))
		{
			try
			{
				// if the license key has not expired
				if (new String(Proces.exec("cplex", "quit".getBytes()))
						.contains("Welcom"))
				{
					return new LocalCPLEX("cplex");
				}
			}
			catch (Throwable t)
			{
				System.err.println(
						"Cannot use the cplex command on this system. Please check.");
			}
		}

		if (Proces.commandIsAvailable("cplex.exe"))
		{
			return new LocalCPLEX("cplex.exe");
		}

		if (Proces.commandIsAvailable("lp_solve"))
		{
			return new LpSolve();
		}

		for (String hostname : getHostnames())
		{
			if (testCplexOnHost(hostname))
			{
				return new CPLEX_SSH(hostname);
			}
		}

		throw new IllegalStateException("no LP solver found");
		// return new ApacheSolver();
	}

	private static List<String> getHostnames()
	{
		RegularFile f = new RegularFile("~/.jalinopt/hosts");

		if (f.exists())
		{
			return TextUtilities.splitInLines(new String(f.getContent()));
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * If CPLEX has not responded (via SSH) in less that one second, it is
	 * assumed unavailable.
	 * 
	 * @param host
	 * @return
	 */
	private static boolean testCplexOnHost(String host)
	{
		try
		{
			byte[] b = Proces.exec("ssh", "echo quit".getBytes(), "-o",
					"ConnectTimeout=1", "-o", "PasswordAuthentication=no", "musclotte",
					"cplex");
			return new String(b).contains("ILOG CPLEX");
		}
		catch (Throwable t)
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		return getClass().toString();
	}
}
