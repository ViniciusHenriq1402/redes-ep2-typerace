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
        System.out.println("Nova conexao aberta");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        id = message.getInt(0);
        synchronized(this) {
            this.notify();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Caso a conexao fechada era para pedir um ID nao printe
        if(code != 4000)
            System.out.println("Conexao fechada com codigo de saida " + code + " Info adicional: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Ocorreu um erro:" + ex);
        System.exit(1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
