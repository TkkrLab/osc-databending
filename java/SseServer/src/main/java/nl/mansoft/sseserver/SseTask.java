/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.sseserver;

import java.util.Queue;
import javax.servlet.ServletConfig;

/**
 *
 * @author manson
 */
public interface SseTask {
    public void init(ServletConfig config, Queue<SseEvent> queue);
    public void destroy();
}
