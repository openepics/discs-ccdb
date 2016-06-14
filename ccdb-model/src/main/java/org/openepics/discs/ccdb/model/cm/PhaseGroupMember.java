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
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;
import org.openepics.discs.ccdb.model.auth.Role;

/**
 * A phase of group 
 * 
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "cm_phasegroup_member")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaseGroupMember.findAll", query = "SELECT d FROM PhaseGroupMember d"),
    @NamedQuery(name = "PhaseGroupMember.findByPhase", query = "SELECT d FROM PhaseGroupMember d WHERE d.phase = :phase"),
    @NamedQuery(name = "PhaseGroupMember.findPhasesByGroup", query = "SELECT d.phase FROM PhaseGroupMember d WHERE d.phaseGroup = :group"),
    @NamedQuery(name = "PhaseGroupMember.findByGroup", query = "SELECT d FROM PhaseGroupMember d WHERE d.phaseGroup = :group")
})
public class PhaseGroupMember extends ConfigurationEntity {

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
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "summary_phase")
    private Boolean summaryPhase = false;
    
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

    public Boolean getSummaryPhase() {
        return summaryPhase;
    }

    public void setSummaryPhase(Boolean summaryPhase) {
        this.summaryPhase = summaryPhase;
    }
    
    
}
