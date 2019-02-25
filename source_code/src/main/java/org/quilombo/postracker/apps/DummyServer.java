package org.quilombo.postracker.apps;

import org.quilombo.postracker.core.ProjectConfig;
import org.quilombo.postracker.gui.GuiUtil;
import org.quilombo.postracker.model.Tag;
import org.quilombo.postracker.websocket.server.EventServer;
import org.quilombo.postracker.websocket.server.TagListProvider;

import java.util.ArrayList;
import java.util.List;

public class DummyServer {

    Thread thread;

    public EventServer eventServer;
    ProjectConfig config;

    private DummyServer() throws Exception {
        String projectName = GuiUtil.chooseProject();
        config = ProjectConfig.load(projectName);
    }

    public void startServer() {

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double maxX = 0;
                    double maxY = 0;
                    double maxZ = 0;
                    for (ProjectConfig.Anchor anchor : config.anchors) {
                        maxX = Math.max(maxX, anchor.x);
                        maxY = Math.max(maxY, anchor.y);
                        maxZ = Math.max(maxZ, anchor.z);
                    }
                    int[] xdir = new int[tags.size()];
                    int[] ydir = new int[tags.size()];
                    double[] xpos = new double[tags.size()];
                    double[] ypos = new double[tags.size()];
                    for (int t = 0; t < tags.size(); t++) {
                        xpos[t] = Math.random() * maxX;
                        ypos[t] = Math.random() * maxY;
                    }
                    long last[] = new long[tags.size()];
                    long waitTime[] = new long[tags.size()];
                    while (true) {
                        long now = System.currentTimeMillis();

                        for (int t = 0; t < tags.size(); t++) {
                            long elapsed = now - last[t];
                            if (elapsed > waitTime[t]) {
                                last[t] = now;
                                xdir[t] = ((int) (Math.random() * 2.0)) == 0 ? 1 : -1;
                                ydir[t] = ((int) (Math.random() * 2.0)) == 0 ? 1 : -1;
                                waitTime[t] = (long) (Math.random() * 2000);
                            }
                        }
                        for (int t = 0; t < tags.size(); t++) {
                            xpos[t] = xpos[t] + (xdir[t] * 0.01);
                            ypos[t] = ypos[t] + (ydir[t] * 0.01);
                            xpos[t] = Math.max(0, xpos[t]);
                            ypos[t] = Math.max(0, ypos[t]);
                            xpos[t] = Math.min(maxX, xpos[t]);
                            ypos[t] = Math.min(maxY, ypos[t]);
                            tags.get(t).setPos(xpos[t], ypos[t], Math.random() * maxZ);
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        TagListProvider tagListProvider = new TagListProvider() {
            @Override
            public List<Tag> getTags() {
                return tags;
            }
        };
        eventServer = new EventServer(config.websocketPort, tagListProvider);
    }

    public static void main(String[] args) throws Exception {

        new DummyServer().startServer();

    }


}
