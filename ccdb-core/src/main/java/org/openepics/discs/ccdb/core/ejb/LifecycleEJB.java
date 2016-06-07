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

import java.util.ArrayList;
import java.util.Iterator;
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
import org.openepics.discs.ccdb.model.cm.PhaseStatus;
import org.openepics.discs.ccdb.model.cm.PhaseTag;
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
     * Reviews with a given tag
     * 
     * @param type
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<Phase> findPhases(StatusType type) {
        return em.createNamedQuery("Phase.findByType", Phase.class).setParameter("type", type).getResultList();
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
     * All assignments
     * 
     * @param type
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<PhaseAssignment> findAssignments(StatusType type) {
        return em.createNamedQuery("PhaseAssignment.findByType", PhaseAssignment.class).setParameter("type", type).getResultList();
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

    /**
     * 
     * @param approvals
     * @param approver
     * @return 
     */
    private boolean isAnApprover(List<PhaseApproval> approvals, User approver) {
        if (approvals == null || approver == null ) {
            return false;
        }
        for (PhaseApproval approval : approvals) {
            if (approver.equals(approval.getAssignedApprover())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * ToDo: Improve the code with set operations.
     * 
     * @param assignment
     * @param approvers 
     */
    public void saveAssignment(PhaseAssignment assignment, List<User> approvers) {

        if (assignment.getId() == null) {
            em.persist(assignment);
        } else {
            em.merge(assignment);
        }

        if (assignment.getApprovals() != null && approvers != null) {
            Iterator<PhaseApproval> iterator =  assignment.getApprovals().iterator();
            while (iterator.hasNext()) {
                PhaseApproval approval = iterator.next();
                
                LOGGER.log(Level.INFO, "checking for removed approvers {0}", approval.getAssignedApprover());
//                if (! approvers.contains(iterator.next().getApproved_by())) {
                if (! approvers.contains(approval.getAssignedApprover())) {
                    iterator.remove();
                    em.remove(approval);
                    LOGGER.log(Level.INFO, "removed approval record {0}", approval.getAssignedApprover());
                }
            }
        }

        if (approvers != null) {
            if (assignment.getApprovals() == null) {
                assignment.setApprovals(new ArrayList<>());
            }
            for(User user : approvers) {
                LOGGER.log(Level.INFO, "checking for new approver {0}", user.getName());
                if (!isAnApprover(assignment.getApprovals(), user)) {
                    PhaseApproval approval = new PhaseApproval();
                    approval.setAssignedApprover(user);
                    approval.setAssignment(assignment);
                    em.persist(approval);
                    assignment.getApprovals().add(approval);
                    LOGGER.log(Level.INFO, "added approver {0}", user.getName());
                }
            }
        }
        
        em.flush(); // ToDo: should not be needed
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
     * All approvals
     * 
     * @return a list of all {@link PhaseApproval}s ordered by name.
     */
    public List<PhaseApproval> findApprovals(StatusType type) {
        return em.createNamedQuery("PhaseApproval.findByType", PhaseApproval.class).setParameter("type", type).getResultList();
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
    
    /**
     * save a phase approval
     *
     * @param approval
     */
    public void saveApproval(PhaseApproval approval) {
        if (approval.getId() == null) {
            em.persist(approval);
        } else {
            em.merge(approval);
        }
        LOGGER.log(Level.INFO, "phase approval  saved - {0}", approval.getId());
    }
    
    /**
     * delete a given approval
     *
     * @param approval
     */
    public void deleteApproval(PhaseApproval approval) {
        PhaseApproval src = em.find(PhaseApproval.class, approval.getId());
        em.remove(src);
    }
    
    // ------------------ Status type
    /**
     * All types
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<StatusType> findAllStatusTypes() {
        return em.createNamedQuery("StatusType.findAll", StatusType.class).getResultList();
    } 
    
    /**
     * PHase type 
     * 
     * @param name
     * @return a list of all {@link Phase}s ordered by name.
     */
    public StatusType findStatusType(String name) {
        return em.createNamedQuery("StatusType.findByName", StatusType.class).setParameter("name", name).getSingleResult();
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
    
    //----------------- phase status
    /**
     * 
     * @return 
     */
    public List<PhaseStatus> findAllStatuses() {
        return em.createNamedQuery("PhaseStatus.findAll", PhaseStatus.class).getResultList();
    }  
    
    /**
     * 
     * @param assignment
     * @return 
     */
    public List<PhaseStatus> findAllStatuses(PhaseAssignment assignment) {
        return em.createNamedQuery("PhaseStatus.findByAssignment", PhaseStatus.class)
                .setParameter("assignment", assignment)
                .getResultList();
    }
    
    /**
     * Find status options of a given type
     * 
     * @param type
     * @return 
     */
    public List<StatusTypeOption> findStatusOptions(StatusType type) {
        return em.createNamedQuery("StatusTypeOption.findByType", StatusTypeOption.class).setParameter("type",type).getResultList();
    }  
    
    /**
     * Find status option for a given id
     * 
     * @param id
     * @return 
     */
    public StatusTypeOption findStatusOption(Long id) {
        return em.find(StatusTypeOption.class,id);
    } 
}
