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

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;
import org.openepics.discs.ccdb.model.Device;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.User;

/**
 * Configuration Management: Phases a slot must go through
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaseAssignment.findAll", query = "SELECT d FROM PhaseAssignment d"),
    @NamedQuery(name = "PhaseAssignment.findByName", query = "SELECT d FROM PhaseAssignment d WHERE d.phase = :phase"),
    @NamedQuery(name = "PhaseAssignment.findByType", query = "SELECT d FROM PhaseAssignment d WHERE d.phase.statusType = :type"),
    @NamedQuery(name = "PhaseAssignment.findBySlot", query = "SELECT d FROM PhaseAssignment d WHERE d.slot = :slot")
})
public class PhaseAssignment extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "slot")
    private Slot slot;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "device")
    private Device device;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "phase")
    private Phase phase;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "requestor")
    private User requestor;    
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "assignment")
    private List<PhaseApproval> approvals;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhaseAssignment)) {
            return false;
        }
        PhaseAssignment other = (PhaseAssignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.ReviewRequirement[ id=" + id + " ]";
    }
    
    // getters and setters

    public User getRequestor() {
        return requestor;
    }

    public void setRequestor(User requestor) {
        this.requestor = requestor;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public List<PhaseApproval> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<PhaseApproval> approvals) {
        this.approvals = approvals;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
}
