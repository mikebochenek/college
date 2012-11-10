/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

public class CommandType 
{
	public final static int LS_COMMAND = 1001;
	public final static int COUNT_COMMAND = 1002;
	public final static int GET_COMMAND = 1003;
	public final static int INVALID_COMMAND = 1004;
	public final static int QUIT_COMMAND = 1005;


	public static int parseCommand (String command)
	{
		if (command.equals ("ls"))
		{
			return LS_COMMAND;
		}
		else if (command.equals ("count"))
		{
			return COUNT_COMMAND;
		}
		else if (command.equals ("quit"))
		{
			return QUIT_COMMAND;
		}
		else if (command.startsWith ("get "))
		{
			return GET_COMMAND;
		}
		else
		{
			return INVALID_COMMAND;
		}
	}

}

