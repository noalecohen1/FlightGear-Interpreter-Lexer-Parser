package model.interpeter.command;

import java.util.ArrayList;

import model.interpeter.expression.ShuntingYardAlgorithm;
import model.interpeter.expression.Variable;

public class AssingCommand extends Command {
	// CTOR.
	public AssingCommand() { super(); }
	
	@Override
	public int execute() {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken();
		String variableServerName = tokens.get(indexBlockOfTokens)[indexToken - 1];
		
		if(tokens.get(indexBlockOfTokens)[indexToken + 1].equals("bind")) { return 0; }
		
		ArrayList<String> expression = new ArrayList<String>();
		String[] block = this.interpeter.getTokens().get(this.interpeter.getIndexBlockOfTokens()); 
		
		for(int i = (indexToken + 1); i < block.length; i++) {
			expression.add(block[i]);
		}
		
		double result = ShuntingYardAlgorithm.execute(expression, this.interpeter.getServerSymbolTable());
		
		if(this.interpeter.getServerSymbolTable().containsKey(variableServerName) == true) {
			Variable serverVariable = this.interpeter.getServerSymbolTable().get(variableServerName);
			serverVariable.setValue(result);
			this.interpeter.getServerSymbolTable().put(variableServerName, serverVariable);
		}
		else {
			System.out.println("Error occured...There is not variable with the name: " + variableServerName + "...");
		}
				
		this.interpeter.setIndexToken(expression.size() + this.interpeter.getIndexToken());
		
		return 0;
	}
}
