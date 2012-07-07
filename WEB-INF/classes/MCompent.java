import freemarker.log.Logger;
import freemarker.template.*;
import java.io.*;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

class DeployFileLoaderSample {

	  /** dom4j object model representation of a xml document. Note: We use the interface(!) not its implementation */
	  public Document doc = null;

	  /**
	   * Loads a document from a file.
	   *
	   * @throw a org.dom4j.DocumentException occurs whenever the buildprocess fails.
	   */
	  public Document parseWithSAX(String string) throws DocumentException {
	    SAXReader xmlReader = new SAXReader();
        FileInputStream fs;
        try{
        	fs = new FileInputStream(string);
        	return xmlReader.read(fs);
        }
        catch (Exception e) {
			// TODO: handle exception
		}
        return this.doc;
	  }
}
public class MCompent
{
    static Logger logger = Logger.getLogger(MCompent.class.getName());
	static long i = 0;
	public String GetModel(String tempFileName, String xml)
	{
		i ++;
		String string = "";
		Document doc ;
		DeployFileLoaderSample foo = new DeployFileLoaderSample();
        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading (
            		new File("./template/"));
            cfg.setObjectWrapper (new DefaultObjectWrapper());
            cfg.setDefaultEncoding("ISO-8859-1");
            Template temp = cfg.getTemplate(tempFileName);
            Map map = new HashMap();
    		try{
    			doc = foo.parseWithSAX("./template/"+xml);
    			Element root = doc.getRootElement();
    			List list = root.elements();
    			for (Object obj:list)
    			{
    				Element element = (Element)obj;	
    				String str;
    				str = new String(element.getText().getBytes("UTF-8"),"ISO-8859-1");
    				map.put(element.getName(),str);
    			}
    			StringWriter out = new StringWriter();	
    			temp.process(map, out);
    			return out.toString();
    		}
    		catch (Exception e) {
    			return e.toString();
    		}      
            
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	public String GetModel(String tempFileName, Map map)
	{
		String string = "";
		Document doc ;
		DeployFileLoaderSample foo = new DeployFileLoaderSample();
        Configuration cfg = new Configuration();
        i ++;
        if (false == map.containsKey("model"))
        {
        	map.put ("model","m"+i+"_");
        }
        String templateDir = "";
        String fileName = "";
        int index = tempFileName.lastIndexOf("/");
        if (-1 == index)
        {
            templateDir = "./";
            fileName = tempFileName;
        }
        else
        {
            templateDir = tempFileName.substring(0, index);
            fileName = tempFileName.substring(index+1,tempFileName.length());            
        }
        logger.debug("template Dir:" + templateDir);
        logger.debug("FileName:" + fileName);
        
        
        try {
            cfg.setDirectoryForTemplateLoading (
            		new File(templateDir));
            cfg.setObjectWrapper (new DefaultObjectWrapper());
            cfg.setDefaultEncoding("ISO-8859-1");
            Template temp = cfg.getTemplate(fileName);
            
    		try{
    			StringWriter out = new StringWriter();
    			temp.process(map, out);
    			return out.toString();
    		}
    		catch (Exception e) {
    			return e.toString();
    		}      
            
		} catch (Exception e) {
			return e.toString();
		}
	}
}
