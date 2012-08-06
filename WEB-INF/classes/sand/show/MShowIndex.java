package sand.show;

import sand.*;
import sand.tal.*;
import sand.sys.*;
import sand.compent.*;
import sand.message.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;

public class MShowIndex extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "showindex";
    static Logger logger = Logger.getLogger(MShowIndex.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MShowIndex obj = new MShowIndex();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    public String load(MDispatchParam param)
    {
        String id = param.id;
        List list = param.list;
        logger.debug("MShowIndex load:"+ id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();

        Displatch (id, list);
        String str = null;
        
        map.put("ip", new MBaseInfo().getServerAddr());
  
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
        
        return mc.GetModel("./template/sand/show/index.html", map);
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
