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
package org.openepics.discs.ccdb.core.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.openepics.discs.ccdb.model.User;
import org.openepics.discs.ccdb.model.cm.Phase;
import org.openepics.discs.ccdb.model.cm.PhaseApproval;
import org.openepics.discs.ccdb.model.cm.PhaseAssignment;
import org.openepics.discs.ccdb.model.cm.StatusType;
import org.openepics.discs.ccdb.model.cm.StatusTypeOption;

/**
 *
 *  Status of life cycle phases of slots or devices
 * 
 * @author vuppala
 *
 */
@Stateless
public class LifecycleEJB {    
    private static final Logger LOGGER = Logger.getLogger(LifecycleEJB.class.getName());   
    @PersistenceContext private EntityManager em;
    
     
    
    /**
     * All reviews
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<Phase> findAllPhases() {
        return em.createNamedQuery("Phase.findAll", Phase.class).getResultList();
    } 
    
    
    
    /**
     * save a process
     *
     * @param phase
     */
    public void savePhase(Phase phase) {
        if (phase.getId() == null) {
            em.persist(phase);
        } else {
            em.merge(phase);
        }
        LOGGER.log(Level.FINE, "process saved - {0}", phase.getId());
    }

    /**
     * delete a given process
     *
     * @param phase
     */
    public void deletePhase(Phase phase) {
        Phase src = em.find(Phase.class, phase.getId());
        em.remove(src);
    }

    /**
     * find a process given its id
     *
     * @param id
     * @return the process
     */
    public Phase findPhase(Long id) {
        return em.find(Phase.class, id);
    }
    
    // ----------------- Assignments 
    /**
     * All assignments
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<PhaseAssignment> findAllAssignments() {
        return em.createNamedQuery("PhaseAssignment.findAll", PhaseAssignment.class).getResultList();
    }
    
    /**
     * save a process
     *
     * @param assignment
     */
    public void saveAssignment(PhaseAssignment assignment) {
        if (assignment.getId() == null) {
            em.persist(assignment);
        } else {
            em.merge(assignment);
        }
        LOGGER.log(Level.FINE, "phase assignment  saved - {0}", assignment.getId());
    }

    public void saveAssignment(PhaseAssignment assignment, List<User> approvers) {
        if (assignment.getId() == null) {
            for (User user: approvers) {
                PhaseApproval approval = new PhaseApproval();
                approval.setAssignedApprover(user);
                approval.setAssignment(assignment);
                em.persist(approval);
            }
            em.persist(assignment);
        } else {
            em.merge(assignment);
        }
        LOGGER.log(Level.FINE, "phase assignment saved - {0}", assignment.getId());
    }
    
    /**
     * delete a given process
     *
     * @param assignment
     */
    public void deleteAssignment(PhaseAssignment assignment) {
        PhaseAssignment src = em.find(PhaseAssignment.class, assignment.getId());
        em.remove(src);
    }

    /**
     * find a process given its id
     *
     * @param id
     * @return the process
     */
    public PhaseAssignment findRequirement(Long id) {
        return em.find(PhaseAssignment.class, id);
    }
    
    // -------------------- Approvals
    /**
     * All approvals
     * 
     * @return a list of all {@link PhaseApproval}s ordered by name.
     */
    public List<PhaseApproval> findAllApprovals() {
        return em.createNamedQuery("PhaseApproval.findAll", PhaseApproval.class).getResultList();
    }   
    
    /**
     * find a approval given its id
     *
     * @param id
     * @return the process
     */
    public PhaseApproval findPhaseApproval(Long id) {
        return em.find(PhaseApproval.class, id);
    }
    
    // ------------------ Status type
    /**
     * All reviews
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<StatusType> findAllStatusTypes() {
        return em.createNamedQuery("StatusType.findAll", StatusType.class).getResultList();
    } 
    /**
     * save a status type
     *
     * @param status type
     */
    public void saveStatusType(StatusType status) {
        if (status.getId() == null) {
            em.persist(status);
        } else {
            em.merge(status);
        }
        LOGGER.log(Level.FINE, "status type saved - {0}", status.getId());
    }

    /**
     * delete a given process
     *
     * @param status
     */
    public void deleteStatusType(StatusType status) {
        StatusType src = em.find(StatusType.class, status.getId());
        em.remove(src);
    }

    /**
     * find a status type given its id
     *
     * @param id
     * @return the status type
     */
    public StatusType findStatusType(Long id) {
        return em.find(StatusType.class, id);
    }
    
    /**
     * All status options
     * 
     * @return a list of all {@link StatusTypeOption}s .
     */
    public List<StatusTypeOption> findAllStatusOptions() {
        return em.createNamedQuery("StatusTypeOption.findAll", StatusTypeOption.class).getResultList();
    } 
    
    /**
     * All status options for a type
     * 
     * @param type given status type
     * @return a list of all {@link StatusTypeOption}s.
     */
    public List<StatusTypeOption> findAllStatusOptions(StatusType type) {
        return em.createNamedQuery("StatusTypeOption.findByType", StatusTypeOption.class)
                .setParameter("type", type)
                .getResultList();
    } 
}
