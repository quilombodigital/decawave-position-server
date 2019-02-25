package org.quilombo.postracker.websocket.client;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebSocket
public class EventClientSocket {

    public List<ClientListener> listeners = new ArrayList<>();

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        //System.out.println("Message received:" + message);
        for (ClientListener listener : listeners)
            listener.onMessage(message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        System.out.println(session.getRemoteAddress().getHostString() + " connected!");
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println(session.getRemoteAddress().getHostString() + " closed!");
    }

    public interface ClientListener {
        void onMessage(String message);
    }

}