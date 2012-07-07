import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

import freemarker.log.Logger;

class XmlFileLoader{
	  public Document doc = null;
	  
	  public Document parseWithSAX(String string, StringWriter out){
	    SAXReader xmlReader = new SAXReader();
	    FileInputStream fs;
	    try{
	    	try{
	    		fs = new FileInputStream(string);
	    		return xmlReader.read(fs);
	    	}
	    	catch (Exception e) {
	    			// TODO: handle exception
	    		out.write(e.toString());
	    		return null;
	    	}
	    	
	    }
	    catch (Exception e) {
	    	out.write(e.toString());
	    	return null;
		}
	  }
}

class MDivProperty extends Object
{
	int index;
	String width = "";
	String height = "";
	String top = "";
	String left = "";
	String bottom = "";
	String right = "";
	String fontString = "";
	String bordString = "";
	String backgroundString = "";
}

public class MCompent_Pane extends MCompent
{
  static int n = 0;
  static Logger logger = Logger.getLogger(MCompent_Pane.class.getName());
  public String GetCodeByIndex (int index, Document doc)
  {
	  String string ="";
	  Element root = doc.getRootElement();
	  List list = root.selectNodes("unit");
	  Element elementIndex = (Element)list.get(index);
	  list = elementIndex.selectNodes("model");
	  if (list.size() == 0)return "";
	  elementIndex = (Element)list.get(0);
	  list = elementIndex.elements();
	  Map map = new HashMap();
	  for (Object obj:list)
	  {
		  Element element = (Element)obj;
		  string += ("Name:"+ element.getName() + "------Text:" + element.getText() + "<br>");
		  map.put(element.getName(), element.getText());
	  }
  
	  MCompent mc = new MCompent();
	  string = mc.GetModel(map.get("template").toString(), map);
	  return string;
  }
  
  private MDivProperty ParseElement (MDivProperty property, Element element, StringWriter out)
  {
	  String name = element.getName();
	  String value = element.getText();
	  if (name == "width")
		  property.width = value;
	  else if (name == "height")
		  property.height = value;
	  else if (name == "bord")
		  property.bordString = value;
	  else if (name == "left")
		  property.left = value;
	  else if (name == "right")
		  property.right = value;
	  else if (name == "top")
		  property.top = value;
	  else if (name == "bottom")
		  property.bottom = value;
	  else if (name == "font")
		  property.fontString = value;
	  else if (name == "background")
		  property.backgroundString = value;
	  else
		  return null;
	  return property;
  }
  
  private List GetAllDivProperty(Document doc, StringWriter out) 
  {
	  List listMDivProperty = new ArrayList();
	  MDivProperty property = new MDivProperty();
	  Element root = doc.getRootElement();
	  List list = root.elements();
	  
	  for (Object obj:list)
	  {
		  Element element = (Element)obj;
		  if (element.getName() != "unit")
		  {
			  ParseElement (property, element, out);
			  out.write("Name1:" + element.getName()+"<br>");
		  }
	  }
	  
	  listMDivProperty.add(property);
	  
	  for (Object obj:list)
	  {
		  Element element = (Element)obj;
		  List listT = element.elements();
		  MDivProperty property2 = new MDivProperty();
		  if (element.getName() != "unit")continue;
		  for (Object obj2:listT)
		  {
			  Element element2 = (Element)obj2;
			  if (element2.getName() == "model")continue;
			  ParseElement (property2, element2, out);
			  out.write("Name2:" + element2.getName()+
					    "-----" + 
					    "Value:" + element2.getText() + "<br>");
		  }
		  listMDivProperty.add(property2); 
	  }
	  
	  return listMDivProperty;
  }
  
  
  public String BuildAPane (String xml)
  {
    String cssString = "";
    String htmlString = "";
    String text = "";
	Document doc = null;
	this.n++;
	XmlFileLoader foo = new XmlFileLoader();
	StringWriter outStringWriter = new StringWriter();
	try {
		doc = foo.parseWithSAX(xml, outStringWriter);
		
		if (doc == null)text += outStringWriter.toString();
	} catch (Exception e) {
		text += e.toString();
	}
	
	List list = GetAllDivProperty(doc, outStringWriter);
	for (Object obj:list)
	{
		MDivProperty property = (MDivProperty)obj;
		//htmlString += (
		//		"Width:"+property.width + "<br>"
		//		);
	}
	//htmlString  += outStringWriter.toString();
	//return htmlString;
	
	for (int i = 1 ; i < list.size(); i++)
	{
		MDivProperty property = (MDivProperty)list.get(i);
        cssString += (".Pane"+ this.n + i+
                "{" +
                "width:" + property.width  + ";" + 
                "height:" + property.height + ";" +
                "position:" + "absolute" + ";" +
                "display:" + "block" + ";" +
                "left:" + property.left + ";" +
                "top:" + property.top + ";" +
                "overflow:" + "hidden;" +
                "margin:" + "0 0 0 0;" +
                "padding:" + "10 10 0 0;" +
                "}"
                );
        if (doc != null)
        {
        	htmlString += "<div class=\"Pane" + this.n + i +" \">";
        	htmlString += GetCodeByIndex (i - 1, doc);
        	htmlString += "</div>";
        }
	}
	
	MDivProperty property = (MDivProperty)list.get(0);
	String cssHeadString;
	cssHeadString = "<style type=\"text/css\">" + 
            ".Pane" + n +
            "{" +
            "width:" + property.width + ";";
	cssHeadString += ("height:" + property.height + ";");
    cssHeadString += 
           ("position:" + "relative" + ";" +
            "overflow:" + "hidden;" +
            "margin:" + "0px 0px 0px 0px;" +
            "padding:" + "0px 0px 0px 0px;");
	if (property.backgroundString != "")
		cssHeadString += "background:"+property.backgroundString+";";
	if (!property.left.isEmpty() )cssHeadString += "left:"+property.left+"px;";
	if (!property.top.isEmpty() )cssHeadString += "top:"+property.top+"px;";
	cssHeadString += "}";
          cssString = cssHeadString +cssString + "</style>";
    htmlString = "<div class=\"Pane" + n + "\"\\>" + htmlString + "</div>";
    return (cssString + htmlString);
      
  }

}
