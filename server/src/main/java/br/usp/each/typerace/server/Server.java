package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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

    // Método para quando um cliente abre uma conexão com o servidor
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // Caso a conexão tenha sido para receber um id:
        if(conn.getResourceDescriptor().equals("/getId")) {
            // Reserva e manda o menor id disponível para o cliente
            ByteBuffer id = assignId();
            conn.send(id);
            return;
        }
        
        // Adiciona o cliente na lista de conexões
        connections.put( resourceDescriptorToInt(conn.getResourceDescriptor()), conn );
        // Adiciona o cliente na lista de jogadores
        players.put(resourceDescriptorToInt(conn.getResourceDescriptor()),
            new Player(resourceDescriptorToInt(conn.getResourceDescriptor()), typeraceGame.getWords()));

        conn.send( "Bem vindo ao servidor!" ); // Manda uma mensagem para o cliente que conectou
        // Printa informações do cliente que conectou para o servidor e todos os clientes
        System.out.println( "Nova conexao em " + conn.getRemoteSocketAddress() );
        broadcast( "Nova conexao de jogador de id: " + resourceDescriptorToInt(conn.getResourceDescriptor()) +
                "\nJogadores conectados: " + (++counter) );
        sendInstructions(conn); // Envia instruções de uso para o cliente
    }

    // Método para quando um cliente fecha a conexão com o servidor
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // Caso a conexão tenha sido para receber um id não faça nada
        if(conn.getResourceDescriptor().equals("/getId")) {
            return;
        }

        // Remove o id do cliente da lista de ids
        int id = resourceDescriptorToInt(conn.getResourceDescriptor());
        idList.remove(Integer.valueOf(id));
        // Remove o cliente da lista de conexões
        connections.remove(resourceDescriptorToInt(conn.getResourceDescriptor()));
        // Remove o cliente da lista de jogadores
        players.remove(resourceDescriptorToInt(conn.getResourceDescriptor()));

        // Printa informações do cliente desconectado para o servidor e todos os clientes
        System.out.println( "Jogador de id: " + id +
                " desconectou com codigo " + code + ". Info adicional: " + reason );
        broadcast( "Jogador de id: " + id + " desconectou" +
                "\nJogadores conectados: " + (--counter) );
    }

    // Método para quando um cliente manda uma mensagem para o serve
    @Override
    public void onMessage(WebSocket conn, String message) {

    	String messageLower = message.toLowerCase();

        // Caso não tenha um jogo ativo:
        if (!gameStarted){
            // Caso receba "start" inicie o jogo
            if (messageLower.equals("start")){
                messageLower = "";
                broadcast("=== GAME START ===");
                gameStarted = true;
                broadcast("PALAVRAS : " + typeraceGame.getSentence());  
                typeraceGame.countTime();                
            }
            // Caso receba "sair" desconecte o cliente
            else if (messageLower.equals("sair")){
                onClose(conn, 0, "Jogador saiu antes do jogo", false);
            }
            // Envia a mensagem do cliente para todos os jogadores
            else
                broadcast("Cliente " + resourceDescriptorToInt(conn.getResourceDescriptor()) + " : " + message);
        }

        // Caso tenha um jogo ativo:
        if (gameStarted && !messageLower.isEmpty()) {
            Player pl = players.get(resourceDescriptorToInt(conn.getResourceDescriptor()));
            // Caso algum jogador tenha escrito todas as palavras termina o jogo
            if (pl.wordTyped(messageLower) == true){
                broadcast("=== GAME OVER ===");
                gameStarted = false;
                typeraceGame.stopTime();
                broadcastStatics();
            }
        }
    }

    // Método que lida com erros
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println( "Um erro ocorreu em " + conn.getRemoteSocketAddress()  + ":" + ex);

        connections.remove(resourceDescriptorToInt(conn.getResourceDescriptor()));
        players.remove(resourceDescriptorToInt(conn.getResourceDescriptor()));
    }

    // Método de inicialização do servidor
    @Override
    public void onStart() {
        System.out.println("Endereco: "+ getAddress());
        System.out.println("Servidor iniciado com sucesso!");

        typeraceGame = new Game();
    }

    // Retorna e adiciona na lista o menor id disponível
    public ByteBuffer assignId() {
        ByteBuffer ret = ByteBuffer.allocate(Integer.BYTES);
        int i = 1;

        while(!idList.isEmpty() && idList.contains(i))
            i++;

        idList.add(i);
        ret.putInt(0, i);
        return ret;
    }

    // Envia instruções de uso para um cliente
    public void sendInstructions(WebSocket conn){

        conn.send(
        
            "\n\nPara iniciar o jogo, digite <start>\n" +
            "Para sair antes do inicio do jogo, digite <sair>\n" +
            "Para conversar com outros usuarios, digite a mensagem\n\n"
            
        );
    }

    // Envia estatísticas da partida para todos os clientes
    public void broadcastStatics(){
        broadcast("\n\n========================================");
        broadcast("Duracao da partida : " + typeraceGame.timeElapsedSeconds() + "s\n");

        broadcast("Ranking :");
        List<Player> rankedPl = typeraceGame.ranking(players);
        int i = 0;
        for (Player pl : rankedPl){
            broadcast((++i) + ") " + "Jogador " + pl.getId() + " : " + pl.getCorrect() + " corretas, " + pl.getWrong() + " erradas");
            pl.setCorrect(0); pl.setWrong(0); // Reseta pontuação dos jogadores para próxima partida
            pl.copyWords(typeraceGame.getWords()); // Copia novas palavras
        }
        broadcast("========================================");
    }

    // Devolve o id guardado no resource descriptor em forma de int
    public int resourceDescriptorToInt(String rd) {
        rd = rd.substring(1, rd.length()); // Remove o "/"
        return Integer.parseInt(rd);
    }   
}
