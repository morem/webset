import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;;
import java.lang.*;
import java.io.*;
import java.util.*;



public class Regist extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        PrintWriter out = resp.getWriter();
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading (
        		new File("/opt/apache-tomcat-7.0.27/webapps/a/template/"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        
        Template temp = cfg.getTemplate("regist.html");
        Map root = new HashMap();
        try {
            temp.process(root, out);		
            out.flush();
        } catch (Exception e) {
			// TODO: handle exception
		}
    }
}
