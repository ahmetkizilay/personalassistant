package pa.utils;

import pa.constants.PAMessage;

public class PAPrinter {
	private boolean isConsoleMode;

	public PAPrinter() {
		this(true);
	}
	
	public PAPrinter(boolean consoleMode) {
		this.isConsoleMode = consoleMode;
	}

	public void printErrorMessage(String message) {
		printMessage(message, PAMessage.ERROR);
	}
	
	public void printInfoMessage(String message) {
		printMessage(message, PAMessage.INFO);
	}
	
	public void printDebugMessage(String message) {
		printMessage(message, PAMessage.DEBUG);
	}
	
	public void printPromptMessage(String message, String detail) {
		String outputMessage = message;
		
		if(!detail.equals("")) {
			outputMessage += "(" + detail + ")";
		}		
		outputMessage += ": ";
		
		printMessage(outputMessage, PAMessage.PROMPT);
	}
	
	public void printMessage(String message, PAMessage messageType) {
		String outputMessage = "";
		if (isConsoleMode) {
			switch (messageType) {
			case INFO:
				outputMessage = "\033[01;32m" + message + "\033[0m\n";
				break;
			case ERROR:
				outputMessage = "\033[01;31m" + message + "\033[0m\n";
				break;
			case DEBUG:
				outputMessage = message + "\n";
				break;
			case PROMPT:
				outputMessage = message;
				break;
			default:
				outputMessage = message + "\n";
				break;
			}
		} else {
			outputMessage = messageType.getName() + ": " + message + "\n";
		}
		
		System.out.print(outputMessage);
	}
}
