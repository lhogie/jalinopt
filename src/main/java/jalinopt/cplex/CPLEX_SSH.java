package jalinopt.cplex;

import jalinopt.LP;
import jalinopt.SSHConnectionInfo;
import toools.extern.Proces;

public class CPLEX_SSH extends AbstractCPLEX
{
	private SSHConnectionInfo auth;

	public CPLEX_SSH(SSHConnectionInfo auth)
	{
		this.auth = auth;
	}


	public CPLEX_SSH(String hostname, String username)
	{
		this(new SSHConnectionInfo(hostname, username));
	}

	public CPLEX_SSH(String hostname)
	{
		this(new SSHConnectionInfo(hostname));
	}

	@Override
	public String callSolverProcessOn(LP lp)
	{
		String args = "cat >.grph.pl; echo -e 'read .grph.pl\\nlp\\n"
				+ method.name()
				+ "\\ndisplay solution variables *\\nquit' | cplex -o ConnectTimeout=1 -o PasswordAuthentication=no";

		String stdOut = new String(Proces.exec("ssh", lp.toCplex().getBytes(),
				"-l", auth.getUsername(), auth.getHost(), args));

		return stdOut;
	}

	@Override
	public String toString()
	{
		return "cplex via SSH " + auth;
	}

}
