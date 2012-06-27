import javax.servlet.ServletException;
import java.io.*;
import javax.servlet.http.*;
import java.lang.*;

public class Login extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        PrintWriter out = resp.getWriter();
        MFile file = new MFile();
        String content = null;
        
        content = file.GetContent ("/opt/apache-tomcat-7.0.27/webapps/a/template/header.jsp");
        content += file.GetContent ("/opt/apache-tomcat-7.0.27/webapps/a/template/menu.jsp");
        content += file.GetContent ("/opt/apache-tomcat-7.0.27/webapps/a/template/footer.jsp"); 
        out.println(content);
        out.close();
    }
}
