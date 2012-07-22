import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;

public class MShowStatus extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "showstatus";
    static Logger logger = Logger.getLogger(MShowStatus.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MShowStatus obj = new MShowStatus();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    public String load(MDispatchParam param)
    {
        String id = param.id;
        List list = param.list;
        logger.debug("MShowStatus load:"+ id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();
        Displatch (param.id, param.list);
        String str = null;
        if (new MUserData().GetShowCaseRunStatus(id) == true)
             str = "run";
        else
            str = "stop";
        map.put("ip", new MBaseInfo().getServerAddr());
        map.put("run_stop", str);

        MUserManager manager = new MUserManager();
        logger.debug("Start get shop Info");
        MUser usr = new MTop_API().getUserShowNum(id);
        
        if (usr != null && usr.showCaseVaild == true)
        {
            logger.debug("Get Shop Info success");
            map.put("show_case_num", usr.showCaseNum);
            map.put("show_case_used", usr.showCaseUsed);
            map.put("show_case_remained", usr.showCaseRemained);
        }
        else
        {
            logger.debug("Get Shop Info error");
            map.put("show_case_num", 0);
            map.put("show_case_used", 0);
            map.put("show_case_remained", 0);            
        }
        List<MItem> listMItems;
        long totalNum = 0;
        listMItems = new MTop_API().getItems(id, null, -1L, null, 0L, 1L, 
                                             "delist_time", 200L);
        if (listMItems != null && listMItems.size() != 0)
        {
            totalNum = listMItems.get(0).total_num;
            logger.debug("list Item Total:" + totalNum);
            Map[] map2 = new HashMap[(int)totalNum];
            Map map3;
            String title ;
            for (int i = 0; i < totalNum; i ++)
            {
                map3 = new HashMap();
                map3.put("pic_url", listMItems.get(i).pic_url);
                try {
                    title = listMItems.get(i).title;
                    title = new String(title.getBytes("UTF-8"),"ISO-8859-1");
                } catch (Exception e) {
                    title = "nothing";
                }
             
                map3.put("title", title);
                map2[i] = map3;
            }
            map.put("items", map2);
        }

        return mc.GetModel("./template/sand/show/status.html", map);
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