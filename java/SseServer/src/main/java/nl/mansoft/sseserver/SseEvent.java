/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.sseserver;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author manson
 */
public class SseEvent {
    private String[] data;
    private String eventName;
    private String lastEventID;

    public SseEvent(String[] data, String eventName, String lastEventID)
    {
        this.data = data;
        this.eventName = eventName;
        this.lastEventID = lastEventID;
    }

    public SseEvent(String data) {
        this(new String[] { data }, null, null);
    }

    public SseEvent(String data, String eventName) {
        this(new String[] { data }, eventName, null);
    }

    @Override
    public String toString()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        writeTo(pw);
        return sw.toString();
    }

    public void writeTo(PrintWriter out)
    {
        if (eventName != null)
            out.println("event: " + eventName);
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
                out.println("data: " + data[i]);
        }
        if (lastEventID != null)
            out.println("id: " + lastEventID);
        out.println();
    }
}
