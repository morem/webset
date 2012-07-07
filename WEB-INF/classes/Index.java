import freemarker.log.Logger;
import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;
import java.lang.*;
import java.io.*;
import java.util.*;

import java.net.URL;

import com.taobao.api.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.response.*;

import freemarker.log.Logger;

public class Index extends HttpServlet
{
    static Logger logger = Logger.getLogger(Index.class.getName());
    
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    { 
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        String visitor_id = (String)httpSession.getAttribute("visitor_id");
        if (visitor_id == null)
        {
            visitor_id = "Guest";
            httpSession.invalidate();
        }
        
        String control = req.getParameter("c");
        if (null == control)control="introduction";
        
        logger.debug("Control=" + control);
        
	    Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading (
        		new File("./template/sand"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        cfg.setDefaultEncoding("ISO-8859-1");
  
        Template temp = cfg.getTemplate("index.html");

        Map root = new HashMap();
        String str = "欢迎"+ visitor_id;
        str = new String(str.getBytes("UTF-8"),"ISO-8859-1");
        root.put("title",str); 
        MCompent_Pane c = new MCompent_Pane();
        root.put("compent1", c.BuildAPane("index.xml"));
        
        logger.debug("getRequestURL():" + req.getRequestURL());
        
        MUserManager m = new MUserManager();
        MUser user = m.GetUserByID(visitor_id);
        if (user == null)
        {
            logger.error("Can't find id:"+visitor_id);
            root.put("head_img", " ");
        }
        else
            root.put("head_img", user.avatar);
       
        try {
            temp.process(root, out);		
            out.flush();
        } catch (Exception e) {
			// TODO: handle exception
        }
    }
}
