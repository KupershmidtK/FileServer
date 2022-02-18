package client;

import server.Request;
import server.RequestType;
import server.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * void send(Request) send Request object to server
 * Response receive() receive the answer and fill Receive object
 */
public class ClientTransmitter {
    static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 7778;
    private final DataOutputStream os;
    private final DataInputStream is;

    private Request request;

    public ClientTransmitter() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        os = new DataOutputStream(socket.getOutputStream());
        is = new DataInputStream(socket.getInputStream());
    }

    public void send(Request request) throws IOException {
        this.request = request;
        String header = requestToString(request);

        os.writeUTF(header);
        if (request.getType() == RequestType.PUT && request.getFileData() != null) {
            os.writeInt(request.getFileData().length);
            os.write(request.getFileData());
        }
        System.out.println("The request was sent.");
    }

    public Response receive() throws IOException {
        Response response = stringToResponse(is.readUTF());

        if (request.getType() == RequestType.GET && response.getCode() == 200) {
            int length = is.readInt();
            byte[] data = new byte[length];
            is.readFully(data, 0, data.length);
            response.setData(data);
        }
        return response;
    }

    private String requestToString(Request request) {
        String msg = request.getType().name();

        switch (request.getType()) {
            case PUT:
                if (!request.getServerFileName().isEmpty()) {
                    msg += " " + request.getServerFileName();
                }
                break;
            case GET:
            case DELETE:
                if (request.getFileId() != 0) {
                    msg += " BY_ID " + request.getFileId();
                } else if (!request.getFileName().isEmpty()) {
                    msg += " BY_NAME " + request.getFileName();
                }
            default:
                break;
        }
        return msg;
    }

    private Response stringToResponse(String header) {
        Response response = new Response();

        String[] args = header.split("\\s+");
        response.setCode(Integer.parseInt(args[0]));
        if (args.length > 1) {
            response.setId(Long.parseLong(args[1]));
        }
        return response;
    }
}