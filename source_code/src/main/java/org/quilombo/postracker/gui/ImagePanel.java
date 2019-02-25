package org.quilombo.postracker.gui;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JFrame {

    private BufferedImage image;

    public ImagePanel(BufferedImage image) throws Exception {
        this.image = image;
        setTitle("Image");
        add(new JLabel(new ImageIcon(image)));
        setSize(image.getWidth() + 20, image.getHeight() + 40);

    }


}