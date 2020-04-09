import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFile {

	// instance variables
	private static ReadFile config = null;
	// fetches a single property from read file
	public static ReadFile getProperty() throws IOException {
		if (config == null) {
			config = new ReadFile();
		}
		return config;
	}

	private Properties p;

	// reads and fetches properties from config file
	private ReadFile() throws IOException {
		this.p = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		if (input != null) {
			this.p.load(input);
		} else {
			throw new FileNotFoundException("Configuration file not found");
		}
	}

	// returns the port set by the config file
	public int getPort() {
		return Integer.parseInt(this.p.getProperty("port"));
	}

	// returns the server set by the config file
	public String getServer() {
		return this.p.getProperty("server");
	}

}
