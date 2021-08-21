package uk.ac.cam.mef40.fjava.tick4star;

import java.util.HashSet;
import java.util.Set;

public class MultiQueue<T> {
    private Set<MessageQueue<T>> outputs = new HashSet<>(); // TODO

    public void register(MessageQueue<T> q) {
        synchronized (this) {
            outputs.add(q);
        }
    }

    public void deregister(MessageQueue<T> q) {
        synchronized (this) {
            outputs.remove(q);
        }
    }

    public void put(T message) {
        synchronized (this) {
            for (MessageQueue<T> output : outputs) {
                output.put(message);
            }
        }
    }
}