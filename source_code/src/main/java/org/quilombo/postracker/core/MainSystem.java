package org.quilombo.postracker.core;

import org.quilombo.postracker.model.Session;
import org.quilombo.postracker.model.TagView;
import org.quilombo.postracker.websocket.client.EventClient;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainSystem {
    public DefaultListModel<TagView> items = new DefaultListModel<>();
    public Map<String, TagView> itemMap = new HashMap<>();
    public DefaultListModel<Session> sessions = new DefaultListModel<>();

    public MainSystem(ProjectConfig projectConfig) throws Exception {
        Files.list(Paths.get("projects/" + projectConfig.name + "/sessions")).forEach((path) -> {
            TagView tagView = getTagView(projectConfig.name, path.toFile().getName());
        });
        EventClient client = new EventClient();
        client.addListener((message) -> {
            String parts[] = message.split(",");
            String id = parts[1];
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            double z = Double.parseDouble(parts[4]);
            TagView tagView = getTagView(projectConfig.name, id);
            if (tagView.recording)
                tagView.setPosition(x, y, z);
        });
        client.connect();
    }

    public TagView getTagView(String projectName, String tagId) {
        TagView tagItem = itemMap.get(tagId);
        if (tagItem == null) {
            tagItem = new TagView(projectName, tagId);
            items.add(0, tagItem);
            itemMap.put(tagId, tagItem);
        }
        return tagItem;
    }


}
