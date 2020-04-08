package done;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFile {

    private static ReadFile file = null;
    private Properties p;

    // reads and fetches properties from config file
    private ReadFile() throws IOException {
        p = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("done/config.properties");
        if (input != null) {
            p.load(input);
        } else {
            throw new FileNotFoundException("Configuration file not found");
        }
    }

    // fetches a single property from read file
    public static ReadFile getProperty() throws IOException {
        if (file == null) {
            file = new ReadFile();
        }
        return file;
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
