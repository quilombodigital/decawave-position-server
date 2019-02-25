package org.quilombo.postracker.heatmap;

import org.quilombo.postracker.core.ProjectConfig;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Heatmap {

    public Heatmap() {
    }

    public BufferedImage generate(ProjectConfig config, BufferedImage background, List<File> sessionFiles, boolean oncePerSession) throws IOException {
        GlobalHeatmap globalHeatmap = new GlobalHeatmap();

        sessionFiles.stream().forEach((sessionFile) -> {
            System.out.println(sessionFile.getName());
            Set<String> pastKeys = new HashSet<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
                String line = "";
                while (line != null) {
                    line = reader.readLine();
                    if (line == null)
                        continue;
                    String[] parts = line.split(",");
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    int xposition = (int) (x * 10.0);
                    int yposition = (int) (y * 10.0);
                    String key = "" + xposition + "|" + yposition;
                    if (oncePerSession)
                        if (pastKeys.contains(key))
                            continue;
                        else
                            pastKeys.add(key);
                    globalHeatmap.incrementAt(xposition, yposition);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        int[][] grid = globalHeatmap.grid();
        System.out.println();

        Color[] gradient = Gradient.createMultiGradient(new Color[]{new Color(255,255,255,0), Color.lightGray,
                Color.green, Color.yellow, Color.orange, Color.red, Color.magenta}, 500);
        if (oncePerSession)
            gradient = Gradient.createMultiGradient(new Color[]{new Color(255,255,255,0), Color.lightGray,
                    Color.cyan, Color.blue}, 500);

        Graphics2D g2d = (Graphics2D) background.getGraphics();
        double space = (config.pixelsByMeter / 10.0);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                int value = grid[y][x] - globalHeatmap.minheat + 1;
                int gradientIndex = (value * (gradient.length - 1) / (globalHeatmap.maxheat - globalHeatmap.minheat + 1));
                System.out.print("" + gradientIndex + ",");
                Color gcolor = gradient[gradientIndex];
                g2d.setColor(gcolor);
                int x1 = ((int) ((x + globalHeatmap.minx) * space)) + config.mapOffset.x;
                int y1 = background.getHeight() - (((int) ((y + globalHeatmap.miny) * space)) + config.mapOffset.y);
                int sy = (int) (((y + 1) * space) - (y * space)) + 1;
                int sx = (int) (((x + 1) * space) - (x * space)) + 1;
                g2d.fillRect(x1, y1, sx, sy);

            }
            System.out.println();
        }
        return background;
    }


}
