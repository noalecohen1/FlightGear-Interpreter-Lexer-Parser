package model.interpeter.expression;

public class Mul extends BinaryExpression {
	// CTOR.
	public Mul(Expression otherLeft, Expression otherRight) { super(otherLeft, otherRight); }

	// Calculates & returns the multiplication result between left Expression & right Expression.
	@Override
	public double calculate() {	return (this.left.calculate() * this.right.calculate()); }
}