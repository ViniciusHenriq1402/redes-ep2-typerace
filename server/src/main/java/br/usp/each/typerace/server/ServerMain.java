package br.usp.each.typerace.server;

import org.java_websocket.server.WebSocketServer;

import java.util.HashMap;



public class ServerMain {


    private WebSocketServer server;


    public ServerMain(WebSocketServer server) {
        this.server = server;
    }


    public void init() {
        System.out.println("Iniciando servidor...");
        
        server.start();
    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        
    	int port = 8080;

    	if (args.length != 0){

    		try {
    			port = Integer.parseInt(args[0]);
    		}

    		catch (NumberFormatException ex){
    			ex.printStackTrace();
    		}    		
    	}


        WebSocketServer server = new Server(8080, new HashMap<>());

        ServerMain main = new ServerMain(server);

        main.init();
    }

}
