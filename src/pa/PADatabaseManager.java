package pa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public class PADatabaseManager {
	Connection con = null;
	CallableStatement stmt = null;

	public PADatabaseManager() throws Exception {
		initConnection();
	}

	private void initConnection() throws Exception {

		PAConfig paConfig = new PAConfig();
		
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + paConfig.getDBUrl() + ":" + paConfig.getDBPort() + "/" + 
					paConfig.getDBName() + "?noAccessToProcedureBodies=true";
		
		String user = paConfig.getDBUserName();
		String password = paConfig.getDBPassword();

		con = DriverManager.getConnection(url, user, password);
	}
	
	public boolean addToDo(String message, String detail, String[] tags,
			Date startDate, Date dueDate) {
		try {

			stmt = con.prepareCall("CALL SP_INS_TASK(?, ?, ?, ?, ?);");
			stmt.clearParameters();
			stmt.setString(1, message);
			stmt.setString(2, detail);
			stmt.setTimestamp(3, (startDate == null) ? null : new Timestamp(startDate.getTime()));
			stmt.setTimestamp(4, (dueDate == null) ? null : new Timestamp(dueDate.getTime()));
			stmt.registerOutParameter(5, Types.INTEGER);
			stmt.execute();
			
			int taskId = stmt.getInt(5);
		
			if(tags != null) {
				for(int i = 0; i < tags.length; i++){
					stmt = con.prepareCall("CALL SP_INS_TAG(?, ?)");
					stmt.clearParameters();
					stmt.setInt(1, taskId);
					stmt.setString(2, tags[i]);
					stmt.execute();
				}
			}	
			
			return true;
		} catch (Exception exp) {
			exp.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
				}
			}
		}

	}
}
