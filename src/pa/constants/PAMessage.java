package pa.constants;

public enum PAMessage {
	ERROR (0, "Error"),
	INFO (1, "Info"),
	DEBUG (2, "Debug");
	
	
	private final int code;
	private String name;
	PAMessage(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
}
