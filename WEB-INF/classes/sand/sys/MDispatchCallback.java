package sand.sys;

import sand.message.*;
import java.util.ArrayList;

import org.apache.log4j.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public interface MDispatchCallback 
{
    public String load(MDispatchParam param);
    public String getname();
}
