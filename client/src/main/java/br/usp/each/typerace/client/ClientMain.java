package br.usp.each.typerace.client;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Scanner;

public class ClientMain {

    private Client client;

    public ClientMain(Client client) {
        this.client = client;
    }

    public void init() {
        System.out.println("Iniciando cliente.");

        client.connect();

        String str;
        Scanner sc = new Scanner(System.in);

        //manda msg pro servidor e o servidor printa no console
        while(!client.isClosed()){
            str = sc.nextLine();
            client.send(str);
            if(str.toLowerCase(Locale.ROOT).equals("sair")) break;
        }
        client.close();
        sc.close();
    }

    public int requestId() {
        System.out.println("Pedindo ID para o server");
        client.connect();
        synchronized(client) {
            try {
                client.wait();
            } catch (InterruptedException e) {}
        }
        System.out.println("ID de valor: " + client.getId() + " recebido");
        client.close(4000);
        return client.getId();
    }

    public static void main(String[] args) {
        int id = 0;
        // String removeMe = "ws://localhost:8080/idCliente";

        // Primeiro se conecta ao servidor para pedir um id
        try {
            URI uri = new URI("ws://localhost:8080/getId");
            Client client = new Client(uri);

            ClientMain main = new ClientMain(client);

            id = main.requestId();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Conecta com o id adquirido
        try {
            URI uri = new URI("ws://localhost:8080/" + Integer.toString(id));
            Client client = new Client(uri);
            client.setId(id);

            ClientMain main = new ClientMain(client);

            main.init();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
