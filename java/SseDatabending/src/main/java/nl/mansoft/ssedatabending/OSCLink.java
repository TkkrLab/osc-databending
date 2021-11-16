/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mansoft.sseserver.SseEvent;

/**
 *
 * @author manson
 */
public class OSCLink extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SseOSCTask sseOSCTask = (SseOSCTask) getServletContext().getAttribute("SseOSCTask");
        if (sseOSCTask != null)
        {
            if (request.getQueryString() == null)
            {
                response.setContentType("text/xml;charset=UTF-8");
                PrintWriter out = response.getWriter();
                try
                {
                    out.print(sseOSCTask.linksToXML());
                }
                finally
                {
                    out.close();
                }                
            }
            else
            {
                String outputDevice = request.getParameter("outputdevice");
                String outputParameter = request.getParameter("outputparameter");
                String inputDevice = request.getParameter("inputdevice");
                String inputParameter = request.getParameter("inputparameter");
                if (outputDevice != null && outputParameter != null && inputDevice != null && inputParameter != null)
                {
                    OSCDeviceParameter outputDeviceParameter = new OSCDeviceParameter(outputDevice, outputParameter);
                    Map<OSCDeviceParameter, Set<OSCDeviceParameter>> links = sseOSCTask.getLinks();
                    Set<OSCDeviceParameter> outputlinks = links.get(outputDeviceParameter);
                    String actionParameter = request.getParameter("action");
                    Queue<SseEvent> queue = sseOSCTask.getQueue();
                    if (actionParameter != null && actionParameter.equals("delete"))
                    {
                        if (outputlinks != null)
                        {
                            outputlinks.remove(new OSCDeviceParameter(inputDevice, inputParameter));
                        }
                        queue.offer(new SseEvent(new String[] { outputDevice, outputParameter, inputDevice, inputParameter }, "DeletedLink", null));
                    }
                    else
                    {
                        if (outputlinks == null)
                        {
                            outputlinks = new LinkedHashSet<OSCDeviceParameter>();
                            links.put(outputDeviceParameter, outputlinks);
                        }
                        outputlinks.add(new OSCDeviceParameter(inputDevice, inputParameter));
                        queue.offer(new SseEvent(new String[] { outputDevice, outputParameter, inputDevice, inputParameter }, "AddedLink", null));
                    }
                }
            }
        }
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
