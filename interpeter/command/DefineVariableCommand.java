package model.interpeter.command;

import java.util.ArrayList;

import model.interpeter.expression.Variable;

public class DefineVariableCommand extends Command{
	// CTOR.
	public DefineVariableCommand() { super(); }
	
	@Override
	public int execute() {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken();
		String variableServerName = tokens.get(indexBlockOfTokens)[indexToken + 1];
		
		this.interpeter.getServerSymbolTable().put(variableServerName, new Variable(0.0, variableServerName));
		
		this.interpeter.setIndexToken(indexToken + 1);
		
		return 0;
	}
}
