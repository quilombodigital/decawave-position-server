package org.quilombo.postracker.apps;

import com.fazecast.jSerialComm.SerialPort;
import org.quilombo.postracker.core.ServerConfig;
import org.quilombo.postracker.gui.GuiUtil;
import org.quilombo.postracker.model.TagsHolder;
import org.quilombo.postracker.serial.SerialReaderWebsocketSender;
import org.quilombo.postracker.websocket.server.EventServer;
import org.quilombo.postracker.websocket.server.TagListProvider;

import java.io.IOException;

public class PositionServer {

    public static PositionServer instance = new PositionServer();

    public TagsHolder tagsHolder;
    public EventServer eventServer;

    private PositionServer() {
    }

    public void startServer(ServerConfig config) throws InterruptedException, IOException {

        tagsHolder = new TagsHolder();

        SerialPort serial = SerialPort.getCommPort(config.serialPort);
        serial.setBaudRate(115200);
        SerialReaderWebsocketSender reader = new SerialReaderWebsocketSender(serial, tagsHolder);
        reader.setDaemon(true);
        reader.start();
        boolean connected = serial.openPort();
        if (!connected) {
            GuiUtil.alert("Could not open port " + config.serialPort);
            System.exit(1);
        }

        //TODO detect if connected...
        Thread.sleep(1000);
        serial.getOutputStream().write('\r');
        Thread.sleep(500);
        serial.getOutputStream().write('\r');
        //serial.getOutputStream().write("?\r".getBytes());
        Thread.sleep(3000);
        serial.getOutputStream().write("lep\r".getBytes());

        TagListProvider tagListProvider = () -> tagsHolder.getTags();
        eventServer = new EventServer(config.websocketPort, tagListProvider);
    }


    public static void main(String[] args) throws Exception {
        ServerConfig config = ServerConfig.load();
        PositionServer.instance.startServer(config);
    }


}
