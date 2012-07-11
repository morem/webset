import java.util.HashMap;
import java.util.ArrayList;
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
        logger.debug("Init");
        if (bInit == true)return;
        bInit = true;
        MShowStatus obj = new MShowStatus();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }
    public String load(String id, ArrayList list)
    {
        logger.debug("MShowStatus load:"+ id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();
        Displatch (id, list);
        String str = null;
        if (new MUserData().GetShowCaseRunStatus(id) == true)
             str = "run";
        else
            str = "stop";

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
        
        return mc.GetModel("./template/sand/show/status.html", map);
    }
    
    public String getname()
    {
        return className;
    }
    
    public boolean Displatch( String id , ArrayList list)
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