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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;
import org.openepics.discs.ccdb.model.auth.User;

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
    @NamedQuery(name = "PhaseStatus.findValid", query = "SELECT d FROM PhaseStatus d"),
//    @NamedQuery(name = "PhaseStatus.findValid", query = "SELECT d FROM PhaseStatus d WHERE d.assignment.slot is null OR d.assignment.slot.cmGroup is NOT null"),
    @NamedQuery(name = "PhaseStatus.findByGroup", query = "SELECT d FROM PhaseStatus d WHERE d.phaseOfGroup.phaseGroup = :group"),
    @NamedQuery(name = "PhaseStatus.findByAssignment", query = "SELECT d FROM PhaseStatus d WHERE d.assignment = :assignment")
})
public class PhaseStatus extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment")
    private PhaseAssignment assignment;
    
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "phaseOfGroup")
    private PhaseGroupMember phaseOfGroup;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_sme")
    private User assignedSME;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "status")
    private StatusOption status;
    
//    @ManyToOne
//    @JoinColumn(name = "approved_by")
//    private User approved_by;
//    
//    @Column(name = "approved_at")
//    @Basic
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date approved_at;
    
    @Column(name = "comment")
    @Size(max = 1024)
    @Basic
    private String comment;
    // --

    public StatusOption getStatus() {
        return status;
    }

    public void setStatus(StatusOption status) {
        this.status = status;
    }  

    public PhaseAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(PhaseAssignment assignment) {
        this.assignment = assignment;
    }

    public User getAssignedSME() {
        return assignedSME;
    }

    public void setAssignedSME(User assignedSME) {
        this.assignedSME = assignedSME;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PhaseGroupMember getPhaseOfGroup() {
        return phaseOfGroup;
    }

    public void setPhaseOfGroup(PhaseGroupMember phaseOfGroup) {
        this.phaseOfGroup = phaseOfGroup;
    }
}
