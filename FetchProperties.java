import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class XFetchProperties {

	private static XFetchProperties i = null;
	private Properties p;

	// reads and fetches properties from config file
	private XFetchProperties() throws IOException{
		p = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("Xconfig.properties");
		if(input != null){
			p.load(input);
		}else{
			throw new FileNotFoundException("Configuration not found");
		}
	}

	// fetches a single property/instance from read file, sets this as i
	public static XFetchProperties fetchInstance() throws IOException{
		if(i == null){
			i = new XFetchProperties();
		}
		return i;
	}

	// returns the server set by the config file
	public String fetchServer(){
		return p.getProperty("server");
	}

	// returns the port set by the config file
	public int fetchPort(){
		return Integer.parseInt(p.getProperty("port"));
	}
	
}
