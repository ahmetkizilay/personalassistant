package pa.commands;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

import pa.constants.PACommand;
import pa.constants.PAPriority;
import pa.constants.PAStatus;
import pa.utils.PAUtils;

public class PATaskEdit extends PATask
{

	private int taskId;
	private PAStatus status;
	private String detailMode;
	private String detail;
	private PAPriority priority;
	private Date dueDate;
	
	public PATaskEdit(String[] args) throws Exception{
		super(args);
		this.command = PACommand.EDIT;
	}
	
	@Override
	public void help() {
		String helpText = "Personal Asistant: Syntax for EDIT \n";
		helpText += "![] fields are required, [] fields are optional, <> fields are input\n";
		helpText += "[--interactive|-i]\n";
		helpText += "![--task-id|-ti] <taskId> [--status|-s] <Active|Completed|Discard> [--complete|-c]\n"; 
		helpText += "[[--detail|--detail-append|-da]|[--detail-override|-do]] <detail>\n";
		helpText += "[--priority|-p] <priority>\n";
		paPrinter.printInfoMessage(helpText);
	}
	
	@Override
	public void execute()
	{
		if(!isPromptRequested()) {
			taskId = getFlagTaskId();
			if(taskId < 1) {
				paPrinter.printErrorMessage("Task Id is not valid");
				return;
			}
			
			if(getFlagIsCompleted()) {
				status = PAStatus.COMPLETED;
			} else {
				status = getFlagStatus();
			}
			
			detailMode = getFlagDetailMode();
			if(!detailMode.equalsIgnoreCase("none")) {
				detail = getFlagDetailMessage();
			}
			
			priority = getFlagPriority();
			
			try {
				dueDate = getFlagDueDate();
			} catch(Exception exp) {
				paPrinter.printErrorMessage(exp.getMessage());
				return;
			}
		}
		
		editTask();
	}
	
	private void editTask() {
		PreparedStatement stmt = null;
		try {
			String queryString = "UPDATE `tasks` SET ";
			boolean anythingToSet = false;
			
			if(status != PAStatus.UNKNOWN) {
				queryString += "`status` = ?, ";
				anythingToSet = true;
			}
			
			if(!detailMode.equalsIgnoreCase("none")) {
				if(detailMode.equalsIgnoreCase("append")) {
					queryString += " `detail` = CONCAT(`detail`, '\n', ?), ";
				} else {
					queryString += " `detail` = ?, ";
				}
				anythingToSet = true;
			}
			
			if(priority != PAPriority.UNKNOWN) {
				queryString += "`priority` = ?, ";
				anythingToSet = true;
			}
			
			if(dueDate != null) {
				queryString += "`duedate` = ?, ";
				anythingToSet = true;
			}
			
			if(!anythingToSet) {
				throw new Exception("Nothing is Specified For Edit. Check Your Syntax");
			}
			
			queryString = queryString.substring(0, queryString.length() - 2);
			
			queryString += " WHERE `id` = ?";
			
			stmt = dbManager.getConnection().prepareStatement(queryString);
			stmt.clearParameters();
			
			int parameterIndex = 1;
			if(status != PAStatus.UNKNOWN) {
				stmt.setInt(parameterIndex++, status.getCode());
			}
			
			if(!detailMode.equalsIgnoreCase("none")) {
				stmt.setString(parameterIndex++, detail);
			}
			
			if(priority != PAPriority.UNKNOWN) {
				stmt.setInt(parameterIndex++, priority.getCode());
			}
						
			if(dueDate != null) {
				stmt.setTimestamp(parameterIndex++, new Timestamp(dueDate.getTime()));
			}
			
			stmt.setInt(parameterIndex, taskId);
			
			int updateResult = stmt.executeUpdate();
			if(updateResult == 1) {
				dbManager.commit();
				paPrinter.printInfoMessage("Edited Task!");
			} else {
				throw new Exception("Unexpected Update Result");
			}
		}
		catch(Exception exp) {
			exp.printStackTrace();
			dbManager.rollback();
			paPrinter.printErrorMessage("Error: " + exp.getMessage());
			paPrinter.printErrorMessage("Could Not Edit Task!");
		}
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}

