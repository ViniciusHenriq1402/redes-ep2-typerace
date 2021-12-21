package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class Client extends WebSocketClient {

    private int id;

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("New connection opened");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        id = message.getInt(0);
        
        System.out.println("O ID do cliente é: " + id);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason);
        System.exit(0);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred:" + ex);
        System.exit(1);
    }

    public int getId() {
        return id;
    }

}
