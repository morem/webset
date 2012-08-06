package sand.compent;

import freemarker.log.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MFreemarkerInit extends HttpServlet { 
     public void init() {
    	try {
            Logger.selectLoggerLibrary(Logger.LIBRARY_NONE);
        } catch (Exception e){
        }       
     }
}