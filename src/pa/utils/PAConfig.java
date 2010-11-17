package pa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PAConfig {
	
	private String configFile;
	
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
	
	public PAConfig(String configFile) {
		this.configFile = configFile;
		initConfig();
	}
	
	public PAConfig() {
		this(null);
	}
	
	private void initConfig() {
		try {
			Properties properties = new Properties();
			if(configFile == null) {
				properties.load(this.getClass().getResourceAsStream("/pa/pa.properties"));
			}
			else {
				properties.load(new FileInputStream(new File(configFile)));
			}
			
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
			PAPrinter paPrinter = new PAPrinter(false);
			paPrinter.printErrorMessage("Error On Config: " + exp.getMessage());
			exp.printStackTrace();
		}
		finally {
			
		}
	}
	
	
}
