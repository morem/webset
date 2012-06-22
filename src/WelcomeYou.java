import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class WelcomeYou extends HttpServlet
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
		out.println (welcomeInfo);
		out.println ("</body></html>");
		out.close();		
	}	
	
}