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
        HttpSession httpSession = req.getSession();
        out.println (httpSession.getAttribute("visitor_id"));
        httpSession.invalidate();
    }
}
