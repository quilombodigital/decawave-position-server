package org.quilombo.postracker.heatmap;

import java.util.HashMap;
import java.util.Map;

public class GlobalHeatmap {

    //why the grid is a map and not an array? mostly because it can grow and have negative values, harder to rebuild on changes
    public Map<String, Integer> grid = new HashMap<>();
    int minx = Integer.MAX_VALUE;
    int miny = Integer.MAX_VALUE;
    int maxx, maxy;
    public int maxheat;
    public int minheat = Integer.MAX_VALUE;

    public void incrementAt(int xposition, int yposition) {
        markradius(xposition, yposition, 5);
        markradius(xposition, yposition, 4);
        markradius(xposition, yposition, 4);
        markradius(xposition, yposition, 3);
        markradius(xposition, yposition, 3);
        markradius(xposition, yposition, 3);
        markradius(xposition, yposition, 2);
        markradius(xposition, yposition, 2);
        markradius(xposition, yposition, 2);
        markradius(xposition, yposition, 2);
        markradius(xposition, yposition, 1);
        markradius(xposition, yposition, 1);
        markradius(xposition, yposition, 1);
        markradius(xposition, yposition, 1);
        markradius(xposition, yposition, 1);
    }

    void mark(int xposition, int yposition) {
        String key = "" + xposition + "|" + yposition;
        Integer value = grid.get(key);
        if (value == null) {
            value = 1;
        } else {
            value = value + 1;
        }
        miny = Math.min(miny, yposition);
        maxy = Math.max(maxy, yposition);
        minx = Math.min(minx, xposition);
        maxx = Math.max(maxx, xposition);
        maxheat = Math.max(maxheat, value);
        minheat = Math.min(minheat, value);
        grid.put(key, value);
    }

    void markradius(int x0, int y0, int radius) {
        int x = radius - 1;
        int y = 0;
        int dx = 1;
        int dy = 1;
        int err = dx - (radius << 1);
        while (x >= y) {
            mark(x0 + x, y0 + y);
            mark(x0 + y, y0 + x);
            mark(x0 - y, y0 + x);
            mark(x0 - x, y0 + y);
            mark(x0 - x, y0 - y);
            mark(x0 - y, y0 - x);
            mark(x0 + y, y0 - x);
            mark(x0 + x, y0 - y);
            if (err <= 0) {
                y++;
                err += dy;
                dy += 2;
            }
            if (err > 0) {
                x--;
                dx += 2;
                err += dx - (radius << 1);
            }
        }
    }

    public int[][] grid() {
        int sizey = maxy - miny + 1;
        int sizex = maxx - minx + 1;
        int[][] result = new int[sizey][sizex];
        for (int y = 0; y < sizey; y++) {
            for (int x = 0; x < sizex; x++) {
                String key = "" + (x + minx) + "|" + (y + miny);
                Integer value = grid.get(key);
                if (value != null) {
                    result[y][x] = value;
                }
            }
        }
        return result;
    }


}
