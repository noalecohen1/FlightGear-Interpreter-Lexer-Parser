package model.interpeter.command;

public class IfCommand extends ConditionParser {
	// CTOR.
	public IfCommand() { super(); }
	
	
	@Override
	public int execute() {
		super.execute();
		
		if(checkCondtion()) {
			executeListOfCommands();
		}
		
		this.interpeter.setIndexBlockOfTokens(this.startIndexBlockOfTokens + this.commandList.size());
		this.interpeter.setIndexToken(0);
		return 0;
	}

}