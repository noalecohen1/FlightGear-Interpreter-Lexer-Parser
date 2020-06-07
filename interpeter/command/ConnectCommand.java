package model.interpeter.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.interpeter.expression.ShuntingYardAlgorithm;

public class ConnectCommand extends Command{
	// Data members:
	private static volatile boolean isConnected = false; 
	private static Socket simulatorServerSocket = null;
	private static PrintWriter outToServer = null;
	
	// CTOR.
	public ConnectCommand() { super(); }
	
	@Override
	public int execute() {

		ArrayList<String[]> tokens = this.interpeter.getTokens();
		int indexBlockOfTokens = this.interpeter.getIndexBlockOfTokens();
		int indexToken = this.interpeter.getIndexToken();
		String ip = tokens.get(indexBlockOfTokens)[indexToken + 1];
		int port = 0;
		
		ArrayList<String> expression = new ArrayList<String>();
		String[] block = this.interpeter.getTokens().get(this.interpeter.getIndexBlockOfTokens()); 
		
		for(int i = (indexToken + 2); i < block.length; i++) {
			expression.add(block[i]);
		}
		
		port = (int)ShuntingYardAlgorithm.execute(expression, this.interpeter.getServerSymbolTable());
		
		while(isConnected == false) {
			try {
				simulatorServerSocket = new Socket(ip, port);
				outToServer = new PrintWriter(simulatorServerSocket.getOutputStream());
						
				isConnected = true;
			} catch (UnknownHostException e) { e.printStackTrace(); } 
			  catch (IOException e) { e.printStackTrace(); }
		}
		
		this.interpeter.setIndexToken(expression.size() + 1);
		
		return 0;
	}
	
	public static void sendToServer(String line) {
		if(isConnected == true) {
			outToServer.println(line);
			outToServer.flush();
		}
	}
	
	public static void closeConnection() {
		if(isConnected == true) {
			sendToServer("bye");

			outToServer.close();
			
			while(true) {
				try {
					simulatorServerSocket.close();
					break;
				} catch (IOException e) { e.printStackTrace(); }
			}
			
			isConnected = false;
//			outToServer = null;
//			simulatorServerSocket= null;
		}
	}
	
}