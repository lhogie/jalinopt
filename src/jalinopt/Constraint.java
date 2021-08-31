package jalinopt;

import toools.text.TextUtilities;

/**
 * A linear constraint of the form: linear expression operator scalar
 * 
 * @author lhogie
 * 
 */

public class Constraint
{
    private final LinearExpression leftHandSide;
    private double rightOperand;
    private String operator;

    public Constraint()
    {
	this.leftHandSide = new LinearExpression();
    }

    public void setRightHandSide(double rightOperand)
    {
	this.rightOperand = rightOperand;
    }

    /**
     * Sets the operator for the constraint. Possible values are: <=, >= and =.
     * 
     * @param operator
     */
    public void setOperator(String operator)
    {
	if (!operator.equals("=") && !operator.equals("<=") && !operator.equals(">="))
	    throw new IllegalArgumentException("unsupported operator: " + operator);
	
	this.operator = operator;
    }

    public LinearExpression getLeftHandSide()
    {
	return leftHandSide;
    }

    public double getRightHandSide()
    {
	return rightOperand;
    }

    public String getOperator()
    {
	return operator;
    }

    @Override
    public String toString()
    {
	StringBuilder b = new StringBuilder();
	b.append(leftHandSide);
	b.append(' ');
	b.append(operator);
	b.append(' ');
	b.append(TextUtilities.removeUselessDecimals(rightOperand));
	return b.toString();
    }

    public void addTerm(int coefficient, Variable v)
    {
	getLeftHandSide().addTerm(coefficient, v);

    }
}
