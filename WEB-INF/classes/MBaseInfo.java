import org.omg.CORBA.PUBLIC_MEMBER;
import org.apache.log4j.*;

public class MBaseInfo
{
    static String serverAddr = null;
    public String appKey(){
        return "21040246";
    }
    public String appSecret()
    {
        return "dc32522be27808c4e1d36262d1e46d94";
    }
    public String url()
    {
        return "http://gw.api.taobao.com/router/rest";
    }
    public String dateBase()
    {
        return "/opt/data/";
    }
    public void setServerAddr(String str)
    {
        serverAddr = str;
    }
    public String getServerAddr()
    {
        return serverAddr;
    }
}