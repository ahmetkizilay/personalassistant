package pa.utils;

import java.util.Date;

import pa.constants.PAMessage;
import pa.constants.PAPriority;
import pa.constants.PAStatus;

public class PAPrinter
{
	private boolean isConsoleMode;

	public PAPrinter()
	{
		this(true);
	}

	public PAPrinter(boolean consoleMode)
	{
		this.isConsoleMode = consoleMode;
	}

	public void printErrorMessage(String message)
	{
		printMessage(message, PAMessage.ERROR);
	}

	public void printInfoMessage(String message)
	{
		printMessage(message, PAMessage.INFO);
	}

	public void printDebugMessage(String message)
	{
		printMessage(message, PAMessage.DEBUG);
	}

	public void printTask(int id, String message, String detail, PAPriority priority, Date dueDate, PAStatus status, String[] tagsArray)
	{
		String outputText = id + ":\t" + message + "\n";
		outputText += "Priority:\t" + priority.getName() + "\n";
		outputText += (detail != null && detail.length() > 0) ? "Detail: " + detail + "\n" : "";
		outputText += "Status: " + status.getName() + "\n";
		outputText += "DueDate:\t" + ((dueDate == null) ? "Not Specified" : PAUtils.formatDate(dueDate)) + "\n";
		outputText += "Tags: ";
		if (tagsArray != null)
		{
			for (int i = 0; i < tagsArray.length; i++)
			{
				outputText += tagsArray[i];
				outputText += (i != (tagsArray.length - 1)) ? (((i % 4) != 3) ? ", " : "\n\t") : "";
			}
		} else
		{
			outputText += "Not Specified";
		}
		System.out.println(colorizeByPriority(outputText, priority));
	}

	private String colorizeByPriority(String message, PAPriority priority)
	{
		switch (priority)
		{
		case HIGH:
			return "\033[0;33m" + message + "\033[0m";
		case MEDIUM:
			return "\033[0;34m" + message + "\033[0m";
		case LOW:
			return "\033[0;35m" + message + "\033[0m";
		default:
			return message;
		}
	}

	public void printPromptMessage(String message, String detail)
	{
		String outputMessage = message;

		if (!detail.equals(""))
		{
			outputMessage += "(" + detail + ")";
		}
		outputMessage += ": ";

		printMessage(outputMessage, PAMessage.PROMPT);
	}

	public void printMessage(String message, PAMessage messageType)
	{
		String outputMessage = "";
		if (isConsoleMode)
		{
			switch (messageType)
			{
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
		} else
		{
			outputMessage = messageType.getName() + ": " + message + "\n";
		}

		System.out.print(outputMessage);
	}
}
