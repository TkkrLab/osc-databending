/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

/**
 *
 * @author manson
 */
public class OSCDeviceParameter {
    private String device;
    private String parameter;

    public OSCDeviceParameter(String device, String parameter) {
        this.device = device;
        this.parameter = parameter;
    }
    
    @Override
    public int hashCode()
    {
        return device.hashCode() ^ parameter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OSCDeviceParameter other = (OSCDeviceParameter) obj;
        if ((this.device == null) ? (other.device != null) : !this.device.equals(other.device)) {
            return false;
        }
        if ((this.parameter == null) ? (other.parameter != null) : !this.parameter.equals(other.parameter)) {
            return false;
        }
        return true;
    }
    
    public String getDevice()
    {
        return device;
    }
    
    public String getParameter()
    {
        return parameter;
    }
    
    @Override
    public String toString()
    {
        return device + "." + parameter;
    }
}
