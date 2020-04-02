import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FetchProperties {

	private static FetchProperties i = null;
	private Properties p;

	// reads and fetches properties from config file
	private FetchProperties() throws IOException{
		p = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
		if(input != null){
			p.load(input);
		}else{
			throw new FileNotFoundException("Property file is not found");
		}
	}

	// fetches a single property/instance from read file, sets this as i
	public static FetchProperties fetchInstance() throws IOException{
		if(i == null){
			i = new FetchProperties();
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
