package com.illposed.osc;

import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
import java.net.DatagramPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OSCPacket is the abstract superclass for the various
 * kinds of OSC Messages. The actual packets are:
 * <ul>
 * <li>OSCMessage &mdash; simple OSC messages
 * <li>OSCBundle &mdash; OSC messages with timestamps and/or made up of multiple messages
 * </ul>
 *<p>
 * This implementation is based on <a href="http://www.emergent.de/Goodies/">Markus Gaelli</a> and
 * Iannis Zannos' OSC implementation in Squeak Smalltalk.
 */
public abstract class OSCPacket {

    protected boolean isByteArrayComputed;
    protected byte[] byteArray;
    protected InetAddress inetAddress;

    /**
     * Default constructor for the abstract class
     */
    public OSCPacket() {
        super();
    }

    /**
     * Generate a representation of this packet conforming to the
     * the OSC byte stream specification. Used Internally.
     * @param isTCP
     */
    protected void computeByteArray(boolean isTCP) {
        OSCJavaToByteArrayConverter stream = new OSCJavaToByteArrayConverter();
        writeStream(stream);
        if (isTCP) {
            OSCJavaToByteArrayConverter streamTCP = new OSCJavaToByteArrayConverter();
            streamTCP.write(stream.size());
            try {
                stream.writeTo(streamTCP.getStream());
                byteArray = streamTCP.toByteArray();
            } catch (IOException ex) {
                Logger.getLogger(OSCPacket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            byteArray = stream.toByteArray();
        }
    }

    /**
     * Subclasses should implement this method to product a byte array
     * formatted according to the OSC specification.
     * @param stream OscPacketByteArrayConverter
     */
    protected abstract void writeStream(OSCJavaToByteArrayConverter stream);

    /**
     * Return the OSC byte stream for this packet.
     * @param isTCP
     * @return byte[]
     */
    public byte[] getByteArray(boolean isTCP) {
        if (!isByteArrayComputed) {
            computeByteArray(isTCP);
        }
        return byteArray;
    }

    /**
     * Run any post construction initialization. (By default, do nothing.) 
     */
    protected void init() {

    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
    
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
}
