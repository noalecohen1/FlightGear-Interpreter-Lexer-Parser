package model.interpeter.command;

import java.util.HashMap;

public class CommandFactory {
	// Data member. 
	private HashMap<String, Command> commandCreatorMap;
	
	// CTOR.
	public CommandFactory() {
		this.commandCreatorMap = new HashMap<String, Command>();
		this.commandCreatorMap.put("return", new ReturnCommand());
		this.commandCreatorMap.put("var", new DefineVariableCommand());
		this.commandCreatorMap.put("=", new AssingCommand());
		this.commandCreatorMap.put("connect", new ConnectCommand());
		this.commandCreatorMap.put("disconnect", new DisconnectCommand());
		this.commandCreatorMap.put("bind", new BindCommand());
		this.commandCreatorMap.put("openDataServer", new OpenDataServerCommand());
		this.commandCreatorMap.put("sleep", new SleepCommand());
		this.commandCreatorMap.put("if", new IfCommand());
		this.commandCreatorMap.put("while", new LoopCommand());
	}
	
	// Returns a new type of Command by received name of Command.
	public Command getCommand(String commandName) { return this.commandCreatorMap.get(commandName); }
	
	// Return true if commandCreatorMap contains the received key.
	public boolean containsKey(String key) { return this.commandCreatorMap.containsKey(key); }
}
