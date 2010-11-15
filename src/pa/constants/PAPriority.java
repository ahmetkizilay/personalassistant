package pa.constants;

public enum PAPriority {
	LOW (0, "Low"),
	MEDIUM (1, "Medium"),
	HIGH (2, "High"),
	UNKNOWN (3, "Unknown");
	
	private int code;
	private String name;
	
	PAPriority(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static PAPriority parseName(String name) {
		for(PAPriority p : PAPriority.values()) {
			if(p.getName().equals(name)) {
				return p;
			}
		}
		return UNKNOWN;
	}
}
