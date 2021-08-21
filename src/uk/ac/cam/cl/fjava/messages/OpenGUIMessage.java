package uk.ac.cam.cl.fjava.messages;

import uk.ac.cam.mef40.fjava.tick4star.ChatClientGUI;

public class OpenGUIMessage extends Message {
    public OpenGUIMessage() {

    }

    @Execute
    public static void openGUI() {
        var chatClient = new ChatClientGUI();
    }
}
