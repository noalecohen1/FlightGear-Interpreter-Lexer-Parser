package model.interpeter.command;

public class DisconnectCommand extends Command {
	// CTOR.
	public DisconnectCommand() { super(); }
	
	@Override
	public int execute() {
		ConnectCommand.closeConnection();
		
		OpenDataServerCommand.stopServer();
		
		return 1;
	}

}