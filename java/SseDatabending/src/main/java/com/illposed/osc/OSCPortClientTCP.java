package com.illposed.osc;

import com.illposed.osc.utility.OSCByteArrayToJavaConverter;
import com.illposed.osc.utility.OSCPacketDispatcher;
import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OSCPortOut is the class that sends OSC messages to a specific address and port.
 *
 * To send an OSC message, call send().
 * <p>
 * An example based on com.illposed.osc.test.OSCPortTest::testMessageWithArgs() :
 * <pre>
 OSCPort sender = new OSCPort();
 Object args[] = new Object[2];
 args[0] = new Integer(3);
 args[1] = "hello";
 OSCMessage msg = new OSCMessage("/sayhello", args);
 try {
 sender.send(msg);
 } catch (Exception e) {
 showError("Couldn't send");
 }
 * </pre>
 * <p>
 * Copyright (C) 2004-2006, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 * <p>
 * See license.txt (or license.rtf) for license information.
 *
 * @author Chandrasekhar Ramakrishnan
 * @version 1.0
 */
public class OSCPortClientTCP implements Runnable {

    protected Socket socket;
    protected InputStream in;
    protected OutputStream out;

    protected boolean isListening;
    protected OSCByteArrayToJavaConverter converter = new OSCByteArrayToJavaConverter();
    protected OSCPacketDispatcher dispatcher = new OSCPacketDispatcher();

    /**
     * Create an OSCPortOutTCP that sends to newAddress, newPort
     * @param newAddress InetAddress
     * @param newPort int
     * @throws java.io.IOException
     */
    public OSCPortClientTCP(InetAddress newAddress, int newPort) throws IOException {
        socket = new Socket(newAddress, newPort);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    /**
     * Run the loop that listens for OSC on a socket until isListening becomes false.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        byte[] lengthbuf = new byte[4];
        while (isListening) {
            try {
                int x = in.read(lengthbuf);
//                System.out.println("length: " + x);
                int length = lengthbuf[0] << 24 | lengthbuf[1] << 16 | lengthbuf[2] << 8 | lengthbuf[3];
                if (length > 0) {
//                    System.out.println("length1: " + length);
                    byte[] buffer = new byte[length];
                    x = in.read(buffer);
//                    System.out.println("length: " + x);
                    OSCPacket oscPacket = converter.convert(buffer, length);
                    dispatcher.dispatchPacket(oscPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
                stopListening();                
            }
        }
    }

    /**
     * Start listening for incoming OSCPackets
     */
    public void startListening() {
        isListening = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop listening for incoming OSCPackets
     */
    public void stopListening() {
        isListening = false;
    }

    /**
     * Am I listening for packets?
     */
    public boolean isListening() {
        return isListening;
    }

    /**
     * Register the listener for incoming OSCPackets addressed to an Address
     * @param anAddress  the address to listen for
     * @param listener   the object to invoke when a message comes in
     */
    public void addListener(String anAddress, OSCListener listener) {
        dispatcher.addListener(anAddress, listener);
    }

    /**
     /**
     * Send an osc packet (message or bundle) to the receiver I am bound to.
     * @param aPacket OSCPacket
     * @throws java.io.IOException
     */
    public void send(OSCPacket aPacket) throws IOException {
        byte[] byteArray = aPacket.getByteArray(true);

        out.write(byteArray);
    }

    /**
     * Close the socket and free-up resources. It's recommended that clients call
     * this when they are done with the port.
     */
    public void close() {
        try {
            System.out.println("close");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(OSCPortClientTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
