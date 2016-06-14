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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 * Status values for a phase group
 * 
 * ToDo: fix the findDefault query
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_status_option" )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusOption.findAll", query = "SELECT d FROM StatusOption d"),
    @NamedQuery(name = "StatusOption.findByGroup", query = "SELECT d FROM StatusOption d WHERE d.phaseGroup = :group"),
    
    @NamedQuery(name = "StatusOption.findByName", query = "SELECT d FROM StatusOption d WHERE  d.name = :name")
})
public class StatusOption extends ConfigurationEntity {

    private static final long serialVersionUID = 1L; 
    
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "phase_group")
    private PhaseGroup phaseGroup;
    
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
    @NotNull
    @Column(name = "logical_value")
    private Boolean logicalValue = true;
    
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

    public PhaseGroup getPhaseGroup() {
        return phaseGroup;
    }

    public void setPhaseGroup(PhaseGroup phaseGroup) {
        this.phaseGroup = phaseGroup;
    }

    public Boolean getLogicalValue() {
        return logicalValue;
    }

    public void setLogicalValue(Boolean logicalValue) {
        this.logicalValue = logicalValue;
    }
    
}
