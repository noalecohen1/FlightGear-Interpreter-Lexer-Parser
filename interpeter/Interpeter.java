package model.interpeter;

import java.util.ArrayList;
import java.util.HashMap;

import model.interpeter.command.Command;
import model.interpeter.command.CommandFactory;
import model.interpeter.expression.ExpressionCommand;
import model.interpeter.expression.SimulatorVariable;
import model.interpeter.expression.Variable;

public class Interpeter {
	// Data members.
	private HashMap<String, Variable> serverSymbolTable;
	private HashMap<String, SimulatorVariable> simulatorSymbolTable;
	private CommandFactory commandFactory;
	private ArrayList<String[]> tokens;
	private double returnedValue;
	private int indexToken;
	private int indexBlockOfTokens;
	
	// CTOR.
	public Interpeter() {
		this.serverSymbolTable = new HashMap<String, Variable>();
		this.simulatorSymbolTable = new HashMap<String, SimulatorVariable>();
		this.tokens = new ArrayList<String[]>();
		this.commandFactory = new CommandFactory();
		this.returnedValue = 0;
		this.indexToken = 0;
		this.indexBlockOfTokens = 0;
	}
	
	// Getters & Setters.
	public HashMap<String, Variable> getServerSymbolTable() { return this.serverSymbolTable; }
	
	public HashMap<String, SimulatorVariable> getSimulatorSymbolTable() { return this.simulatorSymbolTable; }
	
	public CommandFactory getCommandFactory() { return this.commandFactory; }
	
	public void setTokens(ArrayList<String[]> otherTokens) { this.tokens = otherTokens; }
	public ArrayList<String[]> getTokens() { return tokens; }

	public void setReturnedValue(double otherReturnedValue) { this.returnedValue = otherReturnedValue; }
	public double getReturnedValue() { return this.returnedValue; }
	
	public void setIndexToken(int otherIndexToken) { this.indexToken = otherIndexToken; }
	public int getIndexToken() { return this.indexToken; }
	
	public void setIndexBlockOfTokens(int otherIndexBlockOfTokens) { this.indexBlockOfTokens = otherIndexBlockOfTokens; }
	public int getIndexBlockOfTokens() { return this.indexBlockOfTokens; }
	
	// Interpret some code lines using the lexing function & parsing function.
	// Returns a double number in the end if it was requested to return value, if not, 0 returned as success. 
	public double interpret(String[] codeLines) {
		resetInterpeter();
		lexing(codeLines);
		parsing();
		printData();
		
		return getReturnedValue(); 
	}
	
	public double interpret(String fileName) {
		this.lexing(fileName);
		this.parser();
		
		return getReturnedValue();
	}
	
	// The Lexical analysis (Lexer) of MyInterpeter.
	// Converting a sequence of characters into a sequence of tokens
	// (= strings with an assigned and thus identified meaning).
	public void lexing(String[] codeLines) {
		for (String line : codeLines) {
			
			line = line.replaceAll("\\{", "\\ { ").replaceAll("\\}", "\\ } ").replaceAll("\\>", "\\ > ").replaceAll("\\<", "\\ < ")	
					   .replaceAll("\\+", "\\ + ").replaceAll("\\-", "\\ - ").replaceAll("\\*", "\\ * ").replaceAll("\\/", "\\ / ")
					   .replaceAll("\\(", "\\ ( ").replaceAll("\\)", "\\ ) ").replaceAll("\\=", "\\ = ").trim();
			
			this.tokens.add(line.split("\\s+"));
		}
	}
	
	public void lexing(String fileName) {
		try {
			String[] lines = Files.lines(Paths.get("./resources/"+fileName)).toArray(String[]::new);
			this.lexing(lines);
		} catch (IOException e) {}
	}

	public void printData() {
		System.out.println("\nResults:");
		System.out.println(this.serverSymbolTable.keySet());
		System.out.println(this.simulatorSymbolTable.keySet());
		
		for(Variable var : this.serverSymbolTable.values()) {
			System.out.print(var.getName() + " = " +var.getValue() + ", ");
		}
		System.out.println("");
		for(Variable var : this.simulatorSymbolTable.values()) {
			System.out.print(var.getName() + " = " +var.getValue() + ", ");
		}
		System.out.println("");
	}
	
	// The Syntax analysis (Parser) of MyInterpeter.
	// Obtains a list of strings as tokens from the lexical analyzer 
	// and verifies that the string can be the grammar for the source language. 
	public void parsing() {
		// Runs overs each String[] in the list.
		for (this.indexBlockOfTokens = 0; this.indexBlockOfTokens < this.tokens.size(); this.indexBlockOfTokens++) {
			// Runs overs each string in string[].
			for(this.indexToken = 0; this.indexToken < this.tokens.get(indexBlockOfTokens).length; this.indexToken++) {
				Command command = this.commandFactory.getCommand(this.tokens.get(indexBlockOfTokens)[this.indexToken]);
				if(command != null) {
					command.setInterpeter(this);
					new ExpressionCommand(command).calculate();
				}
			}
		}
	}
	
	// Resets the Interpeter's variables.
	private void resetInterpeter() {
		this.tokens.clear();
		this.tokens = null;
		this.tokens = new ArrayList<String[]>();
		this.indexToken = 0;
		this.indexBlockOfTokens = 0;
		this.returnedValue = 0;
		this.serverSymbolTable.clear();
		this.simulatorSymbolTable.clear();
		this.simulatorSymbolTable.put("simX", new SimulatorVariable(0.0, "simX"));
		this.simulatorSymbolTable.put("simY", new SimulatorVariable(0.0, "simY"));
		this.simulatorSymbolTable.put("simZ", new SimulatorVariable(0.0, "simZ"));
	}
}