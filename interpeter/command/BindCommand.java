package model.interpeter.command;

import java.util.ArrayList;

import model.interpeter.expression.SimulatorVariable;
import model.interpeter.expression.Variable;

public class BindCommand extends Command{
	// CTOR,
	public BindCommand() { super(); }
	
	@Override
	public int execute() {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken();
		String variableSimulatorName = tokens.get(indexBlockOfTokens)[indexToken + 1];
		String variableServerName = tokens.get(indexBlockOfTokens)[indexToken - 2];
		
//		if(this.interpeter.getSimulatorSymbolTable().containsKey(variableSimulatorName) == false) {
//			this.interpeter.getSimulatorSymbolTable().put(variableSimulatorName, new SimulatorVariable(0.0, variableSimulatorName));
//		}
		
		SimulatorVariable simulatorVariable = this.interpeter.getSimulatorSymbolTable().get(variableSimulatorName);
		Variable serverVariable = null;
		
		
		if(this.interpeter.getServerSymbolTable().containsKey(variableServerName) == true) {
			serverVariable = this.interpeter.getServerSymbolTable().get(variableServerName);
		}
		else {
			System.out.println("Error occured...There is not variable with the name: " + variableServerName + "...");
			return 0;
		}
		
		simulatorVariable.addObserver(serverVariable);
		serverVariable.addObserver(simulatorVariable);
		
		serverVariable.setValue(simulatorVariable.getValue());
		
		this.interpeter.setIndexToken(indexToken + 1);
		return 0;
	}
}