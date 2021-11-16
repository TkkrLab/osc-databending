/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.mansoft.sseserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manson
 */
public class SseQueue implements PropertyChangeListener {
    private ArrayBlockingQueue<SseEvent> queue;
    private Thread thread;

    public SseQueue(Thread thread) {
        this.thread = thread;
        queue = new ArrayBlockingQueue<SseEvent>(256);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!queue.offer((SseEvent) evt.getNewValue())) {
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "Buffer full! interrupting thread " + thread);
            thread.interrupt();
        }
    }

    public SseEvent take() throws InterruptedException {
        return queue.take();
    }
}
