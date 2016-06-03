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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 * Life cycle phase
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_phase" )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Phase.findAll", query = "SELECT d FROM Phase d"),
    @NamedQuery(name = "Phase.findByType", query = "SELECT d FROM Phase d WHERE d.statusType = :type"),
    @NamedQuery(name = "Phase.findByName", query = "SELECT d FROM Phase d WHERE d.name = :name")
})
public class Phase extends ConfigurationEntity {

    private static final long serialVersionUID = 1L; 

    @Basic(optional = false)
    @NotNull
    @Size(min=1, max=64)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Size(min=1, max=255)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @Column(name = "loc")
    @Enumerated(EnumType.STRING)
    private LevelOfCare levelOfControl = LevelOfCare.NONE;
    
    @Basic(optional = false)
    @Column(name = "tag")
    @Enumerated(EnumType.STRING)
    private PhaseTag tag = PhaseTag.NONE;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_type")
    private StatusType statusType;
            
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Phase)) {
            return false;
        }
        Phase other = (Phase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.CMProcess[ id=" + id + " ]";
    }
    
    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LevelOfCare getLevelOfControl() {
        return levelOfControl;
    }

    public void setLevelOfControl(LevelOfCare levelOfControl) {
        this.levelOfControl = levelOfControl;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public PhaseTag getTag() {
        return tag;
    }

    public void setTag(PhaseTag tag) {
        this.tag = tag;
    }
    
}
