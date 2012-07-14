import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.*;

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
    
    public String load(MDispatchParam param)
    {
        logger.debug("MShowConfig load:"+ param.id);
        MCompent mc = new MCompent();
        HashMap map = new HashMap();
        Displatch (param.id, param.list);
        String str = null;
        
        map.put("ip", new MBaseInfo().getServerAddr());
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