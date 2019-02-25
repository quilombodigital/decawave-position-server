package org.quilombo.postracker.model;

import java.io.File;

public class Session {
    public File sessionFile;

    public Session(File sessionFile) {
        this.sessionFile = sessionFile;
    }

    @Override
    public String toString() {
        return sessionFile.getName();
    }
}
