package model.interpeter.expression;

import model.interpeter.command.ConnectCommand;

public class SimulatorVariable extends Variable{
	// CTOR.
	public SimulatorVariable(double otherValue, String otherName) { super(otherValue, otherName); }

	@Override
	public void setValue(double otherValue) {
		super.setValue(otherValue);
		setChanged();
		notifyObservers(otherValue);
		
		String lineToSimulator = "set " + this.getName() + " " + this.getValue();
		ConnectCommand.sendToServer(lineToSimulator);
	}
}
