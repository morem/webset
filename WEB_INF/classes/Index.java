import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;;
import java.lang.*;
import java.io.*;
import java.util.*;



public class Index extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
/*        PrintWriter out = resp.getWriter();

        MFile file = new MFile();
        String content = null;
        Configuration cfg = new Configuration();

        cfg.setDirectoryForTemplateLoading (
        		new File("/opt/apache-tomcat-7.0.27/webapps/a/template/"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        cfg.setDefaultEncoding("ISO-8859-1");

        Template temp = cfg.getTemplate("header.html");
        temp.setEncoding("GBK");
        //out.println (temp.toString());

        Map root = new HashMap();
        root.put("title","aaa");
        try {
            temp.process(root, out);		
           out.flush();
        } catch (Exception e) {
			// TODO: handle exception
		}
	*/
	        PrintWriter out = resp.getWriter();
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading (
        		new File("/opt/apache-tomcat-7.0.27/webapps/a/template/"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        cfg.setDefaultEncoding("ISO-8859-1");
        //cfg.setDefaultEncoding("UTF-8");
        
        Template temp = cfg.getTemplate("index.html");

        Map root = new HashMap();
        String str = "Ê×Ò³";
        str = new String(str.getBytes("UTF-8"),"ISO-8859-1");
        root.put("title",str);
        try {
            temp.process(root, out);		
            out.flush();
        } catch (Exception e) {
			// TODO: handle exception
		}

    }
}
