package org.quilombo.postracker.apps;

import org.quilombo.postracker.core.MainSystem;
import org.quilombo.postracker.core.ProjectConfig;
import org.quilombo.postracker.gui.GuiUtil;
import org.quilombo.postracker.gui.ImagePanel;
import org.quilombo.postracker.gui.PlaybackPanel;
import org.quilombo.postracker.heatmap.Heatmap;
import org.quilombo.postracker.model.Session;
import org.quilombo.postracker.model.TagView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PositionRecorder {
    private JList tagsList;
    private JButton startSessionButton;
    private JButton stopSessionButton;
    private JButton removeSessionButton;
    private JList sessionsList;
    private JButton timeHeatmapButton;
    private JButton playbackButton;
    private JPanel mainpanel;
    private JButton sessionTimeHeatmapButton;
    private JButton areaHeatmapButton;
    private JButton sessionAreaHeatmapButton;
    private ProjectConfig projectConfig;
    private MainSystem system;

    public PositionRecorder(String projectName) throws Exception {
        projectConfig = ProjectConfig.load(projectName);
        system = new MainSystem(projectConfig);
        TagViewer viewer = new TagViewer(projectConfig);
        viewer.setVisible(true);
    }


    public void init() {
        sessionTimeHeatmapButton.setEnabled(false);
        sessionAreaHeatmapButton.setEnabled(false);
        playbackButton.setEnabled(false);
        removeSessionButton.setEnabled(false);
        stopSessionButton.setEnabled(false);
        startSessionButton.setEnabled(false);
        tagsList.setModel(system.items);
        sessionsList.setModel(system.sessions);
        startSessionButton.addActionListener(e -> {
            ((TagView) tagsList.getSelectedValue()).startRecording();
            SwingUtilities.invokeLater(() -> {
                        tagsList.repaint();
                        updateSessionList();
                        updateButtonsState();
                    }
            );
        });

        stopSessionButton.addActionListener(e -> {
            ((TagView) tagsList.getSelectedValue()).stopRecording();
            SwingUtilities.invokeLater(() -> {
                        tagsList.repaint();
                        updateSessionList();
                        updateButtonsState();
                    }
            );
        });

        tagsList.addListSelectionListener(e -> {
            updateSessionList();
            updateButtonsState();
        });

        sessionsList.addListSelectionListener(e -> {
            boolean sessionButtonsEnabled = sessionsList.getSelectedIndices().length != 0;
            removeSessionButton.setEnabled(sessionButtonsEnabled);
            playbackButton.setEnabled(sessionButtonsEnabled);
            sessionTimeHeatmapButton.setEnabled(sessionButtonsEnabled);
            sessionAreaHeatmapButton.setEnabled(sessionButtonsEnabled);
        });

        playbackButton.addActionListener(evt -> {
            try {
                Session session = ((Session) sessionsList.getSelectedValue());
                PlaybackPanel tagViewer = new PlaybackPanel(projectConfig, session.sessionFile);
                tagViewer.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        timeHeatmapButton.addActionListener(e -> {
            try {
                showHeatMap(getSessionFiles(), false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        areaHeatmapButton.addActionListener(e -> {
            try {
                showHeatMap(getSessionFiles(), true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        sessionTimeHeatmapButton.addActionListener(e -> {
            try {
                List<File> sessionFiles = new ArrayList<>();
                Session session = ((Session) sessionsList.getSelectedValue());
                sessionFiles.add(session.sessionFile);
                showHeatMap(sessionFiles, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
        sessionAreaHeatmapButton.addActionListener(e -> {
            try {
                List<File> sessionFiles = new ArrayList<>();
                Session session = ((Session) sessionsList.getSelectedValue());
                sessionFiles.add(session.sessionFile);
                showHeatMap(sessionFiles, true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
        removeSessionButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to remove this session?", "Remove Session?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                Session session = ((Session) sessionsList.getSelectedValue());
                session.sessionFile.delete();
                updateSessionList();
            }

        });
    }

    private List<File> getSessionFiles() throws Exception {
        List<File> sessionFiles = new ArrayList<>();
        Files.list(Paths.get("projects/" + projectConfig.name + "/sessions")).forEach((path) -> {
            try {
                Files.list(path).forEach((f) -> {
                    sessionFiles.add(f.toFile());
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return sessionFiles;
    }

    private void showHeatMap(List<File> sessionFiles, boolean oncePerSession) throws Exception {
        BufferedImage background = ImageIO.read(new File("projects/" + projectConfig.name + "/map.png"));
        Heatmap heatmap = new Heatmap();
        BufferedImage heatmapImage = heatmap.generate(projectConfig, background, sessionFiles, oncePerSession);
        ImagePanel panel = new ImagePanel(heatmapImage);
        panel.setVisible(true);
    }

    private void updateSessionList() {
        system.sessions.clear();
        ((TagView) tagsList.getSelectedValue()).sessions().forEach((session) -> {
            system.sessions.add(0, session);
        });
        sessionsList.repaint();
    }

    public void updateButtonsState() {
        if (((TagView) tagsList.getSelectedValue()).recording) {
            stopSessionButton.setEnabled(true);
            startSessionButton.setEnabled(false);
        } else {
            stopSessionButton.setEnabled(false);
            startSessionButton.setEnabled(true);
        }
    }

    public static void main(String[] args) throws Exception {
        String projectName = GuiUtil.chooseProject();
        PositionRecorder panel = new PositionRecorder(projectName);
        panel.init();
        JFrame frame = new JFrame("Position Recorder");
        frame.setContentPane(panel.mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);

    }
}
