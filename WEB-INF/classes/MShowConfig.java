import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.*;

import com.taobao.api.domain.*;

public class MShowConfig extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "showconfig";
    static Logger logger = Logger.getLogger(MShowConfig.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MShowConfig obj = new MShowConfig();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    
    private List getAllCats(String id)
    {
        List list = new ArrayList();
        if (id.equals("Guest"))return null;
        List<SellerCat> sellerCats = new MTop_API().GetTheUserCats(id);
        if (sellerCats == null)
        {
            logger.error("Get the error Catas:" + id);
            return null;
        }
        
        for (Object obj:sellerCats)
        {
            SellerCat sellerCat;
            String name;
            sellerCat = (SellerCat)obj;
        //    if (shopCat.getParentCid() == 0)
        //    {
                Map map= new HashMap();
                map.put("cid", sellerCat.getCid().toString());
                map.put("parent_cid", sellerCat.getParentCid().toString());
                try{
                    name = new String (sellerCat.getName().getBytes("UTF-8"), "ISO-8859-1");
                    map.put("name", name);
                    map.put("is_parent", sellerCat.getParentCid());                
                    list.add(map);
                }
                catch (Exception e) {
                    logger.error("String getBytes error");
                }

         //   }           
        }
        return list;
    }
    
    private List<Map> getItems(MDispatchParam param)
    {
        List<MItem> listMItems;
        String id = param.id;
        HttpServletRequest req = param.req;
        HttpServletResponse resp = param.resp;
        long totalNum = 0;
        if (null == req.getParameter("inshow"))
        {    
            listMItems = new MTop_API().getItems(id, null, -1L, null, -1L, -1L, 
                                                 null, -1L);
            if (listMItems == null)
            {
                logger.error("get the Items error");
                return null;
            }
            else
                logger.debug("get the Items Num:" + listMItems.size());
            
            List<Map> listMap = new ArrayList<Map>();
            int i = 0;
            String title;
            for (MItem item:listMItems)
            {
                Map mapItem = new HashMap();
                mapItem.put("pic_url", item.pic_url);
                try {
                    title = new String (item.title.getBytes("UTF-8"), "ISO-8859-1");                   
                } catch (Exception e) {
                    title = "can't get the title";
                }
                mapItem.put("title", title);
                if (item.hasShowcase == true)
                    mapItem.put("status", "true");
                else
                    mapItem.put("status", "false");
                listMap.add(mapItem);
            }  
            logger.debug("here");
            return listMap;
        }
        return null;
    }
    
    public String load(MDispatchParam param)
    {
        logger.debug("MShowConfig load:"+ param.id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();
        MTop_API top = new MTop_API();
        Displatch (param.id, param.list);
        String str = null;
        
        map.put("ip", new MBaseInfo().getServerAddr());
        
        List list = getAllCats(param.id);
        if (list != null)
            map.put("cats", list);

        map.put ("mode", new MUserData().GetShowCaseMode(param.id));

        logger.debug("ip:" + new MBaseInfo().getServerAddr());
        return mc.GetModel("./template/sand/show/config.html", map);
    }
    
    public String getname()
    {
        return className;
    }
    
    public boolean Displatch( String id , List list)
    {
        logger.debug("Enter");
        if (list.size() <= 1)return true;
        String action = (String)list.get(1);
        if (action.equals("update"))
        {
            logger.debug("Get all Production");
            updateAllProduction(id);
        }
        else if (action.equals("run"))
        {
            new MUserData().SetShowCaseRunStatus(id, true);
        }
        else if (action.equals("stop"))
        {
            new MUserData().SetShowCaseRunStatus(id, false);
        }
        return true;
    }
    
    static void updateAllProduction(String usr)
    {
        
    }
}