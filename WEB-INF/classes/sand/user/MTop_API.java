import javax.xml.ws.Response;

import com.taobao.api.*;
import com.taobao.api.domain.User;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.response.*;

import org.apache.log4j.*;


public class MTop_API extends Object
{
    static Logger logger = Logger.getLogger(MTop_API.class.getName());
    
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
            usrMUser.token = token;
            usrMUser.vaild = true;
        }
        catch (Exception e) {
            logger.error("UserGetRequest Fail!");
        }
        return usrMUser;
    }
  
    public MUser getUserShowNum(String id, String token)
    {
        TaobaoClient client=new DefaultTaobaoClient(new MBaseInfo().url(), 
                new MBaseInfo().appKey(), 
                new MBaseInfo().appSecret());
        
        ShopRemainshowcaseGetRequest req=new ShopRemainshowcaseGetRequest();
        logger.debug("Enter");
        MUser usr = new MUserManager().GetUserByID(id);
        if (usr.vaild == false){
            logger.error("Can't found the id info:" + id );
            return null;
        }
        
        try {

            ShopRemainshowcaseGetResponse response = client.execute(req , usr.token);
            if (response == null)
            {
                logger.error("ShopRemainshowcaseGetResponse error"); 
                return null;
            }
            usr.showCaseNum = response.getShop().getAllCount();
            usr.showCaseUsed= response.getShop().getUsedCount();
            usr.showCaseRemained = response.getShop().getRemainCount();
            usr.showCaseVaild = true;
            logger.debug("ShowCase NUM:" + usr.showCaseNum);
            logger.debug("ShowCase Used:" + usr.showCaseUsed);
            logger.debug("ShowCase NUM:" + usr.showCaseRemained);
            return usr;
        } catch (Exception e) {
            usr.showCaseVaild = false;  
            logger.error("Get ShowCase info error " + e);
            return null;
        }
    }
}