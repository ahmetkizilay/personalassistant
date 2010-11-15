package pa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import pa.constants.PACommand;

public final class PAUtils {
	private static SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
	
	public static PACommand getTodoAction(String command) {
		if (command.equals("--add") || command.equals("-a")) {
			return PACommand.ADD;
		} else if (command.equals("--edit") || command.equals("-e")) {
			return PACommand.EDIT;
		} else if (command.equals("--delete") || command.equals("-d")) {
			return PACommand.DELETE;
		} else if (command.equals("--search") || command.equals("-s")) {
			return PACommand.SEARCH;
		} else if (command.equals("--help") || command.equals("-h")) {
			return PACommand.SEARCH;
		} else if(command.equals("--list") || command.equals("-l")) {
			return PACommand.LIST;
		} else {
			return PACommand.UNDEFINED;
		}
	}
	
	public static Date parseDate(String dateString) throws Exception{
		return formatter.parse(dateString);
	}
	
	public static String formatDate(Date date) {
		return formatter.format(date);
	}
}
