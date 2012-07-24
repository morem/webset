import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.*;

import com.taobao.api.domain.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;


public class MPublicInfo extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "public";
    static Logger logger = Logger.getLogger(MPublicInfo.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MPublicInfo obj = new MPublicInfo();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    
    private String getAllCatsDom(String id)
    {
        List list = new ArrayList();
        List<SellerCat> sellerCats = new MTop_API().GetTheUserCats(id);
        if (sellerCats == null)
        {
            logger.error("Get the error Catas:" + id);
            return null;
        }
        
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("ul");
        String title;
        for (SellerCat sellerCat:sellerCats)
        {
            Long cid;
            cid = sellerCat.getCid();
            logger.debug("Get Title:"+sellerCat.getName() +" cid:" + cid + "Pcid:" + sellerCat.getParentCid());
            if (sellerCat.getParentCid().equals(0L))
            {

                Element elementCat1, elementCatUL, elementCat2;
                elementCat1 = rootElement.addElement("li");
                elementCat1.addAttribute("value", cid.toString());

                logger.debug("This is a Parater:"+sellerCat.getName() +" cid:" + cid);
                try{
                    title = new String (sellerCat.getName().getBytes("UTF-8"), "ISO-8859-1");
                }catch(Exception e)
                {
                    logger.error("Create Title Error:" + e);
                    title = "not define";
                }
                elementCat1.setText(title);             

                Boolean needRemove = true;
                elementCatUL = elementCat1.addElement("ul");
                for (SellerCat sellerCat2:sellerCats)
                {
                    logger.debug("cid:" + cid + "pcid:" + sellerCat2.getParentCid());
                    if (cid.equals(sellerCat2.getParentCid()))
                    {
                        needRemove = false;
                        elementCat2 = elementCatUL.addElement("li");
                        logger.debug("This is a Child:"+sellerCat.getName() +" cid:" + sellerCat2.getParentCid());
                        elementCat2.addAttribute("value", sellerCat2.getCid().toString());
                        try{
                            title = new String (sellerCat2.getName().getBytes("UTF-8"), "ISO-8859-1");
                        }catch(Exception e)
                        {
                            title = "not define";
                            logger.error("Create Title Error:" + e);
                        }
                        elementCat2.setText(title);
                    }
                }
                if (needRemove.equals(true))elementCat1.remove(elementCatUL);

                
            }        
        }
        Writer write = new StringWriter();
        XMLWriter output = new XMLWriter(write);
        try{
            output.write(document);
            output.close();
        }catch(Exception e){
            return null;
        }
        logger.debug(write.toString());
        return write.toString();
    }

    private Map getAllCatsTemplate(String id)
    {
        List<SellerCat> sellerCats = new MTop_API().GetTheUserCats(id);
        if (sellerCats == null)
        {
            logger.error("Get the error Catas:" + id);
            return null;
        }
        Map mapRoot = new HashMap();
        String title;
        List<Map> listFather= new ArrayList();
        List<String> cidList = new MUserData().GetCatsListShow(id);
        logger.debug ("cid List" + cidList);
        
        for (SellerCat sellerCat:sellerCats)
        {
            Long cid;
            cid = sellerCat.getCid();
            logger.debug("Get Title:"+sellerCat.getName() +" cid:" + cid + "Pcid:" + sellerCat.getParentCid());
            if (sellerCat.getParentCid().equals(0L))
            {
                Map mapFather = new HashMap();

                logger.debug("This is a Parater:"+sellerCat.getName() +" cid:" + cid);
                try{
                    title = new String (sellerCat.getName().getBytes("UTF-8"), "ISO-8859-1");
                }catch(Exception e)
                {
                    logger.error("Create Title Error:" + e);
                    title = "not define";
                }
                mapFather.put("title", title);
                mapFather.put("cid", cid.toString());
                for (String str:cidList)
                {
                    if (str.equals(cid.toString()))
                    {
                        mapFather.put("status", "selected");
                    }
                }
                Boolean needAddSon = false;
                List<Map> listSon = new ArrayList();
                for (SellerCat sellerCat2:sellerCats)
                {
                    //logger.debug("cid:" + cid + "pcid:" + sellerCat2.getParentCid());
                    if (cid.equals(sellerCat2.getParentCid()))
                    {
                        Map mapSon = new HashMap();
                        needAddSon = true;
                        logger.debug("This is a Child:"+sellerCat.getName() +" cid:" + sellerCat2.getParentCid());
                        try{
                            title = new String (sellerCat2.getName().getBytes("UTF-8"), "ISO-8859-1");
                        }catch(Exception e)
                        {
                            title = "not define";
                            logger.error("Create Title Error:" + e);
                        }
                        mapSon.put("title", title);
                        mapSon.put("cid", sellerCat2.getCid().toString());
                        for (String str:cidList)
                        {
                            if (str.equals(cid.toString())){
                                mapFather.put("status", "selected");
                            }
                        }
                        listSon.add(mapSon);
                    }
                }
                if (needAddSon.equals(true))mapFather.put("sons", listSon);
                listFather.add(mapFather);
            } 
        }
        
        mapRoot.put ("cats", listFather);
        return mapRoot;
    }


    public String load(MDispatchParam param)
    {
        String action = param.req.getParameter("action");
        if (action == null)
        {
            logger.error("not correct action");
            return "Fail";
        }
        logger.debug("MPublicInfo load:"+ param.id + "action:" + action);
        if (action.equals("getcats_dom"))
        {
            logger.debug("get cats data dom");
            return getAllCatsDom(param.id);
        }
        else if (action.equals("getcats_bytemp"))
        {   
            MCompent mc = new MCompent();
            logger.debug("get cats data by temp");
            String model = param.req.getParameter("template");
            Map map = getAllCatsTemplate(param.id);
            return mc.GetModel(model, map);
        }
       
        
        
        
        return "Fail";
    }
    
    public String getname()
    {
        return className;
    }
    
    public boolean Displatch( String id , List list)
    {
        return true;
    }
    
    static void updateAllProduction(String usr)
    {
        
    }
}

