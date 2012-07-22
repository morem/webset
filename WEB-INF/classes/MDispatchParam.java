import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MDispatchParam extends Object
{
    String model;
    String id;
    List<String> list;
    HttpServletRequest req;
    HttpServletResponse resp;
    Map map = null;
}