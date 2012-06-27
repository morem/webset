import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

class MLayout
{
    public void PrintOut(PrintWriter out, String str)
    {
        out.println("MLayout"+str);
    }
}
public class DisplayAFile extends HttpServlet
{
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException,IOException
    {
        String user = req.getParameter("user");
        String welcomeInfo = "Welcome you, "+user;

        resp.setContentType ("text/html");

        PrintWriter out = resp.getWriter();

        out.println ("<html><head><title>");
        out.println ("Welcome Page");
        out.println ("</title></head>");
        out.println ("<body>");
        FileReader fs1 = new FileReader("/home/miao/work/webset/src/README");
        BufferedReader br = new BufferedReader(fs1);
        MLayout layout = new MLayout();
        layout.PrintOut (out,"-------------------------------------\n");
        MLayout2 layout2 = new MLayout2();
        layout2.PrintOut (out,"-------------------------------------\n");
        out.println (br.readLine());
        MLayout3 layout3 = new MLayout3();
        layout3.PrintOut (out,"-------------------------------------\n");
        out.println (br.readLine());
        out.println ("</body></html>");
        out.close();


        fs1.close();


    }

}
