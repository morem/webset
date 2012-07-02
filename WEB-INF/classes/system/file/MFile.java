import java.io.*;

public class MFile
{
	public String GetContent(String file)
	{
		String s;
		InputStream f;
		try {
			f = new FileInputStream(file);	
			try {
				byte b[] = new byte[f.available()];
				f.read(b);
				s = new String(b, "ISO-8859-1");
			} catch (Exception e) {
				return null;// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return s;
	}
	/*US-ASCII,ISO-8859-1,UTF-8,UTF-16BE,UTF-16LE,UTF-16*/
	public String GetContent(String file, String Charset)
	{
		String s;
		InputStream f;
		try {
			f = new FileInputStream(file);	
			try {
				byte b[] = new byte[f.available()];
				f.read(b);
				s = new String(b, Charset);
			} catch (Exception e) {
				return null;// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return s;
	}

}
