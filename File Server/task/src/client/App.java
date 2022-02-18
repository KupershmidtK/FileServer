package client;

import server.Request;
import server.RequestType;

import java.io.IOException;
import java.util.Scanner;

/**
 * Handle user inputs and form request.
 */
public class App {
    private final Scanner scanner = new Scanner(System.in);

    public Request readInput() throws Exception {
        Request request = null;

        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String action = scanner.nextLine();
        switch (action) {
            case "1": // GET
                request = requestGetDelete();
                request.setType(RequestType.GET);
                break;
            case "2": // PUT
                request = requestPut();
                request.setType(RequestType.PUT);
                break;
            case "3": // DELETE
                request = requestGetDelete();
                request.setType(RequestType.DELETE);
                break;
            case "exit": // EXIT
                request = new Request();
                request.setType(RequestType.EXIT);
                break;
            default:
                throw new IOException("Wrong action!");
        }
        return request;
    }

    private Request requestGetDelete() {
        Request request = new Request();

        if (getFileByName()) { request.setFileName(readFileName()); }
        else { request.setFileId(readFileId()); }
        return request;
    }

    private Request requestPut() throws IOException {
        Request request = new Request();

        request.setFileName(readFileName());
        request.setServerFileName(readFileServerName());
        request.setFileData(FileClient.read(request.getFileName()));
        return request;
    }

    private boolean getFileByName() {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        return Integer.parseInt(scanner.nextLine()) == 1;
    }

    private String readFileName() {
        System.out.print("Enter filename: ");
        // TODO check format of name
        return scanner.nextLine();
    }

    private long readFileId() throws  NumberFormatException {
        System.out.print("Enter id: ");
        return Long.parseLong(scanner.nextLine());
    }

    private String readFileServerName() {
        System.out.print("Enter name of the file to be saved on server: ");
        return scanner.nextLine();
    }
}
