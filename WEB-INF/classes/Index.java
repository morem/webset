import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;;
import java.lang.*;
import java.io.*;
import java.util.*;

import java.net.URL;

public class Index extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        MCompent mc = new MCompent();  
	    PrintWriter out = resp.getWriter();
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading (
        		new File("./template/"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        cfg.setDefaultEncoding("ISO-8859-1");
       
        Template temp = cfg.getTemplate("index.html");

        Map root = new HashMap();
        String str = "首页";
        str = new String(str.getBytes("UTF-8"),"ISO-8859-1");
        root.put("title",str); 
        MCompent_Pane c = new MCompent_Pane();
        root.put("compent3", c.BuildAPane("/home/miao/work/webset/template/Pane.xml"));
        
        MCompent_Pane d = new MCompent_Pane();
        root.put("compent4", d.BuildAPane("/home/miao/work/webset/template/PanePackage.xml"));

        MCompent_Pane f = new MCompent_Pane();
        root.put("compent5", f.BuildAPane("/home/miao/work/webset/template/PaneClothes.xml"));

        MCompent_Pane h = new MCompent_Pane();
        root.put("compent6", h.BuildAPane("/home/miao/work/webset/template/PaneFans.xml"));
        
        try {
            temp.process(root, out);		
            out.flush();
        } catch (Exception e) {
			// TODO: handle exception
		}	
    }
}
