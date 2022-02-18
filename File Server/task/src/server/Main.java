package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.runServer();
    }
}

/**
 * Setup application parameters.
 * Run the server.
 */
class App {
    private static final String PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private static final int PORT = 7778;
    private RequestHandler requestHandler = null;
    private static ServerSocket serverSocket;

    public static void closeSocket() {
        try { serverSocket.close(); }
        catch (IOException e) { }
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public App() {
        try {
            requestHandler = (RequestHandler) SerializationUtils.deserialize("ServerParams.dat");
        } catch (Exception e) {
            requestHandler = new RequestHandler();
        }

        File path = new File(PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        requestHandler.setPATH(PATH);
    }

    public void runServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started!");

            while (true) {
                ServerTransmitter transmitter = new ServerTransmitter(serverSocket.accept(), requestHandler);
                transmitter.start();
            }

        } catch (SocketException e) {
            System.out.println("Server stopped!");
        } catch (Exception e) {
             e.printStackTrace();
        } finally {

            try {
                SerializationUtils.serialize(requestHandler, "ServerParams.dat");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

