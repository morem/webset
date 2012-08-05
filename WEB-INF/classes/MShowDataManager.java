import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;

public class MShowDataManager extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "showdatamanager";
    static Logger logger = Logger.getLogger(MShowDataManager.class.getName());
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        MShowDataManager obj = new MShowDataManager();
        MDispatch p = new MDispatch();
        p.RegistObject (obj);        
    }

    private String saveMode (MDispatchParam param)
    {
        String mode = param.req.getParameter("mode");
        if (mode == null)mode = "all";
        logger.info ("the mode is " + mode);
        new MUserData().SetShowCaseMode (param.id, mode);
        return "success";
    }  
    
    public String load(MDispatchParam param)
    {
        logger.debug("MShowDataManager load:"+ param.id);
        String action = param.req.getParameter("action");
        if (action == null)
        {
            logger.error("get action error");
            return "error";
        }

        if (action.equals("savemode"))
        {
            logger.info ("the action is save mode");
            return saveMode (param);
        }
        
        String id = param.req.getParameter("id");
        if (id == null)
        {
            logger.error("no id param");
            return "error";
        }
        String idList[] = id.split(":");
        List<String> list = new ArrayList<String>();

        for (String item : idList) {
            if (item.length() == 0)continue;
            list.add(item);
            logger.debug("item:" + item);
        }
        
        if (action.equals("forceShow"))
        {
            logger.debug("forceShow");
            new MUserData().SetForceShow(param.id, list);
            return "success_force"; 
        }
        else if (action.equals("forbidShow"))
        {
            logger.debug("forbidShow");
            new MUserData().SetForbidShow(param.id, list); 
            return "success_forbid"; 
        }
        else if (action.equals("normalShow"))
        {
            logger.debug("normalShow");
            new MUserData().SetNormalShow(param.id, list);
            return "success_normal"; 
        }
        else if (action.equals("catsShow"))
        {
            logger.debug("catsShow");
            new MUserData().SetCatsShow(param.id, list);
            return "catsShow"; 
        }
        else
        {
            logger.error("found a vaild req" + action);
            return "error";
        }      
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
