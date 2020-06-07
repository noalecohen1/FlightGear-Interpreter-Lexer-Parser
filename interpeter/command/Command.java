package model.interpeter.command;

import model.interpeter.Interpeter;

public abstract class Command {
	// Data member.
	Interpeter interpeter;
	
	// CTOR.
	public Command() { this.interpeter = null; }
	
	public abstract int execute();
	public void setInterpeter(Interpeter otherInterpeter) {this.interpeter = otherInterpeter; } 
}