package pa.constants;

public enum PASortBy
{
	STARTDATE (0, "startdate"),
	DUEDATE (1, "duedate"),
	PRIORITY (2, "priority"),
	LASTEDITED (3, "lastedited"),
	UNKNOWN(4, "Unknown");
	
	private int code;
	private String name;
	
	PASortBy(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static PASortBy parseName(String name) {
		for(PASortBy p : PASortBy.values()) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return UNKNOWN;
	}
}
