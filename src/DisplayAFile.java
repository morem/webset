import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

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
		out.println (br.readLine());
                out.println ("</body></html>");
                out.close();
		fs1.close();


	}	
	
}
