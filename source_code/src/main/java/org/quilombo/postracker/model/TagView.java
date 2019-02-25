package org.quilombo.postracker.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TagView {
    private String projectName;
    public String id;
    public boolean recording;
    public BufferedWriter out;
    public long startMillis;

    public TagView(String projectName, String id) {
        this.projectName = projectName;
        this.id = id;
    }

    public void startRecording() {
        try {
            recording = true;
            String dir = "projects/" + projectName + "/sessions/" + id + "/";
            new File(dir).mkdirs();
            Date now = new Date();
            startMillis = now.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = dir + "session_" + format.format(now) + ".csv";
            out = new BufferedWriter(
                    new FileWriter(filename, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        recording = false;
        try {
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y, double z) {
        try {
            Date now = new Date();
            if (recording) {
                out.write("" + now.getTime() + "," + val(x) + "," + val(y) + "," + val(z) + "\n");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String val(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    public List<Session> sessions() {
        List<Session> sessions = new ArrayList<>();
        String dir = "projects/" + projectName + "/sessions/" + id;
        new File(dir).mkdirs();
        try {
            Files.list(Paths.get(dir)).forEach((path) -> {
                sessions.add(new Session(path.toFile()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public String toString() {
        String tmp = id;
        if (recording)
            tmp = id + " {RECORDING}";
        return tmp;
    }
}
