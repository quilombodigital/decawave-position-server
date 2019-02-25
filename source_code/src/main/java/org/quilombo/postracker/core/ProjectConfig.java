package org.quilombo.postracker.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProjectConfig {
    public String name;
    public double pixelsByMeter;
    public Offset mapOffset;
    public List<Anchor> anchors;

    public String serialPort;
    public int websocketPort;
    public int sendRate;

    public static class Offset {
        public int x;
        public int y;
    }

    public static class Anchor {
        public String name;
        public double x;
        public double y;
        public double z;
    }

    public static ProjectConfig load(String projectName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("projects/" + projectName + "/config.json"), ProjectConfig.class);
    }
}
