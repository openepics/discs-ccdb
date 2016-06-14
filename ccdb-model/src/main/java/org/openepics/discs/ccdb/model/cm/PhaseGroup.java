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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 *
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_phasegroup" )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaseGroup.findAll", query = "SELECT d FROM PhaseGroup d"),
    @NamedQuery(name = "PhaseGroup.findByName", query = "SELECT d FROM PhaseGroup d WHERE  d.name = :name")
})
public class PhaseGroup extends ConfigurationEntity {

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
    
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "phaseGroup")
    private List<StatusOption> options;
    
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable( name = "cm_phase_in_group", 
//                joinColumns = @JoinColumn(name = "phasegroup"),
//                inverseJoinColumns = @JoinColumn(name = "phase") )
//    private List<Phase> phases;
  
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="phaseGroup")
    private List<PhaseGroupMember> phases;
    
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

    public List<StatusOption> getOptions() {
        return options;
    }

    public void setOptions(List<StatusOption> options) {
        this.options = options;
    }

    public List<PhaseGroupMember> getPhases() {
        return phases;
    }

    public void setPhases(List<PhaseGroupMember> phases) {
        this.phases = phases;
    }
}
