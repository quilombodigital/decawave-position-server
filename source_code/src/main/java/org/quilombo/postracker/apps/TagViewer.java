package org.quilombo.postracker.apps;

import org.quilombo.postracker.core.ProjectConfig;
import org.quilombo.postracker.gui.GuiUtil;
import org.quilombo.postracker.gui.TagsPanel;
import org.quilombo.postracker.model.Tag;
import org.quilombo.postracker.websocket.client.EventClient;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TagViewer extends JFrame {

    private BufferedImage image;

    private boolean isDirty = false;
    private Thread screenUpdater;
    private Map<String, Tag> tags = new HashMap<>();
    private ProjectConfig projectConfig;

    public TagViewer(ProjectConfig projectConfig) throws Exception {
        this.projectConfig = projectConfig;
        EventClient client = new EventClient();
        client.addListener((message) -> {
            String[] parts = message.split(",");
            Tag tag = new Tag(parts[1]);
            tag.setPos(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
            tags.put(parts[1], tag);
            isDirty = true;
        });
        client.connect();
        initUI();
        screenUpdater = new Thread(() -> {
            while (true) {
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

    private void initUI() {
        tagsPanel = new TagsPanel(projectConfig, tags);
        setTitle("Tag Viewer");
        add(tagsPanel);
        setSize(tagsPanel.image.getWidth() + 20, tagsPanel.image.getHeight() + 40);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public static void main(String args[]) throws Exception {
        String projectName = GuiUtil.chooseProject();
        TagViewer tagViewer = new TagViewer(ProjectConfig.load(projectName));
        tagViewer.setVisible(true);
        while (true) {
            Thread.sleep(1000);
        }
    }
}