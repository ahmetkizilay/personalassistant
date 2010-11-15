package pa;

public class PAPrinter {
	private boolean isConsoleMode;

	public PAPrinter() {
		this(true);
	}
	
	public PAPrinter(boolean consoleMode) {
		this.isConsoleMode = consoleMode;
	}

	public void printErrorMessage(String message) {
		printMessage(message, 1);
	}
	
	public void printInfoMessage(String message) {
		printMessage(message, 0);
	}
	
	public void printDebugMessage(String message) {
		printMessage(message, 2);
	}
	
	public void printMessage(String message, int messageType) {
		String outputMessage = "";
		if (isConsoleMode) {
			switch (messageType) {
			case PAConstants.MESSAGE_INFO:
				outputMessage = "\033[01;32m" + message + "\033[0m";
				break;
			case PAConstants.MESSAGE_ERROR:
				outputMessage = "\033[01;31m" + message + "\033[0m";
				break;
			case PAConstants.MESSAGE_DEBUG:
				outputMessage = message;
				break;
			default:
				outputMessage = message;
				break;
			}
		} else {
			outputMessage = message;
		}
		
		System.out.println(outputMessage);
	}
}
