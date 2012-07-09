import java.util.ArrayList;

import org.apache.log4j.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public interface MDispatchCallback 
{
    public String load(String arg0, ArrayList list);
    public String getname();
}