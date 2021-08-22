package uk.ac.cam.cl.fjava.messages;

import uk.ac.cam.mef40.fjava.tick4star.ChatClientGUI;

public class OpenGUIMessage extends Message {
    String server;
    int port;

    public OpenGUIMessage(String server, int port) {
        this.server = server;
        this.port = port;
    }

    @Execute
    public void openGUI() {
        var chatClient = new ChatClientGUI(this.server, this.port);
    }
}
