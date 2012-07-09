import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.*;

public class MUserManager
{
    private String url = "jdbc:mysql://localhost:3306/sand";
    private String user = "root";
    private String password = "mjlmjl";
    private String driverClass = "com.mysql.jdbc.Driver";
    static HashMap map = new HashMap();
    static Logger logger = Logger.getLogger(MUserManager.class.getName());
    
    public MUser GetAndUpdateDatabase(String id, String token)
    {
        /*id char(16) not null primary key,
            -> nick char(50) not null,
            -> img char(255),
            -> path char(255)*/
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            logger.error("Load database error");
            return null;
        }
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("get Connection error");
            return null;
        }
        try {
            stmt = conn.createStatement();         
        } catch (Exception e) {
            logger.error("createStatement error");
            return null;
        }

        logger.debug("Read Database:select * from usermap where id='" + id.toString() + "'");
        try {
            rs = stmt.executeQuery("select * from usermap where id='" + id.toString() + "'");
        } catch (Exception e) {
            logger.error("executeQuery error");
            return null;
        }
        int row;
        try {
            MUser usr = new MUser();
            rs.next();
            usr.id = rs.getString("id");
            logger.debug("Get User ID:" + usr.id);
            if (false == usr.id.equals(id))
            {
                logger.error("Get the Item Error!");
            }
            else
            {
                usr.nick = rs.getString("nick");
                usr.avatar = rs.getString("img");
                usr.token = rs.getString("token");
                usr.vaild = true;
                logger.debug("Get User nick:" + usr.nick);
                return usr;
            }
            
        } catch (SQLException e) {
            logger.error("getRow error:" + e +":"+ e.getErrorCode());
            MTop_API top = new MTop_API();
            MUser usr;
            logger.debug("ID:" + id);
            logger.debug("Token:" + token);
            usr = top.getUserInfo(id,token);
            if (usr.vaild == true)
            {
                String sqlString =  "insert into usermap values(" +
                                    "'" + usr.id + "'," +
                                    "'" + usr.nick + "'," +
                                    "'" + usr.token + "'," +
                                    "'" + usr.avatar + "'," +
                                    "'" + "/home/miao/work/sanddata/" + "usr.id" + ".xml'" +
                                    ")";
                
                logger.debug("Add the User to Map:"+sqlString);
                try {
                    stmt.addBatch(sqlString);
                    stmt.executeBatch();
                } catch (Exception e1) {
                    logger.error("addBatch error:" + e1);
                    return null;
                }
                
                return usr;
            }
            return null; 
        }
        return null ;
    }
    
    public MUser GetUserByID(String id, String token, boolean update)
    {
        logger.debug("Get User By ID:"+ id + "   "+id.toString());
        MUser usr = null;
        usr = GetAndUpdateDatabase (id, token);
        if (usr == null)
        {
            logger.debug("Can't get usr data");
            return usr;
        }
        else {
            if (usr.vaild == false)
            {
                logger.debug("Can't get vaild usr data");
                return null;
            }
            else
                return usr;
        }
    }
    
    public MUser GetUserByID(String id)
    {
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            logger.error("Load database error");
            return null;
        }
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("get Connection error");
            return null;
        }
        try {
            stmt = conn.createStatement();         
        } catch (Exception e) {
            logger.error("createStatement error");
            return null;
        }

        logger.debug("Read Database:select * from usermap where id='" + id.toString() + "'");
        try {
            rs = stmt.executeQuery("select * from usermap where id='" + id.toString() + "'");
        } catch (Exception e) {
            logger.error("executeQuery error");
            return null;
        }
        int row;
        try {
            MUser usr = new MUser();
            rs.next();
            usr.id = rs.getString("id");
            logger.debug("Get User ID:" + usr.id);
            if (false == usr.id.equals(id))
            {
                logger.error("Get the Item Error!");
                return null;
            }
            else
            {
                usr.nick = rs.getString("nick");
                usr.avatar = rs.getString("img");
                usr.token = rs.getString("token");
                usr.vaild = true;
                logger.debug("Get User nick:" + usr.nick);
                return usr;
            }
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    public void UpdateAllProduction(String id)
    {
        MUser usr = GetUserByID (id);
        if (usr == null)return;
        if (usr.vaild == false)return;
       
    }
    
    public MUser GetShopInfo(String id)
    {
        MUser usr = GetUserByID(id);
        if (usr == null){
            logger.error("Get the User info error:"+id);
            return null ;
        }
        if (usr.vaild == false){
            logger.error("Get the User info error:"+id);
            return null;
        }
        MTop_API top = new MTop_API();
        logger.equals("ID:" + usr.id + "  token:" + usr.token);
        usr = top.getUserShowNum(usr.id, usr.token);
        return usr;
    }
}