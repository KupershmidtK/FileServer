package client;

import server.Request;
import server.Response;
import java.io.IOException;
import java.util.Scanner;

/**
 * public void handleResponse(Request request, Response response) throws IOException
 * Write file to folder (throws exception in case of error), print server's answer.
 */
public class ResponseHandler {

    public void handleResponse(Request request, Response response) throws IOException {
        switch (request.getType()) {
            case PUT:
                printPutResponse(response);
                break;
            case GET:
                printGetResponse(response);
                break;
            case DELETE:
                printDeleteResponse(response);
                break;
            default:
                break;
        }
    }

    private void printPutResponse(Response response) {
        String msg = "";
        if (response.getCode() == 200) {
            msg = "Response says that file is saved! ID = " + response.getId();
        } else {
            msg = "The response says that creating the file was forbidden!";
        }
        System.out.println(msg);
    }

    private void printGetResponse(Response response) throws IOException {
        String msg = "";
        if (response.getCode() == 200) {
            System.out.print("The file was downloaded! Specify a name for it: ");
            try (Scanner scanner = new Scanner(System.in)) {
                String fileName = scanner.nextLine();
                FileClient.write(fileName, response.getData());
            }
            msg = "File saved on the hard drive!";
        } else {
            msg = "The response says that this file is not found!";
        }
        System.out.println(msg);
    }

    private void printDeleteResponse(Response response) {
        String msg = "";
        if (response.getCode() == 200) {
            msg = "The response says that the file was successfully deleted!";
        } else {
            msg = "The response says that this file is not found!";
        }
        System.out.println(msg);
    }
}
