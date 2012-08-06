package sand.sys;
import java.io.*;
import org.apache.log4j.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class Log4jInit { 
     public void init() {
          System.out.print ("--------------------------------------------------\n");
          String file = "WEB-INF/log4j.properties\n";
          System.out.print (file + "\n");
          if(file != null) {  
              PropertyConfigurator.configure("/home/miao/work/webset/WEB-INF/log4j.properties"); 
          } 
     	Logger logger = Logger.getLogger(Log4jInit.class.getName());
    	logger.error("Log Start Now............");
        }
}
/*
public class Log4jInit 
{
    static Logger logger = Logger.getLogger(Log4jInit.class);

    public static void main(String[] args) {
        new tt().init ();
            
       }


}*/
