package pa.commands;

import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import pa.constants.PACommand;
import pa.constants.PAPriority;
import pa.utils.PAUtils;

public class PATaskAdd extends PATask
{

	private String message;
	private String detail;
	private Date startDate;
	private Date dueDate;
	private PAPriority priority;
	private String[] tagsArray;

	public PATaskAdd(String[] args) throws Exception
	{
		super(args);
		this.command = PACommand.ADD;
	}

	@Override
	public void execute()
	{
		if (!isPromptRequested())
		{
			message = getFlagMessage();
			detail = getFlagDetail();
			tagsArray = getFlagTags();
			startDate = null;
			dueDate = null;
			priority = getFlagPriority();

			if (message == null)
			{
				paPrinter.printErrorMessage("Message is required. Correct Your Syntax!");
				return;
			}
		}

		addTask();
	}
	
	private void addTask() {
		CallableStatement stmt = null;
		try {
			stmt = dbManager.getConnection().prepareCall("CALL SP_INS_TASK(?, ?, ?, ?, ?, ?);");
			stmt.clearParameters();
			stmt.setString(1, message);
			stmt.setString(2, detail);
			stmt.setTimestamp(3, (startDate == null) ? null : new Timestamp(startDate.getTime()));
			stmt.setTimestamp(4, (dueDate == null) ? null : new Timestamp(dueDate.getTime()));
			stmt.setInt(5, priority.getCode());
			stmt.registerOutParameter(6, Types.INTEGER);
			stmt.execute();
			
			int taskId = stmt.getInt(6);
		
			if(tagsArray != null) {
				for(int i = 0; i < tagsArray.length; i++){
					stmt = dbManager.getConnection().prepareCall("CALL SP_INS_TAG(?, ?)");
					stmt.clearParameters();
					stmt.setInt(1, taskId);
					stmt.setString(2, tagsArray[i]);
					stmt.execute();
				}
			}
			
			if(dbManager.commit()) {
				paPrinter.printInfoMessage("Added A New Task!");
			}
			else {
				throw new Exception("Failed To Commit Changes!");
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			dbManager.rollback();
			paPrinter.printErrorMessage("Error: " + exp.getMessage());
			paPrinter.printErrorMessage("Could Not Add Task!");
		} finally {
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

		while (true)
		{
			paPrinter.printPromptMessage("Message", "Cannot Be Empty");
			message = scanner.nextLine();
			if (!message.equals(""))
			{
				break;
			}
		}

		paPrinter.printPromptMessage("Detail", "");
		detail = scanner.nextLine();

		while (true)
		{
			paPrinter.printPromptMessage("Start Date", "MM-dd-yyyy HH:mm");
			String startDateString = scanner.nextLine();
			if (!startDateString.equals(""))
			{
				try
				{
					startDate = PAUtils.parseDate(startDateString);
					break;
				} catch (Exception exp)
				{
					paPrinter.printErrorMessage(startDateString + " is not a valid date input!");
					startDate = null;
				}
			} else
			{
				break;
			}
		}

		while (true)
		{
			paPrinter.printPromptMessage("Due Date", "MM-dd-yyyy HH:mm");
			String dueDateString = scanner.nextLine();
			if (!dueDateString.equals(""))
			{
				try
				{
					dueDate = PAUtils.parseDate(dueDateString);
					break;
				} catch (Exception exp)
				{
					paPrinter.printErrorMessage(dueDateString + " is not a valid date input!");
					dueDate = null;
				}
			} else
			{
				break;
			}
		}

		while (true)
		{
			paPrinter.printPromptMessage("Priority", "L/M/H");
			String priorityString = scanner.nextLine();
			if (!priorityString.equals(""))
			{
				if (priorityString.equalsIgnoreCase("l"))
				{
					priority = PAPriority.LOW;
					break;
				} else if (priorityString.equalsIgnoreCase("m"))
				{
					priority = PAPriority.MEDIUM;
					break;
				} else if (priorityString.equalsIgnoreCase("h"))
				{
					priority = PAPriority.HIGH;
					break;
				} else
				{
					paPrinter.printErrorMessage(priorityString + " is not a valid input!");
				}
			} else
			{
				priority = PAPriority.LOW;
				break;
			}
		}

		List<String> tagList = new ArrayList<String>();
		while (true)
		{
			paPrinter.printPromptMessage("Tag", "");
			String thisTag = scanner.nextLine();
			if (thisTag.equalsIgnoreCase("q"))
			{
				break;
			} else
			{
				tagList.add(thisTag);
			}
		}

		if (tagList.size() > 0)
		{
			tagsArray = tagList.toArray(new String[tagList.size()]);
		} else
		{
			tagsArray = null;
		}
	}

	private String getFlagMessage()
	{
		String[] messageArray = retrieveAnyOf(new String[] { "--message", "-m" });
		if (messageArray != null)
		{
			return messageArray[0];
		} else
		{
			return null;
		}
	}

	private String getFlagDetail()
	{
		String[] detailArray = retrieveAnyOf(new String[] { "--detail", "-d" });
		if (detailArray != null)
		{
			return detailArray[0];
		} else
		{
			return null;
		}
	}

	private String[] getFlagTags()
	{
		String[] tagArray = retrieveAnyOf(new String[] { "--tags", "--tag", "-t" });
		return tagArray;
	}

	private PAPriority getFlagPriority()
	{
		String[] priorityArray = retrieveAnyOf(new String[] { "--priority", "-p" });
		if (priorityArray != null)
		{
			return PAPriority.parseName(priorityArray[0]);
		}
		return PAPriority.LOW;
	}

}
