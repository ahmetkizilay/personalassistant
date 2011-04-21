package pa;

import pa.commands.PATaskFactory;

/***
 * This is the main entry point for the ToDoList program.<br />
 * The aim of this application is to manage a console based todo list.
 * 
 * @author ahmetkizilay
 *
 */
public class ToDoList {

	public static void main(String[] args) throws Exception{
		PATaskFactory.newInstance(args);
	}

}
