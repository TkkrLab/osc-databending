/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import com.illposed.osc.OSCMessage;
import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mansoft.sseserver.SseEvent;

/**
 *
 * @author manson
 */
public class SendOSC extends HttpServlet {
    public static void sendOSC(SseOSCTask sseOSCTask, String name, String param, float value) {
//        System.out.println("SendOSC: param = " + param + ", value = " + value);
        Queue<SseEvent> queue = sseOSCTask.getQueue();
        queue.offer(new SseEvent(new String[] { name, param, String.valueOf(value) }, "sliderchange", null));
        OSCInputDevice inputDevice = sseOSCTask.getInputDevices().get(name);
        if (inputDevice != null)
        {
            inputDevice.setValue(param, value);
            
            Object args[] = new Object[1];
            args[0] = new Float(value);
            OSCMessage msg = new OSCMessage("/" + name + "/in/" + param, args);
            try {
                inputDevice.getOSCPortOut().send(msg);
            } catch (IOException ex) {
                Logger.getLogger(SendOSC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        sendOSC((SseOSCTask) getServletContext().getAttribute("SseOSCTask"), request.getParameter("name"), request.getParameter("param"), Float.parseFloat(request.getParameter("value")));
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
