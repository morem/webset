package sand.message;

import sand.*;
import sand.sys.*;
import sand.tal.*;
import sand.show.*;
import sand.message.*;
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


class ConnectionListener implements ConnectionLifeCycleListener{
	private HttpServletResponse resp ;
    static Logger logger = Logger.getLogger(ConnectionListener.class.getName());
	public void onConnect() {
			logger.debug("Connect Suc, start listening...");
	}
	public void onException(Throwable throwable) {
			logger.debug("Some Exception have occur...");
	}

	public void onConnectError(Exception e) {
			logger.debug("Connect error , check it:" + e);
	}

	public void onReadTimeout() {
			logger.debug("read time out... need read it again...");
	}
	public void onSysErrorException(Exception e) {
			logger.debug("error occur ,check it:" + e);
	}

	public void onMaxReadTimeoutException() {
		    logger.debug("10 times time out by half an hour... check it...");
	}
	@Override
	public void onBeforeConnect() {
	}
	
}

class MessageListener implements TopCometMessageListener{
	private HttpServletResponse resp;
    static Logger logger = Logger.getLogger(MMessageListener.class.getName());


    public String getKeyValue (String json, String key)
    {
        int index = json.indexOf(key);
        key = key + "\":";
        String temp = json.substring(index + key.length());
        temp = temp.substring(0,temp.indexOf(","));
        if (temp.startsWith("\""))
            return temp.substring(1, temp.length() - 1);
        else
            return temp;
    }

	public void onReceiveMsg(String message) {
	    logger.debug("get a message:" + message);
		logger.debug("have send to process:");
        String topic = getKeyValue(message, "topic");
        String nick = getKeyValue(message, "nick");
        String status = getKeyValue(message, "status");
        String user_id = getKeyValue(message, "user_id");
        logger.debug ("topic:"+ topic);
        logger.debug ("nick:"+ nick);
        logger.debug ("status:"+ status);
        logger.debug ("user_id:"+ user_id);
      //  MessageDecode dec 


        
        if (status.equals("ItemRecommendDelete"))
        {
            
            logger.debug("create thread for message buffer");
            MShowCaseEvent msg = new MShowCaseEvent ();
            msg.addEvent (user_id, nick, message);
        }
	}

	public void onConnectReachMaxTime() {
			logger.debug("reach the max connect time, close it ");
	}


	public void onDiscardMsg(String message) {
			logger.debug(message);
	}

	public void onServerUpgrade(String message) {
		
	}

	public void onServerRehash() {
	}

	public void onServerKickOff() {
			logger.debug("process time out, check it...");
	}

	public void onOtherMsg(String message) {
			logger.debug(message);
	}

	public void onException(Exception ex) {
			logger.debug(ex.getMessage());
	}

	public void onConnectMsg(String message) {
			logger.debug("connect to server:" + message);
	}

	public void onHeartBeat() {
            logger.debug("nothing heppen, only a heart beat");
	}

    public void onClientKickOff() {
			logger.debug("two use one appkey connect , so close it");
	}
}


public class MMessageListener extends HttpServlet implements MDispatchCallback
{
    static boolean bInit = false;
    private String className = "messagelistener";
    static Logger logger = Logger.getLogger(MMessageListener.class.getName());
    public void PermitReq()
    {
        logger.debug("Create notify connection to server...");
        Configuration config = new Configuration(new MBaseInfo().appKey(),
                                                 new MBaseInfo().appSecret(),
                                                 null);

        TopCometStream stream = new TopCometStreamFactory(config).getInstance();
        stream.setConnectionListener(new ConnectionListener());
        stream.setMessageListener(new MessageListener());
        stream.start();
    }


    
    public void init()
    {
        if (bInit == true)return;
        bInit = true;
        logger.debug("Init");
        PermitReq ();
        
    }    

    public String load(MDispatchParam param)
    {
        return "ok";
    }


    public String getname()
    {
        return className;
    }
}