			dbManager.closeConnection();

		}
	}
	
	@Override
	protected void prompt()
	{
		Scanner scanner = new Scanner(System.in);
		
		// task id
		while(true) {
			paPrinter.printPromptMessage("Task Id", "");
			String idString = scanner.nextLine();
			try {
				taskId = Integer.parseInt(idString);
				break;
			}
			catch(Exception exp) {
				paPrinter.printErrorMessage(idString + " is not a valid input!");
			}
		}
		
		// status
		while(true) {
			
			paPrinter.printPromptMessage("Set Status", "Active/Completed/Discard");
			String statusString = scanner.nextLine();
			if(statusString.equals("")) {
				status = PAStatus.UNKNOWN;
				break;
			} else if (statusString.startsWith("a") || statusString.startsWith("A")) {
				status = PAStatus.ACTIVE;
				break;
			} else if (statusString.startsWith("c") || statusString.startsWith("C")) {
				status = PAStatus.COMPLETED;
				break;
			} else if (statusString.startsWith("d") || statusString.startsWith("D")) {
				status = PAStatus.DISCARDED;
				break;
			} else {
				paPrinter.printErrorMessage(statusString + " is not a valid input!");
			}
		}
		
		// detail mode type
		while(true) {
			
			paPrinter.printPromptMessage("Modify Detail", "Append/Override/None");
			String detailModeString = scanner.nextLine();
			if(detailModeString.equals("") || detailModeString.startsWith("n") || detailModeString.startsWith("N")) {
				detailMode = "None";
				break;
			} else if (detailModeString.startsWith("a") || detailModeString.startsWith("A")) {
				detailMode = "Append";
				break;
			} else if (detailModeString.startsWith("o") || detailModeString.startsWith("O")) {
				detailMode = "Override";
				break;
			} else {
				paPrinter.printErrorMessage(detailModeString + " is not a valid input!");
			}
		}
		
		// detail message
		if(!detailMode.equalsIgnoreCase("none")) {
			paPrinter.printPromptMessage("Enter Detail", "");
			detail = scanner.nextLine();
		}
		
		// priority
		while(true) {
			
			paPrinter.printPromptMessage("Modify Priority", "Low/Medium/High");
			String priorityString = scanner.nextLine();
			if(priorityString.equals("")) {
				priority = PAPriority.UNKNOWN;
				break;
			} else if (priorityString.startsWith("l") || priorityString.startsWith("L")) {
				priority = PAPriority.LOW;
				break;
			} else if (priorityString.startsWith("m") || priorityString.startsWith("M")) {
				priority = PAPriority.MEDIUM;
				break;
			} else if (priorityString.startsWith("h") || priorityString.startsWith("H")) {
				priority = PAPriority.MEDIUM;
				break;
			} else {
				paPrinter.printErrorMessage(priorityString + " is not a valid input!");
			}
		}

		// due date
		while(true) {
			paPrinter.printPromptMessage("Due Date", "MM-dd-yyyy HH:mm");
			String dueDateString = scanner.nextLine();
			if(dueDateString.equals("")) {
				dueDate = null;
				break;
			}
			
			try {
				dueDate = PAUtils.parseDate(dueDateString); 
				break;
			}
			catch(Exception exp) {
				paPrinter.printErrorMessage(dueDateString + " is not a valid input!");
			}
		}
	}
	
	private int getFlagTaskId() {
		String[] taskIdString = retrieveAnyOf(new String[]{"--task-id", "-ti"});
		
		if(taskIdString != null) {
			try {
				return Integer.parseInt(taskIdString[0]);
			}catch(Exception exp) {
				return -1;
			}
		}
		return -1;
	}
	
	private PAStatus getFlagStatus() {
		String[] statusString = retrieveAnyOf(new String[]{"--status", "-s"});
		if(statusString != null) {
			return PAStatus.parseName(statusString[0]);
		}
		else {
			return PAStatus.UNKNOWN;
		}
	}
	
	private String getFlagDetailMode() {
		if(retrieveAnyOf(new String[]{"--detail", "--detail-append", "-da"}) != null) {
			return "Append";
		} else if(retrieveAnyOf(new String[]{"--detail-override", "-do"}) != null) {
			return "Override";
		} else {
			return "None";
		}
	}
	
	private String getFlagDetailMessage() {
		String[] detailArray = retrieveAnyOf(new String[]{"--detail", "--detail-append", "-da", "--detail-override", "-do"});
		if(detailArray == null) {
			return null;
		} else {
			return detailArray[0];
		}
	}
	
	private PAPriority getFlagPriority() {
		String[] priorityString = retrieveAnyOf(new String[]{"--priority", "-p"});
		if(priorityString != null) {
			return PAPriority.parseName(priorityString[0]);
		}
		else {
			return PAPriority.UNKNOWN;
		}
	}
	
	private Date getFlagDueDate() throws Exception {
		String[] dueDateArray = retrieveAnyOf(new String[] {"--due-date", "-dd"});
		if(dueDateArray == null) {
			return null;
		}
		else {
			try{
			return PAUtils.parseDate(dueDateArray[0]);
			}
			catch(Exception exp) {
				throw new Exception(dueDateArray[0] + " is not a valid date!");
			}
		}
	}
	
	private boolean getFlagIsCompleted() {
		if(retrieveAnyOf(new String[]{"--complete", "-c"}) != null) {
			return true;
		}
		else {
			return false;
		}
	}
}
