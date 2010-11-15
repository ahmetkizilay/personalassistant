package pa;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ToDoList {

	private ToDoCommandType command;
	private Hashtable<String, String[]> arguments = new Hashtable<String, String[]>();

	public ToDoList(String[] args) {
		parseInput(args);
	}

	private void parseInput(String[] args) {
		if (args.length == 0) {
			System.out.println("Arguments Needed");
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
		if (command == ToDoCommandType.Add) {
			executeAdd();
		} else if (command == ToDoCommandType.Delete) {
			executeDelete();
		} else if (command == ToDoCommandType.Edit) {
			executeEdit();
		} else if (command == ToDoCommandType.Help) {
			executeHelp();
		} else if (command == ToDoCommandType.Search) {
			executeSearch();
		} else if (command == ToDoCommandType.Undefined) {
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
				System.out.println("Message is required. Correct Your Syntax!");
				return;
			}
			
			new PADatabaseManager().addToDo(message, detail, tags, startDate, dueDate);
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private void executeDelete() {
		System.out.println("Delete: Not Implemented Yet!");
	}

	private void executeEdit() {
		System.out.println("Edit: Not Implemented Yet");
	}

	private void executeHelp() {
		System.out.println("Help: Not Implemented Yet!");
	}

	private void executeSearch() {
		System.out.println("Search: Not Implemented Yet!");
	}

	public void executeDebug() {
		System.out.println("Command: " + command);
		Enumeration<String> commands = arguments.keys();
		while (commands.hasMoreElements()) {
			String thisFlag = commands.nextElement();
			System.out.print(thisFlag + ": ");
			String[] flagParams = arguments.get(thisFlag);
			for (int i = 0; i < flagParams.length; i++) {
				System.out.print(flagParams[i]);

				if (i != (flagParams.length - 1)) {
					System.out.print(", ");
				} else {
					System.out.println();
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

	private ToDoCommandType getTodoAction(String command) {
		if (command.equals("--add") || command.equals("-a")) {
			return ToDoCommandType.Add;
		} else if (command.equals("--edit") || command.equals("-e")) {
			return ToDoCommandType.Edit;
		} else if (command.equals("--delete") || command.equals("-d")) {
			return ToDoCommandType.Delete;
		} else if (command.equals("--search") || command.equals("-s")) {
			return ToDoCommandType.Search;
		} else if (command.equals("--help") || command.equals("-h")) {
			return ToDoCommandType.Help;
		} else {
			return ToDoCommandType.Undefined;
		}
	}

	public static void main(String[] args) {
		ToDoList todoList = new ToDoList(args);
		todoList.execute();
	}

}
