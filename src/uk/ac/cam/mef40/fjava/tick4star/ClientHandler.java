package uk.ac.cam.mef40.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClientHandler {
    private Socket socket;
    private MultiQueue<Message> multiQueue;
    private String nickname;
    private MessageQueue<Message> clientMessages;

    public ClientHandler(Socket s, MultiQueue<Message> q) {
        socket = s;
        multiQueue = q;
        clientMessages = new SafeMessageQueue<>();
        multiQueue.register(clientMessages);

        // Generate 5-digit default nickname
        nickname = "Anonymous";
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            nickname += r.nextInt(10);
        }

        StatusMessage connectedMsg = new StatusMessage(nickname + " connected from " + socket.getInetAddress().getHostName());
        q.put(connectedMsg);

        Thread incoming = new Thread() {
            @Override
            public void run() {
                try {
                    InputStream is = s.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);

                    while (true) {
                        Message msg = (Message)ois.readObject();

                        if (msg instanceof ChangeNickMessage) {
                            ChangeNickMessage cnm = (ChangeNickMessage)msg;
                            StatusMessage out = new StatusMessage(nickname + " is now known as " + cnm.name + ".");

                            nickname = cnm.name;
                            q.put(out);
                        } else if (msg instanceof ChatMessage) {
                            ChatMessage cm = (ChatMessage)msg;
                            RelayMessage out = new RelayMessage(nickname, cm);

                            q.put(out);
                        }
                        // else ignore other message types
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // call deregister on multiQ.
                    q.deregister(clientMessages);
                    // create and send a statusmessage to all remaining clients
                    StatusMessage out = new StatusMessage(nickname + " has disconnected.");
                    q.put(out);
                    return;
                }
            }
        };
        incoming.start();

        Thread outgoing = new Thread() {
            @Override
            public void run() {
                try {
                    OutputStream os = s.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    // Send ChatClientGUI Class (New Message Type)
                    Class gui = ChatClientGUI.class;
                    InputStream is2 = gui.getClassLoader().getResourceAsStream(gui.getName().replaceAll("\\.", "/") + ".class");
                    clientMessages.put(new NewMessageType(gui.getName(), is2.readAllBytes()));

                    // Send OpenGUIMessage Class (New Message Type)
                    Class openGuiMsg = OpenGUIMessage.class;
                    InputStream is3 = openGuiMsg.getClassLoader().getResourceAsStream(openGuiMsg.getName().replaceAll("\\.", "/") + ".class");
                    clientMessages.put(new NewMessageType(openGuiMsg.getName(), is3.readAllBytes()));

                    // Send OpenGUIMessage
                    clientMessages.put(new OpenGUIMessage());

                    while (true) {
                        Message msg = clientMessages.take();
                        oos.writeObject(msg);
                    }
                } catch (IOException e) {
                    q.deregister(clientMessages);
                    StatusMessage out = new StatusMessage(nickname + " has disconnected.");
                    q.put(out);
                    return;
                }
            }
        };
        outgoing.start();
    }
}
