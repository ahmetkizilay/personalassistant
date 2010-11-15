package pa.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public class PADatabaseManager {
	private Connection con = null;

	public PADatabaseManager() throws Exception {
		initConnection();
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public void closeConnection() {
		try {
			if(con != null && !con.isClosed()) {
				con.close();
			}
		}
		catch(Exception exp) {
			
		}
	}
	
	public boolean commit() {
		try {
			if(con != null && !con.isClosed()) {
				con.commit();
			}
			return true;
		}
		catch(Exception exp) {
			this.rollback();
			return false;
		}
	}
	
	public boolean rollback() {
		try {
			if(con != null && !con.isClosed()) {
				con.rollback();
			}
			return true;
		}
		catch(Exception exp) {
			return false;
		}
	}

	private void initConnection() throws Exception {
		
		PAConfig paConfig = new PAConfig();
		
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + paConfig.getDBUrl() + ":" + paConfig.getDBPort() + "/" + 
					paConfig.getDBName() + "?noAccessToProcedureBodies=true";
		
		String user = paConfig.getDBUserName();
		String password = paConfig.getDBPassword();

		con = DriverManager.getConnection(url, user, password);
		con.setAutoCommit(false);
	}
	
}
