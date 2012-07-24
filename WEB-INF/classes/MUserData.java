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
    private boolean CreateAndInitFile(String id, String nick, String token)
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
        Element nickElement = userElement.addElement("nick");
        nickElement.setText(nick);
        Element tokenElement = userElement.addElement("token");
        tokenElement.setText(token);
        Element itemsElement = userElement.addElement("items");
        Element itemsTotalNumElement = itemsElement.addElement("total_num");
        Element itemsTotalNumUpdateTime = itemsElement.addElement("update_time");
        Element showcaseElement = userElement.addElement("showcase");
        Element showcaseRunStatusElement = showcaseElement.addElement("runstatus");
        showcaseRunStatusElement.setText("stop");
        Element showcaseRunModeElement = showcaseElement.addElement("runstatus");
        showcaseRunModeElement.setText("all");
        Element itemAdjuestMust = showcaseElement.addElement("forceShow");
        Element itemAdjuestNever = showcaseElement.addElement("forbidShow");
        Element catsAdjuest = showcaseElement.addElement("catsShow");
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
    
    private boolean UpdateUserDataList (String id, String path, String item, List<String> value)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        Element element;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            element = (Element)document.selectSingleNode(path);
            if (element == null)
            {
                logger.error("Can't found the element:" + path + "/" + item);
                return false;                 
            }
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        
        for (String str:value)
        {
            Integer i;
            Boolean add;
            add = true;
            List<Node> nodeList = null;
            nodeList = element.selectNodes(item);
            logger.debug("check:" + str);
            if (nodeList != null && nodeList.size() != 0)
            {
                for (i = 0; i < nodeList.size(); i ++)
                {
                    String nodeString = nodeList.get(i).getText();
                    if (nodeString == null || nodeString.length() == 0)continue;
                    if(true == nodeString.equals(str))
                    {
                        add = false;
                        logger.debug("found the same" + str);
                        break;
                    }
                }      
            }
            
            if (add == true)
            {
                logger.debug("add a new str:"+str);
                Element elt = element.addElement(item);
                elt.setText(str);                
            }
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

    private boolean RemoveUserDataList (String id, String path, List<String> value)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        List<Node> list = null;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            list = document.selectNodes(path);
            if (list == null)return false;                 
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        for (String str:value)
        {
            int i;
            logger.debug("search the path have node, text:" + str);
            for (i = 0; i < list.size(); i ++)
            {
                Node node = (Node)list.get(i);
                String nodeString = node.getText();
                if (nodeString == null)continue;
                if(true == nodeString.equals(str))
                {
                    logger.debug("remove id:" + nodeString);
                    Element elmt = node.getParent();
                    elmt.remove(node);
                }               
            }
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

    private boolean EmptyUserDataList (String id, String path)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        List<Node> list = null;
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            list = document.selectNodes(path);
            if (list == null)return false;                 
        } catch (Exception e) {
            logger.error("Write XML File Error " + e);
            return false;
        }
        
        for (Integer i = 0; i < list.size(); i ++)
        {
            Node node = (Node)list.get(i);
            Element elmt = node.getParent();
            elmt.remove(node);
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

    
    private List<String> GetUserDataList(String id, String path)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        List<String> strList = new ArrayList<String>();
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            List<Node> nodeList = document.selectNodes(path);
            if (nodeList == null)return null;
            for (Node node:nodeList)
                strList.add(node.getText());
            return strList;
        } catch (Exception e) {
            logger.error("Read User Data Error" + e);
            return null;
        }
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
    
    public void CreateUser(String id,String nick, String token) {

        File f1 = new File(new MBaseInfo().dateBase()+ id + ".xml");
        if (f1.exists() == true)
        {
            UpdateUserData(id, "/user/token", token);
            UpdateUserData(id, "/user/nick", nick);
        }
        else
            CreateAndInitFile(id, nick, token);
    }
    
    public String GetUserToken(String id)
    {
        return GetUserData(id, "/user/token");
    }
    
    public String GetUserNick(String id)
    {
        return GetUserData(id, "/user/nick");
    }
    
    public boolean SetItemsTotalNum(String id, Long num)
    {
        return UpdateUserData(id, "/user/items/total_num", num.toString());
    }
    
    public Long GetItemsTotalNum(String id)
    {
        String num =  GetUserData(id, "/user/items/total_num");
        return Long.valueOf(num);
    }
    
    public boolean SetItemTotalNumUpdateTime (String id)
    {
        Long ms = System.currentTimeMillis();
        return UpdateUserData(id, "/user/items/update_time", ms.toString());
    }
    
    public Long getItemTotalNumUpdateTime (String id)
    {
        String ms = GetUserData(id, "/user/items/update_time");
        if (ms == null)return 0L;
        try {
            Long timeLong =  Long.valueOf(ms);      
            return timeLong;
        } catch (Exception e) {
            return 0L;
        }
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
    public boolean SetForceShow(String id, List<String> list)
    {
        if (list.isEmpty())
        {
            logger.error("Found a empty list, so no action");
            return true;
        }
        UpdateUserDataList (id, "/user/showcase/forceShow", "id", list);
        RemoveUserDataList (id, "/user/showcase/forbidShow/id", list);
        return true;
    }
    
    public boolean SetForbidShow(String id, List<String> list)
    {
        if (list.isEmpty())
        {
            logger.error("Found a empty list, so no action");
            return true;
        }
        UpdateUserDataList (id, "/user/showcase/forbidShow", "id", list);
        RemoveUserDataList (id, "/user/showcase/forceShow/id", list);
        return true;
    }

    public boolean SetNormalShow(String id, List<String> list)
    {
        if (list.isEmpty())
        {
            logger.error("Found a empty list, so no action");
            return true;
        }
        RemoveUserDataList (id, "/user/showcase/forbidShow/id", list);
        RemoveUserDataList (id, "/user/showcase/forceShow/id", list);
        return true;
    }
    
    public boolean SetCatsShow(String id, List<String> list)
    {
        EmptyUserDataList (id, "/user/showcase/catsShow/cid");
        if (false == list.isEmpty())
        {
            UpdateUserDataList (id, "/user/showcase/catsShow", "cid", list);
        }
        return true;
    }
    
    public List<MItem> GetItemStatus(String id, List<MItem> listItem)
    {
        SAXReader xmlReader = new SAXReader();
        Document document = null;
        List<Node> forceShowList,forbidShowList;
        List<MItem> list = new ArrayList<MItem>();
        try {
            document = xmlReader.read(new File(new MBaseInfo().dateBase() + id + ".xml"));
            forceShowList = document.selectNodes("/user/showcase/forceShow/id");
            forbidShowList = document.selectNodes("/user/showcase/forbidShow/id");
        } catch (Exception e) {
            logger.error("Read User Data Error" + e);
            return null;
        }
        
        Integer totalItem = listItem.size();
        MItem item;
        Boolean addBoolean;
        for (Integer i = 0 ; i < totalItem; i ++){
            item = listItem.get(i);
            addBoolean = false;
            item.status = 0;
            for (Node node : forceShowList) {
                if (item.num_iid.equals(node.getText())){
                    item.status = 1;
                    break;
                }
            }
            if (addBoolean == false)
            {
                for (Node node: forbidShowList){
                    if (item.num_iid.equals(node.getText())){
                        item.status = 2;
                        break;
                    }
                }
            }
            list.add(item);
        }       
        return list;
    }

    public List<String> GetCatsListShow(String id)
    {
        return GetUserDataList(id, "/user/showcase/catsShow/cid");
    }
}
