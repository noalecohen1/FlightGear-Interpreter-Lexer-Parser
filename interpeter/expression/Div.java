package model.interpeter.expression;

public class Div extends BinaryExpression {
	// CTOR.
	public Div(Expression otherLeft, Expression otherRight) { super(otherLeft, otherRight); }

	// Calculates & returns the division result of left Expression & right Expression.
	@Override
	public double calculate() {	return (this.left.calculate() / this.right.calculate()); }
}