import freemarker.log.Logger;
import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

import java.lang.*;
import java.io.*;
import java.util.*;

public class MDispatch
{
    static Document doc = null;
    static HashMap  map = new HashMap();
    static Logger logger = Logger.getLogger(MDispatch.class.getName());
    
    public void Init()
    {
        if (doc != null)return;
        
        SAXReader xmlReader = new SAXReader();
        FileInputStream fs;
        try{
            fs = new FileInputStream("./WEB-INF/page.xml");
            doc =  xmlReader.read(fs);
        }
        catch (Exception e) {
                // TODO: handle exception
            logger.error("Open the File error");
            return;
        }
        
        Element root = doc.getRootElement();
        List list = root.elements();
        for (Object obj:list)
        {
            Element elt = (Element)obj;
            map.put(elt.getName(), elt.getText());
            logger.debug("Read Displatch Info,Name:" + elt.getName() + " Value:" + elt.getText());
        }        
    }
    
    public void Dispatch(String path)
    {
        if (null != map.get(path))
        {            
            
        }
        else
        {
            
            
        }
    }

}