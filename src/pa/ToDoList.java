package pa;

import pa.commands.PATask;
import pa.commands.PATaskFactory;

public class ToDoList {

	private PATask task;
	
	public ToDoList(String[] args) throws Exception{
		task = getTask(args);
	}

	private PATask getTask(String[] args) throws Exception{
		return PATaskFactory.newInstance(args);
	}

	private void execute(){		
		task.execute();
	}

	public static void main(String[] args) throws Exception{
		ToDoList todoList = new ToDoList(args);
		todoList.execute();
	}

}
