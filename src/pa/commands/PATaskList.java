package pa.commands;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import pa.constants.PACommand;
import pa.constants.PAPriority;
import pa.constants.PASortBy;

public class PATaskList extends PATask
{

	private final int MAX_RECORD = 15;
	
	private int recordCount;
	private PASortBy sortBy;
	private boolean isDetailed;
	
	public PATaskList(String[] args) throws Exception
	{
		super(args);
		this.command = PACommand.LIST;
	}

	@Override
	public void execute()
	{
		if(!isPromptRequested()) {
			recordCount = getFlagRecordCount();
			sortBy = getFlagSortBy();
			isDetailed = getFlagIsDetailed();
		}
		
		listTask();
	}
 
	@Override
	protected void prompt()
	{
		Scanner scanner = new Scanner(System.in);
		
		paPrinter.printPromptMessage("Record Count", "Max " + MAX_RECORD + ", Enter for All");
		String recordCountString = scanner.nextLine();
		try {
			recordCount = Integer.parseInt(recordCountString);
			recordCount = Math.min(MAX_RECORD, recordCount);
		}
		catch(Exception exp) {
			recordCount = MAX_RECORD;
		}
		
		while(true) {
			paPrinter.printPromptMessage("Sort By", "StartDate/DueDate/Priority/LastEdited");
			String sortByString = scanner.nextLine();
			if(sortByString.equals("")) {
				sortBy = PASortBy.LASTEDITED;
				break;
			}
			
			sortBy  = PASortBy.parseName(sortByString);
			if(sortBy == PASortBy.UNKNOWN) {
				paPrinter.printErrorMessage(sortByString + " is not a valid input!");
			}
			else {
				break;
			}
		}
		
		while(true) {
			paPrinter.printPromptMessage("Detailed", "Y/N");
			String detailedString = scanner.nextLine();
			if(detailedString.equals("")) {
				isDetailed = true;
				break;
			} else if (detailedString.equalsIgnoreCase("y")) {
				isDetailed = true;
				break;
			} else if(detailedString.equalsIgnoreCase("n")) {
				isDetailed = false;
				break;
			} else {
				paPrinter.printErrorMessage(detailedString + " is not a valid input!");
			}					
		}
	}
	
	private int getFlagRecordCount() {
		String[] recordCountArray = retrieveAnyOf(new String[] { "--top", "-t" });
		if (recordCountArray != null)
		{
			try {
				return Integer.parseInt(recordCountArray[0]);
			}
			catch(Exception exp) {
				paPrinter.printErrorMessage(recordCountArray[0] + " is not a valid input!");
				paPrinter.printErrorMessage("Flag initialized to " + MAX_RECORD);
				return MAX_RECORD;
			}
		} else
		{
			return MAX_RECORD;
		}
	}
	
	private PASortBy getFlagSortBy() {
		String[] sortByArray = retrieveAnyOf(new String[] { "--sort-by", "-s" });
		if (sortByArray != null)
		{
			return PASortBy.parseName(sortByArray[0]);
		} else
		{
			return PASortBy.UNKNOWN;
		}
	}
	
	private boolean getFlagIsDetailed() {
		String[] detailedArray = retrieveAnyOf(new String[] {"--detailed", "-d"});
		return detailedArray != null;
	}
	
	private void listTask() {		
		PreparedStatement stmt = null;
		try {
			String queryString = "SELECT * FROM `tasks` ";
			queryString += "WHERE `status` = 0 ";
			queryString += "ORDER BY `" + (sortBy != PASortBy.UNKNOWN ? sortBy.getName() : PASortBy.LASTEDITED.getName()) + "` ";
			queryString += "LIMIT " + recordCount;
			
			stmt = dbManager.getConnection().prepareStatement(queryString);
			stmt.clearParameters();
			
			ResultSet rs = stmt.executeQuery();
			if(rs == null) {
				throw new Exception("Query yielded null results!");
			}
			
			while(rs.next()) {
				int thisId = rs.getInt("id");
				String thisMessage = rs.getString("message");				
				int thisPriority = rs.getInt("priority");
				String thisDetail = isDetailed ? rs.getString("detail") : "";	
				
				Timestamp thisTimestamp = rs.getTimestamp("duedate");
				Date thisDueDate = (thisTimestamp == null) ? null : new Date(rs.getTimestamp("duedate").getTime());
				
				String tagQuery = "SELECT `name` FROM `tags` WHERE `id` IN (SELECT `tag_id` FROM `tags_tasks` WHERE `task_id` = ?)";
				stmt = dbManager.getConnection().prepareStatement(tagQuery);
				stmt.clearParameters();
				stmt.setInt(1, thisId);
				ResultSet tagsRS = stmt.executeQuery();
				if(tagsRS == null) {
					throw new Exception("Query yielded null results!");
				}
				List<String> tagsList = new ArrayList<String>();
				while(tagsRS.next()) {
					tagsList.add(tagsRS.getString("name"));
				}
				paPrinter.printTask(thisId, thisMessage, thisDetail, PAPriority.parseCode(thisPriority), thisDueDate, tagsList.toArray(new String[tagsList.size()]));
			}
			
			
		}
		catch(Exception exp) {
			exp.printStackTrace();
			paPrinter.printErrorMessage("Error: " + exp.getMessage());
			paPrinter.printErrorMessage("Could Not Retrieve Any Results");
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
}
