package uk.ac.cam.mef40.fjava.tick4star;


public class SafeMessageQueue<T> implements MessageQueue<T> {
    private static class Link<L> {
        L val;
        Link<L> next;

        Link(L val) {
            this.val = val;
            this.next = null;
        }
    }

    private Link<T> first = null;
    private Link<T> last = null;

    public synchronized void put(T val) {
        Link<T> link = new Link<>(val);
        if (first == null) {
            // If the list is currently empty
            first = link;
            last = link;
        } else {
            last.next = link;
            last = link;
        }

        this.notify();
    }

    public synchronized T take() {
        while (first == null) {
            try {
                this.wait();
            } catch (InterruptedException ie) {
                // Ignored exception
            }
        }
        T val = first.val;
        first = first.next;

        return val;
    }
}
