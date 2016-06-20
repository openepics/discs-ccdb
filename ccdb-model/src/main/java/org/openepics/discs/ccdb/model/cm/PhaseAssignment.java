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
import org.openepics.discs.ccdb.model.auth.User;

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
    @NamedQuery(name = "PhaseAssignment.findGroupAssignments", query = "SELECT d FROM PhaseAssignment d WHERE d.slotGroup IS NOT null"),
    @NamedQuery(name = "PhaseAssignment.findSlotAssignments", query = "SELECT d FROM PhaseAssignment d WHERE d.slot IS NOT null"),
    @NamedQuery(name = "PhaseAssignment.findDeviceAssignments", query = "SELECT d FROM PhaseAssignment d WHERE d.slot IS null AND d.device IS NOT null"),
    @NamedQuery(name = "PhaseAssignment.findByGroup", query = "SELECT d FROM PhaseAssignment d WHERE d.phaseGroup = :group"),
    @NamedQuery(name = "PhaseAssignment.findBySlotGroup", query = "SELECT d FROM PhaseAssignment d WHERE d.slotGroup = :group"),
    @NamedQuery(name = "PhaseAssignment.findBySlot", query = "SELECT d FROM PhaseAssignment d WHERE d.slot = :slot")
})
public class PhaseAssignment extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "slot_group")
    private SlotGroup slotGroup;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "slot")
    private Slot slot;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "device")
    private Device device;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "phasegroup")
    private PhaseGroup phaseGroup;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "requestor")
    private User requestor;    
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "assignment")
    private List<PhaseApproval> approvals;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "assignment")
    private List<PhaseStatus> statuses;
    
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

    public PhaseGroup getPhaseGroup() {
        return phaseGroup;
    }

    public void setPhaseGroup(PhaseGroup phaseGroup) {
        this.phaseGroup = phaseGroup;
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

    public SlotGroup getSlotGroup() {
        return slotGroup;
    }

    public void setSlotGroup(SlotGroup slotGroup) {
        this.slotGroup = slotGroup;
    }

    public List<PhaseStatus> getStatuses() {
        return statuses;
    }
}
