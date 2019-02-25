package org.quilombo.postracker.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ServerConfig {

    public String serialPort;
    public int websocketPort;
    public int sendRate;


    public static ServerConfig load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("server_config.json"), ServerConfig.class);
    }
}
