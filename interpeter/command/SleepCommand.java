package model.interpeter.command;

import java.util.ArrayList;

import model.interpeter.expression.ShuntingYardAlgorithm;

public class SleepCommand extends Command {
	// CTOR.
	public SleepCommand() { super(); }
	
	@Override
	public int execute() {
		int indexToken = this.interpeter.getIndexToken();
		String[] block = this.interpeter.getTokens().get(this.interpeter.getIndexBlockOfTokens()); 
		ArrayList<String> expression = new ArrayList<String>();
		
		for(int i = (indexToken + 1); i < block.length; i++) {
			expression.add(block[i]);
		}
		
		int timeToSleep = (int) ShuntingYardAlgorithm.execute(expression, this.interpeter.getServerSymbolTable());
		
		try {
			Thread.sleep(timeToSleep);
		} catch (InterruptedException e) { e.printStackTrace(); }
		
		
		this.interpeter.setIndexToken(expression.size() + this.interpeter.getIndexToken());
		
		return 0;
	}
}