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

    // Método para quando o cliente abre uma conexão com um servidor
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Nova conexao aberta");
    }

    // Método para quando o cliente recebe uma mensagem do servidor
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    /* Método para quando o cliente recebe uma mensagem em forma de 
       ByteBuffer do servidor (utilizado para receber id) */
    @Override
    public void onMessage(ByteBuffer message) {
        id = message.getInt(0);
        // Notifica que o id foi recebido
        synchronized(this) {
            this.notify();
        }
    }

    // Método para quando o cliente fecha a conexão com um servidor
    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Caso a conexao fechada era para pedir um ID nao printe
        if(code != 4000)
            System.out.println("Conexao fechada com codigo de saida " + code + " Info adicional: " + reason);
    }

    // Método que lida com erros
    @Override
    public void onError(Exception ex) {
        System.err.println("Ocorreu um erro:" + ex);
        System.exit(1);
    }

    // Método que retorna o id do cliente
    public int getId() {
        return id;
    }

    // Método que altera o id do cliente
    public void setId(int id) {
        this.id = id;
    }

}
