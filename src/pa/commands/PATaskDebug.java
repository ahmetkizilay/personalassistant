package pa.commands;

import java.util.Enumeration;

import pa.constants.PACommand;

public class PATaskDebug extends PATask {
	
	public PATaskDebug(String[] args) throws Exception {
		super(args);
		this.command = PACommand.DEBUG;
	}

	@Override
	public void help() {
	}
	
	@Override
	public void execute(){
		try {
		paPrinter.printDebugMessage("Command: " + command);
		Enumeration<String> commands = arguments.keys();
		while (commands.hasMoreElements()) {
			String thisFlag = commands.nextElement();
			paPrinter.printDebugMessage(thisFlag + ": ");
			String[] flagParams = arguments.get(thisFlag);
			for (int i = 0; i < flagParams.length; i++) {
				paPrinter.printDebugMessage(flagParams[i]);

				if (i != (flagParams.length - 1)) {
					paPrinter.printDebugMessage(", ");
				} else {
					paPrinter.printDebugMessage("");
				}
			}
		}
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}		
	}
	
	@Override
	protected void prompt() {
		paPrinter.printErrorMessage("PATaskDebug.prompt not implemented yet!");
	}
}
