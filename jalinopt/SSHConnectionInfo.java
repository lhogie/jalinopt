package jalinopt;

public class SSHConnectionInfo
{
    private final String host;
    private final String username;
    private final String password;

    public SSHConnectionInfo(String host, String user, String password)
    {
	this.host = host;
	this.username = user;
	this.password = password;
    }

    public SSHConnectionInfo(String host, String user)
    {
	this(host, user, null);
    }

    public SSHConnectionInfo(String host)
    {
	this(host, System.getProperty("user.name"));
    }

    public String getHost()
    {
	return host;
    }

    public String getUsername()
    {
	return username;
    }

    public String getPassword()
    {
	return password;
    }

    public SSHConnectionInfo()
    {
	this("musclotte.inria.fr");
    }

    @Override
    public String toString()
    {
	return "ssh info: " + username + "@" + host;
    }

}
