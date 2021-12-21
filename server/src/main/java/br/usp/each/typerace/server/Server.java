package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Map;
import java.util.ArrayList;
import java.util.*;

public class Server extends WebSocketServer {

    private final Map<Integer, WebSocket> connections;
    private final LinkedList<Integer> idList = new LinkedList<>();
    private int counter = 0;

    private boolean gameStarted = false;
    private Game typeraceGame;
    private Map<Integer, Player> players = new HashMap<>(); 

    public Server(int port, Map<Integer, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        ByteBuffer id = assignId();
        conn.send(id); // Manda o id para o cliente
        conn.setAttachment(id.getInt(0)); 
        conn.send( "Bem vindo ao servidor!" ); //This method sends a message to the new client
        broadcast( "Nova conexao de jogador de id: " + conn.getAttachment() +
                "\nJogadores conectados: " + (++counter) ); //This method sends a message to all clients connected
        System.out.println( "new connection to " + conn.getRemoteSocketAddress() );
        connections.put( conn.getAttachment(), conn );

        sendInstructions(conn);
        players.put(conn.getAttachment(), new Player(conn.getAttachment(), typeraceGame.getWords()));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        int id = conn.getAttachment();
        idList.remove(Integer.valueOf(id));
        connections.remove(conn.getAttachment());
        System.out.println( "Jogador de id: " + id +
                " desconectou com codigo " + code + " info adicional: " + reason );
        broadcast( "Jogador de id: " + id + " desconectou" +
                "\nJogadores conectados: " + (--counter) );

        players.remove(conn.getAttachment());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

    	String messageLower = message.toLowerCase();

        if (!gameStarted){
            if (messageLower.equals("start")){
                messageLower = "";
                broadcast("GAME START");
                gameStarted = true;
                broadcast("PALAVRAS : " + typeraceGame.getSentence());  
                typeraceGame.countTime();                
            }

            else if (messageLower.equals("sair")){
                onClose(conn, 0, "Jogador saiu antes do jogo", false);
            }

            else
                broadcast("Cliente " + conn.getAttachment() + " : " + message);
        }

        if (gameStarted && !messageLower.isEmpty()) {

            Player pl = players.get(conn.getAttachment());
            if (pl.wordTyped(messageLower) == true){

                broadcast("GAME OVER");
                gameStarted = false;
                typeraceGame.stopTime();
                broadcastStatics();
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println( "Um erro ocorreu em " + conn.getRemoteSocketAddress()  + ":" + ex);

        connections.remove(conn.getAttachment());
        players.remove(conn.getAttachment());
    }

    @Override
    public void onStart() {
        System.out.println("Endereco: "+ getAddress());
        System.out.println("Servidor iniciado com sucesso!");

        typeraceGame = new Game();
    }

    // Retorna o menor id ainda não utilizado e o adiciona em idList
    public ByteBuffer assignId() {
        ByteBuffer ret = ByteBuffer.allocate(Integer.BYTES);
        int i = 1;

        while(!idList.isEmpty() && idList.contains(i))
            i++;

        idList.add(i);
        ret.putInt(0, i);
        return ret;
    }

    public void sendInstructions(WebSocket conn){

        conn.send(
        
            "\n\nPara iniciar o jogo, digite <start>\n" +
            "Para sair, digite <exit>\n" +
            "Para conversar com outros usuarios, digite a mensagem\n\n"
            
        );
    }

    public void broadcastStatics(){
        broadcast("========================================");
        broadcast("Duracao da partida : " + typeraceGame.timeElapsedSeconds() + "s\n");

        broadcast("Ranking :");
        List<Player> rankedPl = typeraceGame.ranking(players);
        int i = 0;
        for (Player pl : rankedPl){
            broadcast((++i) + ") " + "Jogador " + pl.getId() + " : " + pl.getCorrect() + " corretas, " + pl.getWrong() + " erradas");
        }
        broadcast("========================================");
    }

}
