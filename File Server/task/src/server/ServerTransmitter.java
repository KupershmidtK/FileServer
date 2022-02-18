package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

 public class ServerTransmitter extends Thread {
    private final Socket socket;
    private final RequestHandler requestHandler;

    public ServerTransmitter(Socket socket, RequestHandler requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try (
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream())
        ) {
            String requestStr = is.readUTF();
            if (!requestStr.isEmpty()) {
                Request request = parseHeader(requestStr);
                if (request.getType() == RequestType.PUT) {
                    int length = is.readInt();
                    byte[] data = new byte[length];
                    is.readFully(data, 0, data.length);
                    request.setFileData(data);
                }

                Response response = requestHandler.handleRequest(request);
                String msg = String.valueOf(response.getCode());
                if (request.getType() == RequestType.PUT) {
                    msg += " " + response.getId();
                }
                os.writeUTF(msg);

                if (request.getType() == RequestType.GET && response.getCode() == 200) {
                    os.writeInt(response.getData().length);
                    os.write(response.getData());
                }

                if (request.getType() == RequestType.EXIT) {
                    App.closeSocket();
                }
            }
            socket.close();
        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request parseHeader(String msg) {
        Request request = null;

        String[] requestParam = msg.split("\\s+");
        switch (requestParam[0]) {
            case "PUT":
                request = parsePUT(requestParam);
                break;
            case "GET":
                request = parseGET(requestParam);
                break;
            case "DELETE":
                request = parseDELETE(requestParam);
                break;
            case "EXIT":
                request = new Request();
                request.setType(RequestType.EXIT);
                break;
            default:
                break;
        }
        return request;
    }

    private Request parsePUT(String[] params) {
        Request request = new Request();
        request.setType(RequestType.PUT);

        if (params.length > 1) {
            request.setFileName(params[1]);
        }
        return request;
    }

    private Request parseGET(String[] params) {
        Request request = new Request();
        request.setType(RequestType.GET);

        if (params.length > 2) {
            if (params[1].equals("BY_NAME")) {
                request.setFileName(params[2]);
            } else {
                request.setFileId(Long.parseLong(params[2]));
            }
        }
        return request;
    }

    private Request parseDELETE(String[] params) {
        Request request = new Request();
        request.setType(RequestType.DELETE);

        if (params.length > 2) {
            if (params[1].equals("BY_NAME")) {
                request.setFileName(params[2]);
            } else {
                request.setFileId(Long.parseLong(params[2]));
            }
        }
        return request;
    }
}
