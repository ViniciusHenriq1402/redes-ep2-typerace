package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;

// https://coderedirect.com/questions/539962/websocket-handshake-failed-in-java

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    	connections.add(conn);

        // TODO: Implementar
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    	
    	connections.remove(conn);

        // TODO: Implementar
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

    	System.out.println("Message : " + message);
    	conn.send(message);

    	// TODO: Implementar
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    	ex.printStackTrace();

    	connections.remove(conn);

        // TODO: Implementar
    }

    @Override
    public void onStart() {
        // TODO: Implementar
    }
}
