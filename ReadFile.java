import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadFile {

    // instance variables
    private static ReadFile config = null;
    private Properties p;

    // reads and fetches properties from config file
    private ReadFile() throws IOException {
        p = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (input != null) {
            p.load(input);
        } else {
            throw new FileNotFoundException("Configuration file not found");
        }
    }

    // fetches a single property from read file
    public static ReadFile getProperty() throws IOException {
        if (config == null) {
            config = new ReadFile();
        }
        return config;
    }

    // returns the server set by the config file
    public String getServer() {
        return p.getProperty("server");
    }

    // returns the port set by the config file
    public int getPort() {
        return Integer.parseInt(p.getProperty("port"));
    }

}
