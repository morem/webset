package sand.show;

import sand.*;
import sand.sys.*;
import sand.show.*;
import sand.tal.*;
import sand.sys.*;
import sand.message.*;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;



class MShowCaseEventBody
{
    int times;
    String nick;
}


public class MShowCaseEvent  extends HttpServlet  implements Runnable {
    
    static Map <String, MShowCaseEventBody> eventWaitBuffer = new HashMap<String, MShowCaseEventBody>();
    static List <MUser> eventList = new ArrayList<MUser>();
    static Thread t;
    static Logger logger = Logger.getLogger(MShowCaseEvent.class.getName());
    static boolean bInit = false;

    
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        t = new Thread (this, MShowCaseEvent.class.getName());
        t.start ();
    }

    public void addEvent (String id, String nick, String msg)
    {
        logger.debug("addEvent");
        MShowCaseEventBody body;
        if (false == eventWaitBuffer.containsKey (id))
            body = new MShowCaseEventBody ();
        else
            body = eventWaitBuffer.get (id);
        body.times = 5;
        body.nick = nick;
        eventWaitBuffer.put (id, body);
    }

    public List<MUser> getEvent ()
    {
        List <MUser> list = new ArrayList<MUser>();
        for (MUser u:eventList)
            list.add (u);
        eventList.clear();
        return list;
    }

    public void run ()
    {
        while (true)
        {
            try{
                Thread.currentThread().sleep(1000*10);
            }catch (Exception e){
                logger.error(e);
            }
            for (String id:eventWaitBuffer.keySet())
            {
                MUser u = new MUser ();
                MShowCaseEventBody b = eventWaitBuffer.get (id);
                b.times = b.times - 1;
                logger.debug ("check...id:" + id + " times:" + b.times);
                if (b.times == 0)
                {
                    u.id = id;
                    u.nick = b.nick;
                    eventList.add(u);
                    eventWaitBuffer.remove (id);
                    logger.debug("Start processEvent...");
                    new MProcessEvent ().start();
                }
                else
                {
                    eventWaitBuffer.put (id, b);  
                }
            }                
        }
    }    
}





