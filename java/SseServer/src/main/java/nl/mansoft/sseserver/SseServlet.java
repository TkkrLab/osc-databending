/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.sseserver;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author manson
 */
public class SseServlet extends HttpServlet implements Runnable {
//    int proef = 0;
    protected Thread syncThread;
    protected SseTask sseTask;
    protected Thread taskThread;
    protected ArrayBlockingQueue<SseEvent> queue = new ArrayBlockingQueue<SseEvent>(30);
    protected PropertyChangeSupport pcs;
    protected final ArrayList<Thread> threads = new ArrayList<Thread>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Thread currentThread = Thread.currentThread();
        synchronized (threads) {
            threads.add(currentThread);
        }
        response.setContentType("text/event-stream;charset=UTF-8");
        PrintWriter out = response.getWriter();
        SseQueue sseQueue = new SseQueue(currentThread);

        pcs.addPropertyChangeListener(sseQueue);
        Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, currentThread + ": starting");
        try {
            for (;;) {
                SseEvent sseEvent = sseQueue.take();
//                Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "sseEvent: {0}", sseEvent);
                sseEvent.writeTo(out);
                response.flushBuffer();
            }
        } catch (Exception ex) {
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, currentThread + ": " + ex);
        } finally {
            pcs.removePropertyChangeListener(sseQueue);
            synchronized (threads) {
                threads.remove(currentThread);
            }
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, currentThread + ": exiting");
        }
    }

    public void run() {
        Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "syncThread running");
        for (;;) {
            try {
                SseEvent sseEvent = queue.take();
                pcs.firePropertyChange("sse", null, sseEvent);
            } catch (InterruptedException ex) {
                Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "interrupted");
                break;
            }
        }
        synchronized (threads) {
            for (Thread thread: threads)
            {
                Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "interrupt thread " + thread);
                thread.interrupt();
            }
        }
        Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "syncThread exiting");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            String sseTaskClassName = config.getInitParameter("sseTask");
            if (sseTaskClassName == null)
                throw new ServletException("sseTask parameter is required");
            Class c = Class.forName(sseTaskClassName);
            if (!SseTask.class.isAssignableFrom(c))
                throw new ServletException("Class " + c.getName() + " is not a SseTask");
            pcs = new PropertyChangeSupport(this);
            syncThread = new Thread(this);
            syncThread.start();
            sseTask = (SseTask) c.newInstance();
            sseTask.init(config, queue);
        } catch (Exception ex) {
//            Logger.getLogger(SseServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    @Override
    public void destroy() {
        try {
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "disconnecting");
            sseTask.destroy();
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "interrupting");
            syncThread.interrupt();
            Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "joining");
            syncThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(SseServlet.class.getName()).log(Level.INFO, "Servlet Exiting");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
