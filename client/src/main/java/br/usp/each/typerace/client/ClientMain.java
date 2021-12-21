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
        // TODO: Implementar
        client.connect();

        String str;
        Scanner sc = new Scanner(System.in);

        //manda msg pro servidor e o servidor printa no console
        while(!client.isClosed()){
            str = sc.nextLine();
            if(str.toLowerCase(Locale.ROOT).equals("exit")) break;
            client.send(str);
        }
        client.close();

    }

    public static void main(String[] args) {
        /*
           FIXME: ID criado dinâmicamente na conexão, usar attachment para identificar cliente.
           Remover resource descriptor?
        */
        String removeMe = "ws://localhost:8080/idCliente";

        try {
            URI uri = new URI(removeMe);
            Client client = new Client(uri);

            ClientMain main = new ClientMain(client);

            main.init();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
