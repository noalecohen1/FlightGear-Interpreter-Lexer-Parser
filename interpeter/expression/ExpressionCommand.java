package model.interpeter.expression;

import model.interpeter.command.Command;

public class ExpressionCommand implements Expression {
	// Data member.
	Command command;
	
	// CTOR.
	public ExpressionCommand(Command otherCommand) { this.command = otherCommand; }
	
	@Override
	public double calculate() { return this.command.execute(); }
}