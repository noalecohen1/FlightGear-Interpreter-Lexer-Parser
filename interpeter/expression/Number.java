package model.interpeter.expression;

public class Number implements Expression {
	// Data member.
	private double value;
	
	// CTOR.
	public Number(double otherValue) { this.value = otherValue; }

	// Sets the Number's value.
	public void setValue(double otherValue) { this.value = otherValue; }
	
	// Returns the Number's value.
	public double getValue() { return value; }
	
	// Calculates the Number's value. 
	@Override
	public double calculate() { return this.getValue(); }
}