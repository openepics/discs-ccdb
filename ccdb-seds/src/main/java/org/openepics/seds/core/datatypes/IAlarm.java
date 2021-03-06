/*
 * This software is Copyright by the Board of Trustees of Michigan
 *  State University (c) Copyright 2013, 2014.
 *
 *  You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *    http://www.gnu.org/licenses/gpl.txt
 *
 *  Contact Information:
 *       Facility for Rare Isotope Beam
 *       Michigan State University
 *       East Lansing, MI 48824-1321
 *        http://frib.msu.edu
 */
package org.openepics.seds.core.datatypes;

import java.util.Objects;

import org.openepics.seds.api.datatypes.SedsAlarm;
import org.openepics.seds.util.AlarmType;

class IAlarm implements SedsAlarm {

    private final AlarmType severity;
    private final String status;
    private final String message;

    IAlarm(AlarmType severity, String status, String message) {
        this.severity = severity;
        this.status = status;
        this.message = message;
    }

    @Override
    public AlarmType getSeverity() {
        return severity;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((severity == null) ? 0 : severity.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IAlarm other = (IAlarm) obj;
        if (!Objects.equals(this.severity, other.severity)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "IAlarm [severity=" + severity + ", status=" + status + ", message=" + message + "]";
    }
}
