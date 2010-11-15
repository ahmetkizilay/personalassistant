package pa.constants;

public enum PACommand {
	ADD (0, "Add"),
	SEARCH (1, "Search"),
	EDIT (2, "Edit"),
	DELETE (3, "Delete"),
	HELP (4, "Help"),
	LIST (5, "List"),
	UNDEFINED (6, "Undefined"),
	DEBUG (7, "Debug");
	
	private int code;
	private String name;
	
	PACommand(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public final int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
}
