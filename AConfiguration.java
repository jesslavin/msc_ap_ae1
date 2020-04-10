// Jessica Lavin - 2495543L

public class AConfiguration {

    // returns the port set by the config file
    public static int getPort() {
        int port = 8765;
        return port;
    }

    // returns the server set by the config file
    public static String getServer() {
        String server = "127.0.0.1";
        return server;
    }

}
