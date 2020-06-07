package model.interpeter.command;

public class LoopCommand extends ConditionParser {
	// CTOR.
	public LoopCommand() { super(); }
	
	
	@Override
	public int execute() {
		super.execute();
		
		while(checkCondtion()) {
			executeListOfCommands();
		}
		
		this.interpeter.setIndexBlockOfTokens(this.startIndexBlockOfTokens + this.commandList.size());
		this.interpeter.setIndexToken(0);
		return 0;
	}

}