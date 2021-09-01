package jalinopt;

public class Variable
{
	private final String name;
	private TYPE type = TYPE.REAL;
	private double value;
	private boolean valueDefined = false;

	public enum TYPE {
		REAL, INTEGER, BOOLEAN
	};

	public Variable(String name)
	{
		name = name.trim();

		if (name.isEmpty())
			throw new IllegalArgumentException();

		this.name = name;
	}

	public void setType(TYPE type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return name + (valueDefined ? "=" + value : "") + " is " + type.name();
	}

	public TYPE getType()
	{
		return type;
	}

	public double getValue()
	{
		if (!valueDefined)
			throw new IllegalStateException("the value for variable "
					+ getName() + " is not defined");

		return value;
	}

	public void setValue(double value)
	{
		this.valueDefined = true;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Variable && ((Variable) obj).name.equals(name);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

}
