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
	
	public void printMessage(String message, PAMessage messageType) {
		String outputMessage = "";
		if (isConsoleMode) {
			switch (messageType) {
			case INFO:
				outputMessage = "\033[01;32m" + message + "\033[0m";
				break;
			case ERROR:
				outputMessage = "\033[01;31m" + message + "\033[0m";
				break;
			case DEBUG:
				outputMessage = message;
				break;
			default:
				outputMessage = message;
				break;
			}
		} else {
			outputMessage = messageType.getName() + ": " + message;
		}
		
		System.out.println(outputMessage);
	}
}
