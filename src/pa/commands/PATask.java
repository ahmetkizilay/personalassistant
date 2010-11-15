package pa.commands;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import pa.utils.PADatabaseManager;
import pa.utils.PAPrinter;
import pa.constants.PACommand;

public abstract class PATask {
	protected Hashtable<String, String[]> arguments = new Hashtable<String, String[]>();
	protected PADatabaseManager dbManager;
	protected PAPrinter paPrinter;
	protected PACommand command;

	protected void createArgumentsTable(String[] args) {
		for (int i = 1; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				String currentFlagName = args[i];
				List<String> currentFlagParams = new ArrayList<String>();
				while ((args.length > i + 1) && !args[i + 1].startsWith("-")) {
					i++;
					currentFlagParams.add(args[i]);
				}
				;
				arguments.put(currentFlagName, currentFlagParams
						.toArray(new String[currentFlagParams.size()]));
			}
		}
	}

	protected String[] retrieveAnyOf(String[] possibleFlagNames) {
		for (int i = 0; i < possibleFlagNames.length; i++) {
			String[] outputArray = arguments.get(possibleFlagNames[i]);
			if (outputArray != null) {
				return outputArray;
			}
		}
		return null;
	}
	
	private boolean isRunningOnConsole() {
		String[] consoleArray = retrieveAnyOf(new String[]{"--no-console", "-nc"});
		if(consoleArray == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean isPromptRequested() {
		String[] promptArray = retrieveAnyOf(new String[]{"--interactive", "-i"});
		if(promptArray == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public PACommand getCommand() {
		return this.command;
	}

	public PATask() throws Exception {}
	
	public PATask(String[] args) throws Exception {

		createArgumentsTable(args);
		paPrinter = new PAPrinter(isRunningOnConsole());
		if(isPromptRequested()) {
			prompt();
		}
		dbManager = new PADatabaseManager();
	}

	public abstract void execute();
	protected abstract void prompt();
	
	
}
