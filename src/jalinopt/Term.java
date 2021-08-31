package jalinopt;



public class Term
{
    private  double coefficient;
    private  Variable variable;
    
    public Term(double c, Variable v)
    {
	if (v == null)
	    throw new NullPointerException();

	this.coefficient = c;
	this.variable =v;
    }

    public void setCoefficient(double coefficient)
    {
        this.coefficient = coefficient;
    }

    public void setVariable(Variable variable)
    {
        this.variable = variable;
    }

    public double getCoefficient()
    {
        return coefficient;
    }

    public Variable getVariable()
    {
        return variable;
    }
}
