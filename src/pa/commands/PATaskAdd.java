package pa.commands;

import java.util.Date;

import pa.constants.PACommand;
import pa.constants.PAPriority;

public class PATaskAdd extends PATask{
	public PATaskAdd(String[] args) throws Exception{
		super(args);
		this.command = PACommand.ADD;
	}

	@Override
	public void execute(){
		try {
			String message = getFlagMessage();
			String detail = getFlagDetail();
			String[] tags = getFlagTags();
			Date startDate = null;
			Date dueDate = null;
			PAPriority priority = getFlagPriority();

			if (message == null) {
				paPrinter.printErrorMessage("Message is required. Correct Your Syntax!");
				return;
			}
			
			if(dbManager.addTask(message, detail, tags, startDate, dueDate, priority.getCode())) {
				paPrinter.printInfoMessage("Added A New Task!");
			}
			else {
				paPrinter.printErrorMessage("Could Not Add The Task!");
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	@Override
	public PACommand getCommand() {
		return PACommand.ADD;
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

	private PAPriority getFlagPriority() {
		String[] priorityArray = retrieveAnyOf(new String[]{"--priority", "-p"});
		if(priorityArray != null) {
			return PAPriority.parseName(priorityArray[0]);
		}
		return PAPriority.LOW;
	}

}
