import org.apache.log4j.*;
import freemarker.template.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.transform.*;
import java.lang.*;
import java.io.*;
import java.util.*;

import java.net.URL;

import com.taobao.api.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.response.*;

public class Index extends HttpServlet
{
    static Logger logger = Logger.getLogger(Index.class.getName());
    
    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    { 
        PrintWriter out = resp.getWriter();
        //Session处理，判断是否在会话时间内
        HttpSession httpSession = req.getSession();
        String visitor_id = (String)httpSession.getAttribute("visitor_id");
        if (visitor_id == null)
        {
            visitor_id = "Guest";
            httpSession.invalidate();
        }
        
        //得到控制器 . index.html/后面跟的是分支控制器
        String control = req.getPathInfo();
        if (control == null)control = "#";
        logger.debug("PathInfo:" + req.getPathInfo());

        //得到主页模版
	    Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading (
        		new File("./template/sand"));
        cfg.setObjectWrapper (new DefaultObjectWrapper());
        cfg.setDefaultEncoding("ISO-8859-1");
        Template temp = null;
        
        String ctype = req.getParameter("ct");
        if (ctype != null)
        {
            if (ctype.equals("1"))
                temp = cfg.getTemplate("index.html");
            else
                temp = cfg.getTemplate("index2.html");
        }
        else
            temp = cfg.getTemplate("index.html");
        //构建基本的内容map
        Map root = new HashMap();
        String str = "欢迎"+ visitor_id;
        str = new String(str.getBytes("UTF-8"),"ISO-8859-1");
        root.put("title",str); 
        logger.debug("Get Header:" + req.getLocalAddr() + req.getLocalPort());
        new MBaseInfo().setServerAddr("http://" + req.getLocalAddr() + ":" + req.getLocalPort());
        //基路径
        root.put("ip", "http://" + req.getLocalAddr() + ":" + req.getLocalPort());
        MCompent_Pane c = new MCompent_Pane();
        
        logger.debug("getRequestURL():" + req.getRequestURL());
        //进入分支创建子页面
        MDispatch patch= new MDispatch();
        MDispatchParam param = new MDispatchParam();
        param.id = visitor_id;
        param.req = req;
        param.resp = resp;
        String string = patch.Dispatch(control, param);
        root.put("compent1", string);
        
        //输出页面
        MUserManager m = new MUserManager();
        MUser user = m.GetUserByID(visitor_id);

        try {
            temp.process(root, out);		
            out.flush();
        } catch (Exception e) {
			// TODO: handle exception
        }
    }
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    { 
        doGet(req, resp);
    }

}
