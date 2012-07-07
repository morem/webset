import javax.xml.ws.Response;

import com.taobao.api.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.response.*;

import freemarker.log.Logger;


public class MTop_API extends Object
{
    static Logger logger = Logger.getLogger(MTop_Screct.class.getName());
    
    public MUser getUserInfo(String id, String token)
    {
        MUser usrMUser = new MUser();
        usrMUser.vaild = false;
        TaobaoClient client=new DefaultTaobaoClient(new MBaseInfo().url(), 
                new MBaseInfo().appKey(), 
                new MBaseInfo().appSecret());
        
        UserGetRequest req =new UserGetRequest();
        req.setFields("user_id,nick,seller_credit,avatar");
        try{
            UserGetResponse response = client.execute (req, token);
            
            usrMUser.id = response.getUser().getUserId().toString();
            usrMUser.nick = response.getUser().getNick();
            usrMUser.avatar = response.getUser().getAvatar();
            usrMUser.vaild = true;
        }
        catch (Exception e) {
            logger.error("UserGetRequest Fail!");
        }
        return usrMUser;
    }
  
}