/*
 * This software is Copyright by the Board of Trustees of Michigan
 * State University (c) Copyright 2012.
 *
 * You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *       http://www.gnu.org/licenses/gpl.txt
 *
 * Contact Information:
 *   Facilitty for Rare Isotope Beam
 *   Michigan State University
 *   East Lansing, MI 48824-1321
 *   http://frib.msu.edu
 *
 */

package org.openepics.discs.ccdb.model.cm;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 * Status of a phase assignment 
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_phase_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaseStatus.findAll", query = "SELECT d FROM PhaseStatus d"),
    @NamedQuery(name = "PhaseStatus.findByAssignment", query = "SELECT d FROM PhaseStatus d WHERE d.assignment = :assignment")
})
public class PhaseStatus extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment")
    private PhaseAssignment assignment;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "status")
    private StatusTypeOption status;
       
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhaseStatus)) {
            return false;
        }
        PhaseStatus other = (PhaseStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.CMStatus[ id=" + id + " ]";
    }
    
    // --

    public StatusTypeOption getStatus() {
        return status;
    }

    public void setStatus(StatusTypeOption status) {
        this.status = status;
    }  

    public PhaseAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(PhaseAssignment assignment) {
        this.assignment = assignment;
    }

    
}
