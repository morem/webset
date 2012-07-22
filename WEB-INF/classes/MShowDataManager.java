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

    public void AddForceShowList(String idList[])
    {
        
        
    }
    public void AddForbidShowList(String idList[])
    {
                
    }
    
    public void AddNormalShowList(String idList[])
    {
                
    }
    
    public String load(MDispatchParam param)
    {
        logger.debug("MShowDataManager load:"+ param.id);
        String action = param.req.getParameter("action");
        if (action == null)return null;
        String id = param.req.getParameter("id");
        if (id == null)return null;
        String idList[] = id.split(":");
        if (action.equals("forceShow"))
        {
            AddForceShowList (idList);
        }
        else if (action.equals("forbidShow"))
        {
            AddForbidShowList (idList);
        }
        else if (action.equals("normalShow"))
        {
            AddNormalShowList (idList);
        }       
        return null;       
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