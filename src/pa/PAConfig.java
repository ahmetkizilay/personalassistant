package pa;

import java.util.Properties;

public class PAConfig {
	
	private String dbUrl;
	private String dbPort;
	private String dbName;
	private String dbUserName;
	private String dbPassword;
	
	public String getDBUrl(){
		return this.dbUrl;
	}
	
	public String getDBPort() {
		return this.dbPort;
	}
	
	public String getDBName() {
		return this.dbName;
	}
	
	public String getDBUserName() {
		return this.dbUserName;
	}
	
	public String getDBPassword() {
		return this.dbPassword;
	}
	
	public PAConfig() {
		initConfig();
	}
	
	private void initConfig() {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/pa/pa.properties"));
			
			if(properties.containsKey("DatabaseURL")) {
				this.dbUrl = (String) properties.get("DatabaseURL");
			}
			else {
				System.err.println("DatabaseUrl is not specified");
			}
			
			if(properties.containsKey("DatabasePort")) {
				this.dbPort = (String) properties.get("DatabasePort");
			}
			else {
				System.err.println("DatabasePort is not specified");
			}
			
			if(properties.containsKey("DatabaseName")) {
				this.dbName = (String) properties.get("DatabaseName");
			}
			else {
				System.err.println("DatabaseName is not specified");
			}
			
			if(properties.containsKey("DatabaseUserName")) {
				this.dbUserName = (String) properties.get("DatabaseUserName");
			}
			else {
				System.err.println("DatabaseUserName is not specified");
			}
			
			if(properties.containsKey("DatabasePassword")) {
				this.dbPassword = (String) properties.get("DatabasePassword");
			}
			else {
				System.err.println("DatabasePassword is not specified");
			}
			
			
			
		}
		catch(Exception exp) {
			
		}
		finally {
			
		}
	}
	
	
}
