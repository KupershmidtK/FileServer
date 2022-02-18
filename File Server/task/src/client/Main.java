package client;

import server.Request;
import server.Response;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String PATH = System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

        File path = new File(PATH);
        if (!path.exists()) {
            path.mkdirs();
        }

        Request request = null;
        try {
            /**
             * Select command, ask parameters and form request for server
             */
            App app = new App();
            request = app.readInput();
        } catch (Exception e) {
            System.out.println("Wrong input parameters!");
            e.printStackTrace();
            return;
        }

        try {
            /**
             * Send request to server
             * Get response
             */
            ClientTransmitter transmitter = new ClientTransmitter();
            transmitter.send(request);
            Response response = transmitter.receive();

            /**
             * Handle response, print information
             */
            new ResponseHandler().handleResponse(request, response);
        } catch (Exception e) {
            System.out.println("Error transmitting file!");
            // e.printStackTrace();
        }
    }
}

