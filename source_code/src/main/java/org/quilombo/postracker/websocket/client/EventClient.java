package org.quilombo.postracker.websocket.client;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;

public class EventClient {
    private WebSocketClient client;
    private EventClientSocket socket;

    public EventClient() {
        client = new WebSocketClient();
        socket = new EventClientSocket();
    }

    public void connect() throws Exception {
        client.start();
        String destUri = "ws://localhost:8090/events/";
        URI echoUri = new URI(destUri);
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        client.connect(socket, echoUri, request);
    }

    public void addListener(EventClientSocket.ClientListener listener) {
        socket.listeners.add(listener);
    }

    public static void main(String[] args) throws Exception {
        EventClient client = new EventClient();
        client.addListener((message) -> System.out.println("RECEBI: " + message));
        client.connect();
        while (true) {
            Thread.sleep(1000);
        }
    }

}
