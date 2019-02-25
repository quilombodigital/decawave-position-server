package org.quilombo.postracker.websocket.server;

import org.quilombo.postracker.model.Tag;

import javax.websocket.Session;
import java.io.IOException;

public class EventSender extends Thread {

    private boolean running = true;
    private boolean finished = false;
    private TagListProvider tagListProvider;
    private Session session;
    private long last = System.currentTimeMillis();

    public EventSender(TagListProvider tagListProvider, Session session) {
        this.tagListProvider = tagListProvider;
        this.session = session;
    }

    public void run() {
        try {
            while (running) {
                long now = System.currentTimeMillis();
                long elapsed = now - last;
                if (elapsed >= 100) {
                    try {
                        //session.getBasicRemote().sendText("POS,id1," + tmp + ",0.0,0.0");
                        for (Tag tag : tagListProvider.getTags()) {
                            session.getBasicRemote().sendText("POS," + tag.getId() + "," + tag.getX() + "," + tag.getY() + "," + tag.getZ() + "");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    last = now;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            finished = true;
        }
    }

    public void shutdown() {
        running = false;
        while (!finished) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
            }
        }
    }
}
