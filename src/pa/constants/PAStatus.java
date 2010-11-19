package pa.constants;

public enum PAStatus
{
	OVERDUE (2, "Overdue"),
	ACTIVE (0, "Active"),
	COMPLETED (1, "Completed"),
	DELETED (3, "Deleted"),
	DISCARDED (5, "Discarded"),
	UNKNOWN (4, "Unknown");
	
	private final int code;
	private String name;
	PAStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static PAStatus parseCode(int code) {
		for(PAStatus p : PAStatus.values()) {
			if(p.getCode() == code) {
				return p;
			}
		}
		return UNKNOWN;
	}
	
	public static PAStatus parseName(String name) {
		for(PAStatus p : PAStatus.values()) {
			if(p.getName().equals(name)) {
				return p;
			}
		}
		return UNKNOWN;
	}
}
