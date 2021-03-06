# Decawave Position Server
#### This is a simple websocket server designed to serve decawave´s tags positions, by using a tag listener connected to the computer.

Its main purpose is to work as a generic proxy, that allows different types of clients to connect using websocket.
For testing purposes, there is a **dummy_server**, that will simulate some tags, and a **tag_viewer** app, that will show current tag positions being served.
The **position_server** app is the main server, that will connect to the serial port and transmit data to all connected websocket clients.
Finally, the **position_recorder** is an app that allow you to record tag sessions, playback and show area heatmaps and time heatmaps. You can check how it works by using the dummy_server app.
At the start of every app, it asks for the "project". This is the name of the folder inside the projects folder, that holds the configuration files and the saved sessions of this project. I provided one example that should be self-explanatory. Basically when you start a new project you have to create a new folder and change config.json accordingly.

For the tests I used the decawave´s android app to configure the grid.

I use intellij ide to code, and I provided a jar binary, but you can build from command line using maven and assembly:single target

##### Important Note: This is the first *draft* of this app, and the main reason I did it is because I am helping my wife with her PHD in arts, in a project for Audio augmented reality. The other part of this research will be released later. You can modify and use this code in any way you want it and if you need any help or have ideas for improvement just drop me a line. This implementation is truly naive, but functional. (like any first prototype should be) 8P

Hope this helps!

Ricardo Andere de Mello (gandhi)
quilombodigital@gmail.com

![Alt text](docs/images/screen.png?raw=true "Screen")

