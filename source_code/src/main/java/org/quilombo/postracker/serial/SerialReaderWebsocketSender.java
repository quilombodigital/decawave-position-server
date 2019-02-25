package org.quilombo.postracker.serial;

import com.fazecast.jSerialComm.SerialPort;
import org.quilombo.postracker.model.TagsHolder;

public class SerialReaderWebsocketSender extends Thread {
    SerialPort serial;
    private TagsHolder tagsHolder;

    public SerialReaderWebsocketSender(SerialPort serial, TagsHolder tagsHolder) {
        this.serial = serial;
        this.tagsHolder = tagsHolder;
    }

    byte[] readBuffer = new byte[1];

    public void run() {
        StringBuilder line = new StringBuilder();
        try {
            while (true) {

                while (serial.bytesAvailable() == 0)
                    Thread.sleep(1);

                int readed = serial.readBytes(readBuffer, 1);
                if (readed != 1)
                    continue;

                if ('\r' == (char) readBuffer[0]) {
                    String[] parts = line.toString().split(",");
                    if (parts.length > 0) {
                        if ("POS".equals(parts[0])) {
                            String id = parts[2];
                            double x = Double.parseDouble(parts[3]);
                            double y = Double.parseDouble(parts[4]);
                            double z = Double.parseDouble(parts[5]);
                            tagsHolder.setPos(id, x, y, z);
                        }
                    }
                    //System.out.println(line.toString());
                    line = new StringBuilder();
                } else {
                    if ('\n' == (char) readBuffer[0])
                        continue;
                    line.append((char) readBuffer[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}