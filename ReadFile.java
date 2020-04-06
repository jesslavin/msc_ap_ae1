import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFile {

	private static ReadFile i = null;
	private Properties p;

	// reads and fetches properties from config file
	private ReadFile() throws IOException{
		p = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
		if(input != null){
			p.load(input);
		}else{
			throw new FileNotFoundException("Configuration not found");
		}
	}

	// fetches a single property/instance from read file, sets this as i
	public static ReadFile getInstance() throws IOException{
		if(i == null){
			i = new ReadFile();
		}
		return i;
	}

	// returns the server set by the config file
	public String getServer(){
		return p.getProperty("server");
	}

	// returns the port set by the config file
	public int getPort(){
		return Integer.parseInt(p.getProperty("port"));
	}
	
}
