import java.util.*;
import java.io.*;
import java.lang.*;
import javax.swing.ListModel;
import javax.xml.ws.Response;
import java.net.*;

import com.sun.msv.grammar.ListExp;
import com.taobao.api.*;
import com.taobao.api.domain.ShopCat;
import com.taobao.api.domain.User;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.domain.*;
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
    
    public boolean getUserSubscribes(String nick)
    {
        return true;
    }
  
    public MUser getUserShowNum(String id)
    {
        TaobaoClient client=new DefaultTaobaoClient(new MBaseInfo().url(), 
                new MBaseInfo().appKey(), 
                new MBaseInfo().appSecret());
        
        ShopRemainshowcaseGetRequest req=new ShopRemainshowcaseGetRequest();

        if (id.equals("Guest"))
        {
            MUser usr = new MUser();
            usr.showCaseNum = 40;
            usr.showCaseUsed= 40;
            usr.showCaseRemained = 0;
            usr.showCaseVaild = true;
            return usr;
        }

        try {
            MUser usr = new MUser();
            logger.debug(new MUserData().GetUserToken(id));
            String token = new MUserData().GetUserToken(id);
            if (token == null)
            {
                logger.error("Can't find the user's token");
                return null;
            }
            ShopRemainshowcaseGetResponse response = client.execute(req , token);
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
            logger.error("Get ShowCase info error " + e);
            return null;
        }
    }
    
    public Long getItemsCnt(String id)
    {
        Long ms = System.currentTimeMillis();
        Long i = ms - (new MUserData().getItemTotalNumUpdateTime(id));
        logger.debug("Cur ms:" + ms + " Get ms:" + i);
        if (i < 10*60*1000)
        {
            logger.debug("beacuse total num is get not long ago, so use it");
            Long num = new MUserData().GetItemsTotalNum(id);
            return num;
        }
        else
        {
            Long num;
            List<MItem> list = null;
            logger.debug("because total num is so long time not get, so get it new");
            list = getItems(id, null, -1L, null, -1L, -1L, null, -1L);
            if (list != null)
            {
                num = list.get(0).total_num; 
                new MUserData().SetItemsTotalNum(id, num);
                new MUserData().SetItemTotalNumUpdateTime(id);
                return num;
            }
            return 0L;
        }
    }
    
    private void setItemsCnt(String id, Long num)
    {
        logger.debug("set it cnt:" + num);
        new MUserData().SetItemsTotalNum(id, num);
        new MUserData().SetItemTotalNumUpdateTime(id);
    }

    private List<MItem> getGuestItem()
    {
        return null;
    }
    
    public List<MItem> getItems(String id, String title, Long cid, 
                               String seller_cid, Long page_no, 
                               Long has_showcase, String order, Long page_size)
    {
        TaobaoClient client=new DefaultTaobaoClient(new MBaseInfo().url(), 
                new MBaseInfo().appKey(), 
                new MBaseInfo().appSecret());
        ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
        Boolean canSetItemCntBoolean = true;
        logger.debug("id:" + id);
        logger.debug("title:" + title);
        logger.debug("cid:" + cid);
        logger.debug("seller_cid:" + seller_cid);
        logger.debug("page_no:" + page_no);
        logger.debug("has_showcase:" + has_showcase);
        logger.debug("order:" + order);
        logger.debug("page_size:" + page_size);
        
        req.setFields("num_iid,title,pic_url,has_showcase");
        if (title != null){   
            logger.debug("set title" + title);
            String t = title;
            req.setQ(t);
            canSetItemCntBoolean = false;
        }
        if (cid != -1){
            logger.debug("set cid");
            req.setCid(cid);
            canSetItemCntBoolean = false;
        }
        if (seller_cid != null){
            logger.debug("set seller_cid");
            req.setSellerCids(seller_cid);
            canSetItemCntBoolean = false;
        }
        if (page_no != -1){
            logger.debug("set page_no");
            req.setPageNo(page_no);
            canSetItemCntBoolean = false;
        }
        if (has_showcase == 1){
            logger.debug("set has_showcase true");
            req.setHasShowcase(true);
            canSetItemCntBoolean = false;
        }else if (has_showcase == 0)
        {
            logger.debug("set has_showcase false");
            req.setHasShowcase(false);
            canSetItemCntBoolean = false;
        }
        if (order != null){
            logger.debug("set order");
            req.setOrderBy(order);
            canSetItemCntBoolean = false;
        }
        if (page_size != -1){
            logger.debug("set page_size");
            req.setPageSize(page_size);
            canSetItemCntBoolean = false;
        }
        
        String token = new MUserData().GetUserToken(id);
        if (token == null){
            logger.error("Can't find the user's token");
            return null;
        }
        Long times = 3L;

        if (id.equals("Guest"))
            return getGuestItem();

        
        while (true)
        {
            try {

                ItemsOnsaleGetResponse response = client.execute(req , token);     
                logger.error("here");
                List<Item> list = new ArrayList<Item>();
                List<MItem> listM = new ArrayList<MItem>();
                logger.error("here");
                if (response == null)
                {
                    logger.error("Get The Item fail respone is null");
                    return null;
                }
                if (0 == response.getTotalResults())
                {
                    logger.error("get total results 0");
                    return listM;
                }
                
                list = response.getItems();
                if (list == null)
                {
                    logger.error("not Item found");
                    return listM;
                }

                logger.error("here");
                Long totalItem = response.getTotalResults();
                for (Object obj:list)
                {
                    Item item = null;
                    MItem itemM = new MItem();
                    item = (Item)obj;
                    itemM.num_iid = item.getNumIid().toString();
                    itemM.pic_url = item.getPicUrl();
                    itemM.title = item.getTitle();
                    itemM.total_num = totalItem;
                    itemM.hasShowcase = item.getHasShowcase();
                    listM.add(itemM);
                }
                if (canSetItemCntBoolean)setItemsCnt (id, totalItem);
                return listM;
            }catch (Exception e) {
                logger.error("Taobao API Call fail" + e);
                if (true == e.getLocalizedMessage().matches(".{1,}SocketTimeoutException.{1,}"))
                {
                    times --;
                    if (times > 0)
                    {
                        logger.error("try again:"+ times);
                        continue;
                    }
                    else
                    {
                        logger.error("always timeout return");
                        return null;
                    }
                }
                
                return null;
            } 
        }
    }
    
    public List<SellerCat> GetTheUserCats(String id)
    {
        if (id.equals("Guest"))
        {
            List<SellerCat> list = new ArrayList();
            for (Long i = 0L ; i < 10L; i ++)
            {
                SellerCat cat = new SellerCat();
                cat.setCid(1000L + i*100L);
                cat.setParentCid(0L);
                cat.setName("Father Item " + (1000L + i*100L));
                list.add(cat);
                if (i == 3L)continue;
                for (Long n = 0L; n < 6L; n ++)
                {
                    SellerCat catSon = new SellerCat();
                    catSon.setCid(2000L + i*100L + n);
                    catSon.setParentCid(1000L + i*100L);
                    catSon.setName("Son Item " + (2000L + i*100L + n));
                    list.add(catSon);
                }
            }
            return list;
        }
        
        TaobaoClient client=new DefaultTaobaoClient(new MBaseInfo().url(), 
                new MBaseInfo().appKey(), 
                new MBaseInfo().appSecret());
        SellercatsListGetRequest req=new SellercatsListGetRequest();
        try {
            req.setNick(new MUserData().GetUserNick(id));
            logger.debug("Get " + new MUserData().GetUserNick(id) + "'s cats");
            SellercatsListGetResponse response = client.execute(req);
            if (response == null)
            {
                logger.error("Get Sellercats Error");
                return null;
            }
            
            List<SellerCat> selerrCat = response.getSellerCats();
            return selerrCat;
        } catch (Exception e) {
            logger.error("Get User Cata error:" + e);
            return null;
        }
        
    }

    public Boolean UserNotifyPermit(String id)
    {

        TaobaoClient client=new DefaultTaobaoClient (new MBaseInfo().url(), 
                                                     new MBaseInfo().appKey(),
                                                     new MBaseInfo().appSecret());
        
        IncrementCustomerPermitRequest permitReq = new IncrementCustomerPermitRequest();
        permitReq.setType("get,notify");
        permitReq.setTopics("trade;refund;item");
        permitReq.setStatus("all;all;all");
        
        String token = new MUserData().GetUserToken(id);
        if (token == null){
            logger.error("Can't find the user's token");
            return false;
        }

        try {
            IncrementCustomerPermitResponse permitResp = client.execute(permitReq, token);
            if(permitResp.isSuccess()){
                logger.debug("IncrementCustomerPermitResponse success");
            }else{
                logger.error("IncrementCustomerPermitResponse error:" + permitResp.getBody());
            }
        } catch (ApiException e) {
            logger.error (e);
            return true;
        }
        return true;
    }

    public List<MItem> getItemsInfo (String id, List<String> itemIDList, 
                                     String field)
    {
        int page = (itemIDList.size() + 20 - 1)/20;
        if (field == null) field = "num_iid,title,nick,pric,has_showcase";
        
        String token = new MUserData().GetUserToken(id);
        if (token == null){
            logger.error("Can't find the user's token");
            return null;
        }
        List<MItem> itemList = new ArrayList<MItem>();

        for (int i = 0 ; i < page; i ++)
        {
            String itemIDs ;
            itemIDs = "";
            for (String l:itemIDList)
                itemIDs += (l + ",");

            try {
                TaobaoClient client=new DefaultTaobaoClient (new MBaseInfo().url(), 
                                                             new MBaseInfo().appKey(),
                                                             new MBaseInfo().appSecret());

                ItemsListGetRequest req=new ItemsListGetRequest();
                req.setFields(field);
                req.setNumIids(itemIDs);
                ItemsListGetResponse response = client.execute(req, token);
                List<Item> list;
                list = response.getItems();
                for (Item l:list)
                {
                    MItem k = new MItem();
                    k.title = l.getTitle();
                    k.num_iid = l.getNumIid().toString();
                    k.pic_url = l.getPicUrl();
                    k.hasShowcase = l.getHasShowcase();
                    itemList.add (k);
                }
                return itemList;
            }catch (Exception e){
                logger.error (e);
                continue;
            }
        }
        return null;
    }

    public List<MItem> getLeastForShow (String id, long num)
    {
        List <MItem> items = new ArrayList<MItem>();
        List <MItem> itemsTemp = new ArrayList<MItem>();
        List<String> cats = new MUserData().GetCatsListShow(id);
        List<String> forceShow = new MUserData().GetForceShow(id);
        List<String> forbidShow = new MUserData().GetForbidShow(id);
        int page = 0;

        String seller_cid = null;
        if (cats != null)
        {
            for (String cat:cats)
                seller_cid += cat;
        }
        
        logger.debug ("Seller_cid:" + seller_cid);

        List<MItem> forceShowItemStatus = new ArrayList<MItem>();
        forceShowItemStatus = getItemsInfo (id, forceShow, null);
        for (MItem m:forceShowItemStatus)
            if (m.hasShowcase == false)
                if (num -- != 0)items.add (m);
                else return items;
        
        page = 0;
        while (true){
                logger.debug (".");
            try{
                itemsTemp = getItems(id, null, -1L, seller_cid, new Long(page), 0L, "delist_time:asc", 200L);
                page ++;
                for (MItem m:itemsTemp)
                    if (forbidShow.contains(m.num_iid))
                        continue;
                    else
                        if (num -- != 0)items.add (m);
                        else{
                            logger.debug("out");
                            return items;
                            }
                if (itemsTemp.get(0).total_num % 200 == 0 && page == itemsTemp.get(0).total_num/20 ){
                    logger.debug("out");
                    return items;
                    }
                else if (itemsTemp.size() < 20){
                    logger.debug("out");
                    return items;
                    }
            }catch (Exception e){
                logger.error ("a error occur"+e);
                logger.debug("out");
                return items;
            }
        }
    }

    public Boolean setItemtoShowCase (String id, String num_iid)
    {
        String token = new MUserData().GetUserToken(id);
        if (token == null){
            logger.error("Can't find the user's token");
            return false;
        }
        try{
            TaobaoClient client=new DefaultTaobaoClient (new MBaseInfo().url(), 
                                                         new MBaseInfo().appKey(),
                                                         new MBaseInfo().appSecret());

            ItemRecommendAddRequest req=new ItemRecommendAddRequest();
            req.setNumIid(Long.valueOf(num_iid));
            ItemRecommendAddResponse response = client.execute(req , token);
            return true;
        }catch (Exception e){
            logger.error (e);
            return false;
        }
    }
}



