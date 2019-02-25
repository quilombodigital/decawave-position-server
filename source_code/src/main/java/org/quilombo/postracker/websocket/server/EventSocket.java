package org.quilombo.postracker.websocket.server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/events/")
public class EventSocket {
    EventSender sender;
    private TagListProvider tagListProvider;


    public EventSocket(TagListProvider tagListProvider) {
        this.tagListProvider = tagListProvider;
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        System.out.println("Socket Connected: " + session);
        sender = new EventSender(tagListProvider, session);
        sender.setDaemon(true);
        sender.start();
    }

    @OnMessage
    public void onWebSocketText(String message) {
        System.out.println("Received TEXT message: " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket Closed: " + reason);
        if (sender != null)
            sender.shutdown();
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}