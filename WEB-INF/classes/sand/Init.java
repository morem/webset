package sand;
import sand.*;
import sand.compent.*;
import sand.message.*;
import sand.sys.*;
import sand.show.*;
import sand.tal.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Init extends HttpServlet
{
    public void init (){
        new Log4jInit ().init ();
        new MFreemarkerInit ().init ();
        new MDispatch ().init ();
        new MShowStatus ().init ();
        new MShowConfig ().init ();
        new MShowConfigItemManager ().init ();
        new MShowDataManager ().init ();
        new MPublicInfo ().init ();
        new MMessageListener ().init ();
        new MShowCaseEvent ().init ();
        new MProcessEvent ().init ();
        new MShowIndex ().init ();
    }
}
