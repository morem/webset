import java.io.*;
import java.util.ArrayList;

interface ProcessCallback
{
	public int process(String str);
}

class path_process extends Object
{
	String path;
	ProcessCallback cb;
};

public class MCore
{
	static ArrayList al = new ArrayList();
	
	public int Register (String path, ProcessCallback c)
	{	
		path_process pp = new path_process();
		pp.path = path;
		pp.cb = c;
		
		if (true == al.contains(pp))return -1;
		al.add(pp);		
		return 0;
	}	
}
