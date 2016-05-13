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
 * Configuration Management requirements for slot or device
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_review_equirement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReviewRequirement.findAll", query = "SELECT d FROM ReviewRequirement d"),
    @NamedQuery(name = "ReviewRequirement.findBySlot", query = "SELECT d FROM ReviewRequirement d WHERE d.slot = :slot"),
    @NamedQuery(name = "ReviewRequirement.findByDevice", query = "SELECT d FROM ReviewRequirement d WHERE d.device = :device"),
    @NamedQuery(name = "ReviewRequirement.findByProcess", query = "SELECT d FROM ReviewRequirement d WHERE d.phase = :phase")
})
public class ReviewRequirement extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "slot")
    private Slot slot;
    
    @ManyToOne
    @JoinColumn(name = "device")
    private Device device;
    
    @ManyToOne
    @JoinColumn(name = "phase")
    private Review phase;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "requestor")
    private User requestor;    
    
    @OneToMany(mappedBy = "requirement")
    private List<ReviewApproval> approvals;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReviewRequirement)) {
            return false;
        }
        ReviewRequirement other = (ReviewRequirement) object;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Review getPhase() {
        return phase;
    }

    public void setPhase(Review phase) {
        this.phase = phase;
    }

    public List<ReviewApproval> getApprovals() {
        return approvals;
    }
    
}
