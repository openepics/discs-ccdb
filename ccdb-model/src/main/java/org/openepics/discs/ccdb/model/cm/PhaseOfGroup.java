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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;
import org.openepics.discs.ccdb.model.auth.Role;

/**
 * A phase of group 
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_phase_of_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaseOfGroup.findAll", query = "SELECT d FROM PhaseOfGroup d"),
    @NamedQuery(name = "PhaseOfGroup.findByPhase", query = "SELECT d FROM PhaseOfGroup d WHERE d.phase = :phase"),
    @NamedQuery(name = "PhaseOfGroup.findPhasesByGroup", query = "SELECT d.phase FROM PhaseOfGroup d WHERE d.phaseGroup = :group"),
    @NamedQuery(name = "PhaseOfGroup.findByGroup", query = "SELECT d FROM PhaseOfGroup d WHERE d.phaseGroup = :group")
})
public class PhaseOfGroup extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "phase")
    private Phase phase;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "phasegroup")
    private PhaseGroup phaseGroup;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "sme")
    private Role sme;
    // --

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public PhaseGroup getPhaseGroup() {
        return phaseGroup;
    }

    public void setPhaseGroup(PhaseGroup phaseGroup) {
        this.phaseGroup = phaseGroup;
    }

    public Role getSme() {
        return sme;
    }

    public void setSme(Role sme) {
        this.sme = sme;
    }
}
