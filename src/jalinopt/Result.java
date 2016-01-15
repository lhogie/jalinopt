package jalinopt;

import java.util.Collection;
import java.util.HashSet;

public class Result
{
    private final Collection<Variable> variables = new HashSet<Variable>();
    private double objective = Double.NaN;
    private LP problem;

    public Result(LP p)
    {
	this.problem = p;
	variables.addAll(p.getVariables());
    }

    public LP getProblem()
    {
        return problem;
    }

    public double getObjective()
    {
	return objective;
    }

    public void setObjective(double objective)
    {
	this.objective = objective;
    }

    @Override
    public String toString()
    {
	StringBuilder b = new StringBuilder();
	b.append("objective: " );
	b.append(objective);
	    b.append('\n');
	
	for (Variable v : variables)
	{
	    b.append(v.getName());
	    b.append(" = ");
	    b.append(v.getValue());
	    b.append('\n');
	}
	
	return b.toString();
    }

    public Collection<Variable> getVariables()
    {
	return variables;
    }
}
