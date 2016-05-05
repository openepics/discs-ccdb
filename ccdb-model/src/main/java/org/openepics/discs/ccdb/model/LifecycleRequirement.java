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

package org.openepics.discs.ccdb.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Configuration Management requirements for slot or device
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "lc_requirement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LifecycleRequirement.findAll", query = "SELECT d FROM LifecycleRequirement d"),
    @NamedQuery(name = "LifecycleRequirement.findBySlot", query = "SELECT d FROM LifecycleRequirement d WHERE d.slot = :slot"),
    @NamedQuery(name = "LifecycleRequirement.findByDevice", query = "SELECT d FROM LifecycleRequirement d WHERE d.device = :device"),
    @NamedQuery(name = "LifecycleRequirement.findByProcess", query = "SELECT d FROM LifecycleRequirement d WHERE d.phase = :phase")
})
public class LifecycleRequirement extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "slot")
    private Slot slot;
    
    @ManyToOne
    @JoinColumn(name = "device")
    private Device device;
    
    @ManyToOne
    @JoinColumn(name = "phase")
    private LifecyclePhase phase;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "requestor")
    private User requestor;    
    
    @OneToMany(mappedBy = "requirement")
    private List<LifecycleApprovalRecord> approvals;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LifecycleRequirement)) {
            return false;
        }
        LifecycleRequirement other = (LifecycleRequirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.LifecycleRequirement[ id=" + id + " ]";
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LifecyclePhase getPhase() {
        return phase;
    }

    public void setPhase(LifecyclePhase phase) {
        this.phase = phase;
    }

    public List<LifecycleApprovalRecord> getApprovals() {
        return approvals;
    }
    
}
