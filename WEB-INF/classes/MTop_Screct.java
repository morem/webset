import java.io.*;
import java.net.URLDecoder;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import com.taobao.api.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.*;

class MTop_Screct{
    
    static Logger logger = Logger.getLogger(MTop_Screct.class.getName());
	  /**
	 * 把经过BASE64编码的字符串转换为Map对象
	 *
	 * @param str callback url上top_parameters的值
	 * @param encode callback url上encode的值，如果不存在此参数请传null
	 * @return
	 */
	public Map<String, String> convertBase64StringtoMap(String str, String encode) 
	{
		URLDecoder urlDecoder = new URLDecoder();
	    if (str == null)
	        return null;
        if (encode == null)
                 encode = "GBK";
        String keyvalues = null;
        try {
                 keyvalues = new String(Base64.decodeBase64(urlDecoder.decode(str, "utf-8").getBytes(encode)), encode);
        } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
        }
        String[] keyvalueArray = keyvalues.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String keyvalue : keyvalueArray) {
                 String[] s = keyvalue.split("\\=");
                 if (s == null || s.length != 2)
                          return null;
                 map.put(s[0], s[1]);
        }
        return map;
	}
	
    public Map<String, String> convertBase64StringtoMap(HttpServletRequest req, String encode) 
    {
        String str = req.getParameter("top_parameters");
        URLDecoder urlDecoder = new URLDecoder();
        if (str == null)
            return null;
        if (encode == null)
                 encode = "GBK";
        String keyvalues = null;
        try {
                 keyvalues = new String(Base64.decodeBase64(urlDecoder.decode(str, "utf-8").getBytes(encode)), encode);
        } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
        }
        String[] keyvalueArray = keyvalues.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String keyvalue : keyvalueArray) {
                 String[] s = keyvalue.split("\\=");
                 if (s == null || s.length != 2)
                          return null;
                 {
                     logger.debug(s[0] +":"+ s[1]);
                     map.put(s[0], s[1]);
                 }
        }
        return map;
    }
    
	public boolean CheckTopSign(String top_appkey, 
                         String top_parameters,
                         String top_session,
                         String app_secret,
                         String top_sign,
                         StringWriter out)
	{
	    DigestUtils md = new DigestUtils();
	    
		byte[] digest = md.md5(top_appkey+top_parameters+top_session+app_secret);
		Base64  base64 = new Base64();
		String result = base64.encodeBase64String(digest);
		out.write("result:" + result + "<br>");
		out.write("top_sign:" + top_sign + "<br>");
		
		if (result.equals(top_sign))
		{
			return true;
		}
        return false;
	}
	
	public boolean TopCallbackCheck(HttpServletRequest req)
	{
	    String top_appkey = req.getParameter("top_appkey");
        String top_session = req.getParameter("top_session");
        String top_parameters = req.getParameter("top_parameters");
        String top_sign = req.getParameter("top_sign");
        StringWriter stringWriter = new StringWriter();
        logger.debug("top_appkey:"+top_appkey);
        logger.debug("top_session:"+top_session);
        logger.debug("top_parameters:"+top_parameters);
        logger.debug("top_sign:"+top_sign);
        
        if( true == CheckTopSign(top_appkey,
                top_parameters,
                top_session,
                new MBaseInfo().appSecret(),
                top_sign,
                stringWriter))
        {
            logger.debug("Check Top Sign Call Ok");
            return true;
        }
        else
        {
            logger.error("Check Top Sign Call Fail");
            return false;
        }	    
	}
	
}