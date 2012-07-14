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
    
    public String Dispatch(String path, MDispatchParam param)
    {
        List list = new ArrayList();
        int head;
        int end;
        String element;
        //解板路径，放入到List中去
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
        //list[0]中是分页控制器
        if (list.size() == 0)path = "showindex";
        else path = (String)list.get(0);
        //查找控制器回调
        if (null != map.get(path))
        {
            param.list = list;
            logger.debug("Dispatch " + path + " " + map.get(path));
            MDispatchCallback obj = (MDispatchCallback)map.get(path);
            return obj.load(param);
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