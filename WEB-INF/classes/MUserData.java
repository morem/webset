import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

class MUserData extends Object{
    static Logger logger = Logger.getLogger(MUserData.class.getName());
    private boolean CreateAndInitFile(String id, String token)
    {
        File file = new File(new MBaseInfo().dateBase()+ id + ".xml");
        try{
            file.createNewFile();
        }catch (Exception e) {
            logger.error("Can't Create Data Base File User:" + id + " " + token + " " + e);
            return false;
        }
        
        Document document = DocumentHelper.createDocument();
        Element userElement = document.addElement("user");
        userElement.addComment("User ID" + id + " Config");
        Element idElement = userElement.addElement("id");
        idElement.setText(id);
        Element tokenElement = userElement.addElement("token");
        tokenElement.setText(token);
        Element showcaseElement = userElement.addElement("showcase");
        Element showcaseRunStatusElement = showcaseElement.addElement("runstatus");
        showcaseRunStatusElement.setText("stop");
        
        try {
            XMLWriter output = new XMLWriter(new FileWriter(file));
            output.write(document);
            output.close();
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
        }
         return true;
    }
    
    private boolean UpdateAExistFile(String id, String token)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            Element root = document.getRootElement();
            List list = root.selectNodes("token");
            for (Object obj:list)
            {
                Element elt = (Element)obj;
                if ( true == elt.getName().equals("token"))
                {
                    elt.setText(token);
                }                
            }            
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        
        try {
            XMLWriter output = new XMLWriter(new FileWriter(new File(new MBaseInfo().dateBase() + id + ".xml")));
            output.write(document);
            output.close();
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        
        return true;
    }
    
    private boolean UpdateUserData(String id, String path, String value)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            Node node = document.selectSingleNode(path);
            if (node == null)return false;
            node.setText(value);                    
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        
        try {
            XMLWriter output = new XMLWriter(new FileWriter(new File(new MBaseInfo().dateBase() + id + ".xml")));
            output.write(document);
            output.close();
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        return true;
    }

    private String GetUserData(String id, String path)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            Node node = document.selectSingleNode(path);
            if (node == null)return null;
            return node.getText();
        } catch (Exception e) {
            logger.error("Read User Data Error" + e);
            return null;
        }        
    }
    
    public void CreateUser(String id, String token) {

        File f1 = new File(new MBaseInfo().dateBase()+ id + ".xml");
        if (f1.exists() == true)
            UpdateUserData(id, "/user/token", token);
        else
            CreateAndInitFile(id, token);
    }
    
    public String GetUserToken(String id)
    {
        return GetUserData(id, "/user/token");
    }
    
    public boolean SetShowCaseRunStatus(String id, boolean run)
    {
        String value = "run";
        if (run == false)value = "stop";
        return UpdateUserData(id, "/user/showcase/runstatus", value);
    }
    
    public boolean GetShowCaseRunStatus(String id)
    {
        String status = GetUserData(id, "/user/showcase/runstatus");
        if (status == null)return false;
        if (status.equals("run"))return true;
        return false;
    }
    
}