import javax.servlet.ServletException;
import java.io.*;
import javax.servlet.http.*;

public class SimpleHello extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<BODY><B>HelloWorld-------------miao</B>");
        out.println("</BODY>");
        out.println("</HTML>");
        out.close();
    }

}
