package model.interpeter.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.interpeter.Interpeter;
import model.interpeter.expression.ShuntingYardAlgorithm;
import model.interpeter.expression.SimulatorVariable;

public class OpenDataServerCommand extends Command{
	// Data members:
	private static volatile boolean isConnected = false;
	private int port;
	private int latency;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static BufferedReader inputFromClient = null;
	
	// CTOR.
	public OpenDataServerCommand() {
		super();
		this.port = 0;
		this.latency = 0;
	}
	
	@Override
	public int execute() {
		calculateExpression();
		
		new Thread(() -> runServer()).start();
		
		return 0;
	}
	
	public static void stopServer() { isConnected = false; }
	
	private void runServer() {
		try {			
			while(isConnected == false) {
				serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(1000);
				clientSocket = serverSocket.accept();
				inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				isConnected = true;
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		readLines();
		
		try {
			inputFromClient.close();
		} catch (IOException e1) { e1.printStackTrace(); }
		
		while(true) {
			try {
				if(clientSocket.isClosed() == false) { clientSocket.close(); }
				serverSocket.close();
				break;
			} catch (IOException e) { e.printStackTrace(); }
		}
		
//		this.inputFromClient = null;
//		this.clientSocket = null;
//		this.serverSocket = null;
	}
	
	private void readLines() {
		String[] variblesNames = { "simX", "simY", "simZ" };
		
		String[] variblesValues;
		String line;
		
		try {
			while(isConnected == true) {
			
				if((line = inputFromClient.readLine()) == null) { continue; } 
				else {
					variblesValues = line.split(",");
					
					for(int i = 0; i < variblesNames.length; i++) {
						if(isConnected == false) { break; }
						String simulatorVariableName = variblesNames[i];
						double simulatorVariableValue = Double.parseDouble(variblesValues[i]);
						
						
						System.out.println("BEFOR");
						this.interpeter.printData();
						
						
						SimulatorVariable simulatorVariable = this.interpeter.getSimulatorSymbolTable().get(simulatorVariableName);
						
						if(simulatorVariable != null) {
							simulatorVariable.setValue(simulatorVariableValue);
						} 
						else {
							simulatorVariable = new SimulatorVariable(simulatorVariableValue, simulatorVariableName);
							simulatorVariable.setValue(simulatorVariableValue);
							this.interpeter.getSimulatorSymbolTable().put(simulatorVariableName, simulatorVariable);
						}
						
						
						System.out.println("CHECK");
						this.interpeter.printData();
						
					}
					
					Thread.sleep(1000/this.latency);
				} 
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
	  	  catch (IOException e) { e.printStackTrace(); }
	}
	
	private void calculateExpression( ) {
		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken() + 1;
		int lengthOfBlock = tokens.get(indexBlockOfTokens).length;
		String[] str = tokens.get(indexBlockOfTokens);
		ArrayList<String> list = new ArrayList<String>();
		
		// Creates the server's port.
		for(; indexToken < (lengthOfBlock - 1); indexToken++) {
			// Checks for: 
			// 1. [..."number", "number"...]
			// 2. [...")", "number"...]
			// 3. [...")", "("...]
			// 4. [..."number", "("...]
			if((ShuntingYardAlgorithm.isDouble(str[indexToken]) && ShuntingYardAlgorithm.isDouble(str[indexToken + 1]) ||
				(str[indexToken].equals(")") && ShuntingYardAlgorithm.isDouble(str[indexToken + 1])) ||
				(str[indexToken].equals(")") && str[indexToken+1].equals("(")) || 
				(ShuntingYardAlgorithm.isDouble(str[indexToken]) && str[indexToken+1].equals("(")))) {
				list.add(str[indexToken]);
				break;
			}
			list.add(str[indexToken]);			
		}
		
		this.port = (int)ShuntingYardAlgorithm.execute(list, new Interpeter().getServerSymbolTable());
		
		list.clear();
		
		for(; indexToken < lengthOfBlock; indexToken++) {
			list.add(str[indexToken]);			
		}
		
		this.latency= (int)ShuntingYardAlgorithm.execute(list,this.interpeter.getServerSymbolTable());
		
		this.interpeter.setIndexToken(str.length);
	}

}