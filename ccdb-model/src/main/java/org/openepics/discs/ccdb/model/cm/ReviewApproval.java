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

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;
import org.openepics.discs.ccdb.model.User;

/**
 *
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_review_approval")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReviewApproval.findAll", query = "SELECT d FROM ReviewApproval d"),
    @NamedQuery(name = "ReviewApproval.findByName", query = "SELECT d FROM ReviewApproval d WHERE d.requirement = :requirement")
})
public class ReviewApproval extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
   
    @ManyToOne(optional = false)
    @JoinColumn(name = "requirement")
    private ReviewRequirement requirement;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_approver")
    private User assignedApprover;
    
    @Column(name = "approved")
    @Basic(optional = false)
    private boolean approved = false;
    
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approved_by;
    
    @Column(name = "approved_at")
    @Basic
    @Temporal(DATE)
    private Date approved_at;
    
    @Column(name = "comment")
    @Size(min = 1, max = 1024)
    @Basic
    private String comment;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReviewApproval)) {
            return false;
        }
        ReviewApproval other = (ReviewApproval) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.LifecycleApprovalRecord[ id=" + id + " ]";
    }
    // --

    public ReviewRequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(ReviewRequirement requirement) {
        this.requirement = requirement;
    }

    public User getAssignedApprover() {
        return assignedApprover;
    }

    public void setAssignedApprover(User assignedApprover) {
        this.assignedApprover = assignedApprover;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public User getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(User approved_by) {
        this.approved_by = approved_by;
    }

    public Date getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(Date approved_at) {
        this.approved_at = approved_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
