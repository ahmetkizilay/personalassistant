package pa.utils;

import java.sql.Connection;
import java.sql.DriverManager;


public class PADatabaseManager {
	private Connection con = null;

	public PADatabaseManager() throws Exception {
		initConnection(null);
	}
	
	public PADatabaseManager(String configFile) throws Exception {
		initConnection(configFile);
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

	private void initConnection(String configFile) throws Exception {
		
		PAConfig paConfig = new PAConfig(configFile);
		
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + paConfig.getDBUrl() + ":" + paConfig.getDBPort() + "/" + 
					paConfig.getDBName() + "?noAccessToProcedureBodies=true";
		
		String user = paConfig.getDBUserName();
		String password = paConfig.getDBPassword();

		con = DriverManager.getConnection(url, user, password);
		con.setAutoCommit(false);
	}
	
}
