package sand.show
;
import sand.*;
import sand.tal.*;
import sand.message.*;
import sand.sys.*;
import sand.compent.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.*;

import com.taobao.api.domain.*;

public class MShowConfigItemManager extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "showconfigitemmanager";
    static Logger logger = Logger.getLogger(MShowConfigItemManager.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MShowConfigItemManager obj = new MShowConfigItemManager();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    
 	private List<Map> getItems(MDispatchParam param)
    {
        List<MItem> listMItems;
        String id = param.id;
        HttpServletRequest req = param.req;
        HttpServletResponse resp = param.resp;
        long totalNum = 0;
        String keyword = req.getParameter("keyword");
        if (keyword != null)
        {
            if (keyword.equals(""))keyword = null;
        }
        String cat = req.getParameter("cat");
        if (cat != null)
        {
            if (cat.equals("-1"))cat = null;
        }
        Long inshow = Long.valueOf(req.getParameter("inshow") != null?req.getParameter("inshow") : "-1");
        String sort = req.getParameter("sort");
        if (sort != null)
        {    
            if(true == sort.equals("ignore"))sort = null;
        }
        Long pageItems = Long.valueOf(req.getParameter("pitem") != null ?req.getParameter("pitem") : "40");
        Long jmptoPageNum = Long.valueOf(req.getParameter("jmpto") != null ?req.getParameter("jmpto") : "1");
        String action = req.getParameter("action");
        Long curPage = Long.valueOf(req.getParameter("curpage") != null ?req.getParameter("curpage") : "1");

        logger.debug("keyword:" + keyword);
        logger.debug("cat:" + cat);
        logger.debug("inshow:" + inshow);
        logger.debug("pageNum:" + jmptoPageNum);
        logger.debug("action:" + action);
        logger.debug("cur page:"+ curPage);
        
        Long itemNumber = new MTop_API().getItemsCnt(id);
        if (action == null)
        {
            keyword = null;
            cat = null;
            pageItems = 40L;
            jmptoPageNum  = 1L;
            inshow = -1L;
            sort = null;        
        }
        else if (action.equals("select_forever") || 
                 action.equals("select_never"))
        {
            keyword = null;
            cat = null;
            pageItems = 40L;
            jmptoPageNum  = 1L;
            inshow = -1L;
            sort = null;
        }
        else if (action.equals("tail"))
        {
            jmptoPageNum = (itemNumber + pageItems - 1)/pageItems;
        }
        else if (action.equals("head"))
        {
            jmptoPageNum = 1L;
        }
        else if (action.equals("next"))
        {
            jmptoPageNum = curPage + 1;
            if (jmptoPageNum > (itemNumber + pageItems - 1)/pageItems)
                jmptoPageNum = (itemNumber + pageItems - 1)/pageItems;            
        }
        else if (action.equals("jmpto"))
        {
            if (jmptoPageNum > (itemNumber + pageItems - 1)/pageItems)
                jmptoPageNum = (itemNumber + pageItems - 1)/pageItems;
            else if (jmptoPageNum <= 1)
                jmptoPageNum = 1L;
              
        }
        else if (action.equals("pre"))
        {
            curPage = curPage - 1 ;
            if (curPage <= 1)curPage = 1L;
            jmptoPageNum = curPage;
        }
        else if (action.equals("search"))
        {
            jmptoPageNum = 1L;
        }
        
        listMItems = new MTop_API().getItems(
                         id, 
                         keyword,   /*the key word for get, null if not set*/ 
                         -1L,        /*the catalog , if not need set -1, we use seller cid so set -1 forever*/ 
                         cat,       /*the seller catalog, if not need set -1, we use it*/
                         jmptoPageNum,  /*the page number, */
                         inshow,        /*has show case -1:ingore 0:not in 1:inshow*/
                         sort,          /*sort order: */
                         pageItems);    /*page size , if -1 equ 40*/

        if (listMItems == null)
        {
            logger.error("get the Items error");
            return null;
        }
        else
            logger.debug("get the Items Num:" + listMItems.size());
        listMItems = new MUserData().GetItemStatus(param.id, listMItems);
        
        List<Map> listMap = new ArrayList<Map>();
        int i = 0;
        String title;
        for (MItem item:listMItems)
        {
            Map mapItem = new HashMap();
            mapItem.put("num_iid", item.num_iid);
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
            
            if (item.status == 2)
                mapItem.put("item_status", "forbidShow");
            else if (item.status == 1)
                mapItem.put("item_status", "forceShow");
            else 
                mapItem.put("item_status", "normalShow");
            
            listMap.add(mapItem);
        }
        
        Map map = new HashMap();
        map.put ("page_items", pageItems.toString());
        Long totalPage = ((itemNumber + pageItems -1 )/pageItems);
        map.put ("total_page", totalPage.toString());
        map.put ("cur_page", jmptoPageNum.toString());
        logger.debug("itemPerPage:" + pageItems.toString());
        logger.debug("totalPageNumber:" + ((itemNumber + pageItems -1 )/pageItems));
        param.map = map;
        return listMap;
    }

    public String load(MDispatchParam param)
    {
        logger.debug("MShowConfigItemManager load:"+ param.id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();
        Displatch (param.id, param.list);
        String str = null;
        
        map.put("ip", new MBaseInfo().getServerAddr());
        
        List<Map> listMap;
        listMap = getItems(param);
        if (listMap != null)
        {
            map.put("items", listMap);
            map.put ("cur_page", param.map.get("cur_page"));
            map.put ("total_page", param.map.get("total_page"));
        }
         
        logger.debug("ip:" + new MBaseInfo().getServerAddr());
        return mc.GetModel("./template/sand/show/config_items.html", map);
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
        return true;
    }
}
