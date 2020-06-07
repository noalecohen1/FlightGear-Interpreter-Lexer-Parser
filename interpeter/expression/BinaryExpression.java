package model.interpeter.expression;

public abstract class BinaryExpression implements Expression {
	protected Expression left;
	protected Expression right;

	// CTOR.
	public BinaryExpression(Expression otherLeft, Expression otherRight) { 
		this.left = otherLeft;
		this.right = otherRight;
	}
}