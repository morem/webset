package sand.sys;

import sand.message.*;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MDispatchParam extends Object
{
    public String model;
    public String id;
    public List<String> list;
    public HttpServletRequest req;
    public HttpServletResponse resp;
    public Map map = null;
}
