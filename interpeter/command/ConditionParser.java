package model.interpeter.command;

import java.util.ArrayList;

import model.interpeter.expression.ExpressionCommand;
import model.interpeter.expression.ShuntingYardAlgorithm;

public class ConditionParser extends Command{
	// Data members.
	protected ArrayList<String[]> commandList;
	protected ArrayList<String> leftExpression;
	protected ArrayList<String> rightExpression;
	protected String operator; 
	protected int startIndexBlockOfTokens;
	
	// CTOR.
	public ConditionParser() {
		super();
		this.commandList = new ArrayList<String[]>();
		this.leftExpression = new ArrayList<String>();
		this.rightExpression = new ArrayList<String>();
		this.operator = null;
		this.startIndexBlockOfTokens = 0;
		
	}
	
	@Override
	public int execute() {
		this.startIndexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		getCondition();
	
		this.interpeter.setIndexBlockOfTokens(this.interpeter.getIndexBlockOfTokens() + 1);
		this.interpeter.setIndexToken(0);
		
		getCommands();
		return 0;
	}

	//
	private void getCondition() {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken() + 1;
		boolean check = true;
		
		while(check == true) {
			switch (tokens.get(indexBlockOfTokens)[indexToken]) {
			case "<":	check = false; break;
				
			case ">":	check = false; break;	

			case "=":	check = false; break;

			case "!":	check = false; break;
			 
			default: 	this.leftExpression.add(tokens.get(indexBlockOfTokens)[indexToken]); 
						indexToken++; 
						break;
			}
		}	
		
		this.operator = tokens.get(indexBlockOfTokens)[indexToken];
		
		if(tokens.get(indexBlockOfTokens)[indexToken + 1].equals("=")) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(this.operator).append("=");
			this.operator = stringBuilder.toString();
			indexToken++;
		}
		
		indexToken++;
		
		while(true) {
			if(tokens.get(indexBlockOfTokens)[indexToken].equals("{")) {		
				break;
			}
			
			this.rightExpression.add(tokens.get(indexBlockOfTokens)[indexToken]); 
			indexToken++; 
		}
	}

	//
	private void getCommands() {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		
		while(true) {
			if(tokens.get(indexBlockOfTokens)[0].equals("}")) {
				break;
			}
			else this.commandList.add(tokens.get(indexBlockOfTokens));
			
			indexBlockOfTokens++;
		}
		
		// this.interpeter.setIndexBlockOfTokens(indexBlockOfTokens);
	}

	//
	protected boolean checkCondtion() {
		double leftResult = ShuntingYardAlgorithm.execute(leftExpression, this.interpeter.getServerSymbolTable());
		double rightResult = ShuntingYardAlgorithm.execute(rightExpression, this.interpeter.getServerSymbolTable());
			
		switch (this.operator) {
		case ">": return (leftResult > rightResult);
		case "<": return (leftResult < rightResult);
		case "==": return (leftResult == rightResult);
		case "!=": return (leftResult != rightResult);
		case "<=": return (leftResult <= rightResult);
		case ">=": return (leftResult >= rightResult);
		}
		
		return false;
	}

	//
	protected void executeListOfCommands() {
		// Runs overs each String[] in the list.
		for (int indexBlockOfTokens = 0; indexBlockOfTokens < this.commandList.size(); indexBlockOfTokens++) {
			// Runs overs each string in string[].
			for(int indexToken = 0; indexToken < this.commandList.get(indexBlockOfTokens).length; indexToken++) {
				Command command = this.interpeter.getCommandFactory().getCommand(this.commandList.get(indexBlockOfTokens)[indexToken]);
				if(command != null) {
					this.interpeter.setIndexBlockOfTokens(indexBlockOfTokens + this.startIndexBlockOfTokens + 1);
					this.interpeter.setIndexToken(indexToken);
					command.setInterpeter(this.interpeter);
					new ExpressionCommand(command).calculate();
				}
			}
		}
	}
}