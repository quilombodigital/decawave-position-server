package org.quilombo.postracker.gui;

import org.quilombo.postracker.core.ProjectConfig;
import org.quilombo.postracker.model.Tag;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TagsPanel extends JPanel {

    public BufferedImage image;

    private Map<String, Tag> tags;
    private ProjectConfig projectConfig;

    public TagsPanel(ProjectConfig projectConfig, Map<String, Tag> tags) {
        this.projectConfig = projectConfig;
        this.tags = tags;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.white);

        try {
            image = ImageIO.read(new File("projects/" + projectConfig.name + "/map.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);
        for (ProjectConfig.Anchor anchor : projectConfig.anchors) {
            int x1 = (int) (metersToPixel(anchor.x) + projectConfig.mapOffset.x);
            int y1 = getHeight() - (int) (metersToPixel(anchor.y) + projectConfig.mapOffset.y);
            g.setColor(Color.red);
            g.fillOval(x1 - 5, y1 - 5, 10, 10);
            g.drawString(anchor.name, x1 + 10, y1);
        }
        for (Tag tag : tags.values()) {
            int x1 = (int) (metersToPixel(tag.getX()) + projectConfig.mapOffset.x);
            int y1 = (int) getHeight() - (int) (metersToPixel(tag.getY()) + projectConfig.mapOffset.y);
            g.setColor(Color.blue);
            g.fillOval(x1 - 5, y1 - 5, 10, 10);
            g.drawString(tag.getId(), x1 + 10, y1);
        }
    }

    public double metersToPixel(double meter) {
        return (projectConfig.pixelsByMeter * meter);
    }


}