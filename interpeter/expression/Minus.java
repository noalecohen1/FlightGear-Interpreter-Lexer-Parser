package model.interpeter.expression;

public class Minus extends BinaryExpression {
	// CTOR.
	public Minus(Expression otherLeft, Expression otherRight) { super(otherLeft, otherRight); }

	// Calculates & returns the difference result between left Expression & right Expression.
	@Override
	public double calculate() {	return (this.left.calculate() - this.right.calculate()); }
}