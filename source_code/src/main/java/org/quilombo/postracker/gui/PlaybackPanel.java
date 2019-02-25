package org.quilombo.postracker.gui;

import org.quilombo.postracker.core.ProjectConfig;
import org.quilombo.postracker.model.Tag;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PlaybackPanel extends JFrame {

    private boolean isDirty = false;
    private Thread screenUpdater;
    private Map<String, Tag> tags = new HashMap<>();
    private ProjectConfig projectConfig;

    public PlaybackPanel(ProjectConfig projectConfig, File session) throws Exception {
        this.projectConfig = projectConfig;
        Thread player = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(session));
                    String line = reader.readLine();
                    long startMillis = System.currentTimeMillis();
                    long timeOffset = 0;
                    while (running && line != null) {
                        System.out.println(line);
                        line = reader.readLine();
                        if (line == null)
                            continue;
                        String[] parts = line.split(",");
                        Tag tag = new Tag("tag");

                        tag.setPos(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                        tags.put("tag", tag);
                        isDirty = true;
                        long elapsed = System.currentTimeMillis() - startMillis;
                        if (timeOffset == 0)
                            timeOffset = Long.parseLong(parts[0]);
                        long targetElapsed = Long.parseLong(parts[0]) - timeOffset;
                        while (elapsed < targetElapsed) {
                            Thread.sleep(10);
                            elapsed = System.currentTimeMillis() - startMillis;
                        }
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        player.setDaemon(true);
        player.start();

        initUI();
        screenUpdater = new Thread(() -> {
            running = true;
            while (running) {
                if (isDirty) {
                    isDirty = false;
                    SwingUtilities.invokeLater(() -> {
                        tagsPanel.repaint();
                    });
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        screenUpdater.setDaemon(true);
        screenUpdater.start();
    }

    TagsPanel tagsPanel;
    boolean running = true;

    private void initUI() {
        tagsPanel = new TagsPanel(projectConfig, tags);
        setTitle("Playback Viewer");
        setSize(tagsPanel.image.getWidth(), tagsPanel.image.getHeight());
        add(tagsPanel);
        setSize(tagsPanel.image.getWidth() + 20, tagsPanel.image.getHeight() + 40);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                running = false;
            }
        });
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}