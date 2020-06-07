package model.interpeter.expression;

import java.util.Observable;
import java.util.Observer;

public class Variable extends Observable implements Expression, Observer {
	// Data members:
	private double value;
	private String name;
	
	// CTOR.
	public Variable(double otherValue, String otherName) { 
		this.value = otherValue;
		this.name =otherName;
	}
	
	// Getters & Setters.
	public double getValue() { return this.value; }
	public void setValue(double otherValue) {  
		this.value = otherValue;
		setChanged();
		notifyObservers(this.value);
	} 

	public String getName() { return this.name; }
	public void setName(String otherName) {  this.name = otherName; }	
	
	// Returns the value of the Variable after calculation.
	@Override
	public double calculate() { return getValue(); }	
	
	// Returns the value of the Variable as a string.
	@Override
	public String toString() { return ((Double)(this.value)).toString(); }
	
	// TODO 
	@Override
	public void update(Observable observable, Object arg) {
		Variable otherVariable = (Variable)observable;
		
		if(this.value != otherVariable.getValue()) {
			setValue(otherVariable.getValue());
		}
	}
}