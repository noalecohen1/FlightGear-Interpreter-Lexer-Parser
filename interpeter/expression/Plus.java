package model.interpeter.expression;

public class Plus extends BinaryExpression {
	// CTOR.
	public Plus(Expression otherLeft, Expression otherRight) { super(otherLeft, otherRight); }

	// Calculates & returns the sum result of left Expression & right Expression.
	@Override
	public double calculate() {	return (this.left.calculate() + this.right.calculate()); }
}