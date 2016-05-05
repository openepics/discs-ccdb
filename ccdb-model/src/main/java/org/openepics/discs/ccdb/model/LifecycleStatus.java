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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "lc_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LifecycleStatus.findAll", query = "SELECT d FROM LifecycleStatus d"),
    @NamedQuery(name = "LifecycleStatus.findByRequirement", query = "SELECT d FROM LifecycleStatus d WHERE d.requirement = :requirement")
})
public class LifecycleStatus extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "requirement")
    private LifecycleRequirement requirement;
    
    @Basic(optional = false)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PhaseStatus status = PhaseStatus.NOT_STARTED;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LifecycleStatus)) {
            return false;
        }
        LifecycleStatus other = (LifecycleStatus) object;
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

    public PhaseStatus getStatus() {
        return status;
    }

    public void setStatus(PhaseStatus status) {
        this.status = status;
    }

    public LifecycleRequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(LifecycleRequirement requirement) {
        this.requirement = requirement;
    }
    
}
