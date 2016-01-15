package jalinopt;

import java.util.ArrayList;
import java.util.List;

import toools.text.TextUtilities;


/**
 * This class represents a sum of (coef, variable) terms.
 * 
 * @author lhogie
 * 
 */

public class LinearExpression
{
    private final List<Term> terms = new ArrayList<Term>();

  

    /**
     * Adds the given coefficient*variable term to this linear expression.
     * 
     * @param coefficient
     *            any number
     * @param variableName
     *            the name of the variable to which the coefficient is applied.
     *            It must begin with a letter
     */
    public void addTerm(double coefficient, Variable v)
    {
	if (v == null)
	    throw new NullPointerException();

	terms.add(new Term(coefficient, v));
    }

    @Override
    public String toString()
    {
	StringBuilder b = new StringBuilder();

	for (int i = 0; i < terms.size(); ++i)
	{
	    Term t = terms.get(i);
	    double c = Math.abs(t.getCoefficient());

	    if (c == 1)
	    {
		// don't need to specify
	    }
	    else
	    {
		b.append(TextUtilities.toNiceString(c));
	    }

	    b.append(t.getVariable().getName());

	    if (i < terms.size() - 1)
	    {
		b.append(' ');
		b.append(terms.get(i + 1).getCoefficient() < 0 ? '-' : '+');
		b.append(' ');
	    }
	}

	return b.toString();
    }

    /**
     * Invert all the coefficients in this linear expression.
     */
    public void invert()
    {
	for (Term t : terms)
	{
	    t.setCoefficient(-t.getCoefficient());
	}
    }

    public Term getTerm(Variable variable)
    {
	for (Term t: terms)
	{
	    if (t.getVariable() == variable)
	    {
		return t;
	    }
	}

	return null;
    }

    public List<Term> getTerms()
    {
        return terms;
    }
    
    public List<Variable> getVariables()
    {
	List<Variable>  l = new ArrayList<Variable>();
	
	for (Term t : terms)
	{
	    l.add(t.getVariable());
	}
	
	return l;
    }

}
