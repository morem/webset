package sand;

import sand.*;
import sand.message.*;
import sand.tal.*;
import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;;
import java.lang.*;
import java.io.*;
import java.util.*;

import org.apache.log4j.*;

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
