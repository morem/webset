import org.apache.log4j.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Log4jInit extends HttpServlet { 
     public void init() {
          String prefix =  getServletContext().getRealPath("/");
          String file = getInitParameter("log4j");
          if(file != null) {  
              PropertyConfigurator.configure(prefix+file); 
          } 
     	Logger logger = Logger.getLogger(Log4jInit.class.getName());
    	logger.debug("Log Start Now............");
        
     }

}
