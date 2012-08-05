import freemarker.template.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import net.sf.json.*;

//http://my.open.taobao.com/auth/authorize.htm?appkey=21040246
//http://container.api.taobao.com/container?appkey=21040246
public class Login extends HttpServlet
{
    static Logger logger = Logger.getLogger(Login.class.getName());
    
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        MCompent mc = new MCompent();  
        PrintWriter out = resp.getWriter();
        MTop_Screct top = new MTop_Screct();
        if (top.TopCallbackCheck(req)!= true){
            logger.error("The Callback Address is Error");
            return ;
        }
        
        HttpSession httpSession = req.getSession();
        out.println("It's a New Session:"+ "New");
        Map map = top.convertBase64StringtoMap(req, null); 
        String visitor_id = (String)map.get("visitor_id");
        String visitor_nick = (String)map.get("visitor_nick");
        logger.debug("visitor_nick:" + visitor_nick);
        httpSession.setAttribute("visitor_id", visitor_id);
        MUserData userXMLData = new MUserData();
        userXMLData.CreateUser(visitor_id, visitor_nick,(String)map.get("refresh_token"));

        logger.debug (map.toString());


        new MTop_API().UserNotifyPermit (visitor_id);

        
        resp.sendRedirect("/index.html?ct=1");
    }
}
