import freemarker.template.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;


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
        //if (true == httpSession.isNew())
        {
            out.println("It's a New Session:"+ "New");
            Map map = top.convertBase64StringtoMap(req, "utf-8"); 
            MUserManager manager = new MUserManager();
            String visitor_id = (String)map.get("visitor_id");
            MUser usr = manager.GetUserByID ( visitor_id,
                                              (String)map.get("refresh_token"), true);
            httpSession.setAttribute("visitor_id", visitor_id);
            MUserData userXMLData = new MUserData();
            userXMLData.CreateUser(visitor_id, (String)map.get("refresh_token"));
        }
        resp.sendRedirect("/index.html");
    }
}
