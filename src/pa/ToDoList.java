package pa;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ToDoList {

	private String command;
	private Hashtable<String, String[]> arguments = new Hashtable<String, String[]>();
	private PAPrinter paPrinter;
	
	public ToDoList(String[] args) {
		paPrinter = new PAPrinter();
		parseInput(args);
	}

	private void parseInput(String[] args) {
		if (args.length == 0) {
			paPrinter.printMessage("Arguments Needed", 0);
			return;
		}

		command = getTodoAction(args[0]);

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

	private void execute() {
		if (command == PAConstants.COMMAND_ADD) {
			executeAdd();
		} else if (command == PAConstants.COMMAND_DELETE) {
			executeDelete();
		} else if (command == PAConstants.COMMAND_EDIT) {
			executeEdit();
		} else if (command == PAConstants.COMMAND_HELP) {
			executeHelp();
		} else if (command == PAConstants.COMMAND_SEARCH) {
			executeSearch();
		} else if (command == PAConstants.COMMAND_ADD) {
			executeDebug();
		}
	}

	private void executeAdd() {
		try {
			String message = getFlagMessage();
			String detail = getFlagDetail();
			String[] tags = getFlagTags();
			Date startDate = null;
			Date dueDate = null;

			if (message == null) {
				paPrinter.printMessage("Message is required. Correct Your Syntax!", 0);
				return;
			}
			
			PADatabaseManager dbManager = new PADatabaseManager();
			if(dbManager.addToDo(message, detail, tags, startDate, dueDate)) {
				paPrinter.printInfoMessage("Added A New Task!");
			}
			else {
				paPrinter.printErrorMessage("Could Not Add The Task!");
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private void executeDelete() {
		paPrinter.printErrorMessage("Delete: Not Implemented Yet!");
	}

	private void executeEdit() {
		paPrinter.printErrorMessage("Edit: Not Implemented Yet");
	}

	private void executeHelp() {
		paPrinter.printErrorMessage("Help: Not Implemented Yet!");
	}

	private void executeSearch() {
		paPrinter.printErrorMessage("Search: Not Implemented Yet!");
	}

	public void executeDebug() {
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

	private String getFlagMessage() {
		String[] messageArray = retrieveAnyOf(new String[] { "--message", "-m" });
		if (messageArray != null) {
			return messageArray[0];
		} else {
			return null;
		}
	}

	private String getFlagDetail() {
		String[] detailArray = retrieveAnyOf(new String[] { "--detail", "-d" });
		if (detailArray != null) {
			return detailArray[0];
		} else {
			return null;
		}
	}

	private String[] getFlagTags() {
		String[] tagArray = retrieveAnyOf(new String[] { "--tags", "--tag",
				"-t" });
		return tagArray;
	}

	private String[] retrieveAnyOf(String[] possibleFlagNames) {
		for (int i = 0; i < possibleFlagNames.length; i++) {
			String[] outputArray = arguments.get(possibleFlagNames[i]);
			if (outputArray != null) {
				return outputArray;
			}
		}
		return null;
	}

	private String getTodoAction(String command) {
		if (command.equals("--add") || command.equals("-a")) {
			return PAConstants.COMMAND_ADD;
		} else if (command.equals("--edit") || command.equals("-e")) {
			return PAConstants.COMMAND_EDIT;
		} else if (command.equals("--delete") || command.equals("-d")) {
			return PAConstants.COMMAND_DELETE;
		} else if (command.equals("--search") || command.equals("-s")) {
			return PAConstants.COMMAND_SEARCH;
		} else if (command.equals("--help") || command.equals("-h")) {
			return PAConstants.COMMAND_SEARCH;
		} else {
			return PAConstants.COMMAND_UNDEFINED;
		}
	}

	public static void main(String[] args) {
		ToDoList todoList = new ToDoList(args);
		todoList.execute();
	}

}
