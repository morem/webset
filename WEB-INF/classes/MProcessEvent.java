import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;

import com.taobao.api.internal.stream.Configuration;
import com.taobao.api.internal.stream.TopCometStream;
import com.taobao.api.internal.stream.TopCometStreamFactory;
import com.taobao.api.internal.stream.message.TopCometMessageListener;
import com.taobao.api.internal.stream.connect.ConnectionLifeCycleListener;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.*;
import net.sf.json.*;

public class MProcessEvent extends HttpServlet implements Runnable {
    static Thread t = null;
    static Logger logger = Logger.getLogger(MProcessEvent.class.getName());
    static boolean bInit = false;
    static boolean start = false;
    
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        t = new Thread (this, MProcessEvent.class.getName());
        t.start ();
    }

    public void start ()
    {
        if (t == null)return;
        logger.debug ("Start thread");        
        start = true;
    }

    public void run ()
    {
        while (true)
        {
            start = false;
            while (start == false)
                try{
                    Thread.sleep(100);
                }catch (Exception e){logger.error(e);}
            List<MUser> list = new MShowCaseEvent().getEvent();
            logger.debug (list);
            for (MUser user:list)
            {
                while (true)
                {
                    MUser show = new MTop_API().getUserShowNum (user.id);
                    List<MItem> m;
                    logger.debug (user.id + ": found remain show:" + show.showCaseRemained);
                    m = new MTop_API().getLeastForShow (user.id, show.showCaseRemained);
                    if (show.showCaseRemained == 0)break;
                    logger.debug (user.id + ": Get Iitem:" + m.size());
                    for (MItem l:m)
                    {
                        if (false == new MTop_API().setItemtoShowCase (user.id, l.num_iid))
                            logger.error("Set Item to ShowCase error item:" + l.num_iid);
                    }
                }
            }
        }
    }
}
