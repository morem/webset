import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.lang.*;
import java.io.*;
import java.util.*;

import org.apache.log4j.*;

public class MDispatch extends HttpServlet
{
    static Document doc = null;
    static HashMap  map = new HashMap();
    static Logger logger = Logger.getLogger(MDispatch.class.getName());
    
    public void init()
    {
    }
    
    public String load(String arg0, String arg1)
    {
        return null;
    }
    
    public String Dispatch(String path, String usr)
    {
        ArrayList list = new ArrayList();
        int head;
        int end;
        String element;
        
        while(true)
        {
           head = path.indexOf("/");
           if (head < 0)break;
           path = path.substring(head + 1);
           end = path.indexOf("/");
           if (end < 0)
           {
               logger.debug("Found a element:" + path);
               list.add(path);
               break;
           }
           else
           {
               element = path.substring(0 , end);
               logger.debug("Found a element:" + element);
               list.add(element);
               path = path.substring(end);
           }           
        }
        if (list.size() == 0)path = "#";
        else path = (String)list.get(0);
        if (null != map.get(path))
        {            
            logger.debug("Dispatch " + path + " " + map.get(path));
            MDispatchCallback obj = (MDispatchCallback)map.get(path);
            return obj.load(usr, list);
        }
        else
        {
            logger.error("Go to the introdection Page:" + path);
            return null;
        }
    }
    
    public void RegistObject (MDispatchCallback obj)
    {
        init();
        map.put((Object)obj.getname(), (Object)obj);      
        logger.debug("RegistObject:" + obj.getname());
    }
 }