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

/**
 *
 *  Status of life cycle phases of slots or devices
 * 
 * @author vuppala
 *
 */
@Stateless
public class ReviewEJB {    
    private static final Logger logger = Logger.getLogger(ReviewEJB.class.getName());   
    @PersistenceContext private EntityManager em;
    
    /**
     * All approvals
     * 
     * @return a list of all {@link PhaseApproval}s ordered by name.
     */
    public List<PhaseApproval> findAllApprovals() {
        return em.createNamedQuery("ReviewApproval.findAll", PhaseApproval.class).getResultList();
    }    
    
    /**
     * All reviews
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<Phase> findAllReviews() {
        return em.createNamedQuery("Review.findAll", Phase.class).getResultList();
    } 
    
    /**
     * All reviews
     * 
     * @return a list of all {@link Phase}s ordered by name.
     */
    public List<PhaseAssignment> findAllRequirements() {
        return em.createNamedQuery("ReviewRequirement.findAll", PhaseAssignment.class).getResultList();
    }
    
    /**
     * save a process
     *
     * @param process
     */
    public void saveProcess(Phase process) {
        if (process.getId() == null) {
            em.persist(process);
        } else {
            em.merge(process);
        }
        logger.log(Level.FINE, "process saved - {0}", process.getId());
    }

    /**
     * delete a given process
     *
     * @param process
     */
    public void deleteProcess(Phase process) {
        Phase src = em.find(Phase.class, process.getId());
        em.remove(src);
    }

    /**
     * find a process given its id
     *
     * @param id
     * @return the process
     */
    public Phase findProcess(Long id) {
        return em.find(Phase.class, id);
    }
    
    // ----------------- Requirements
    
    /**
     * save a process
     *
     * @param process
     */
    public void saveRequirement(PhaseAssignment process) {
        if (process.getId() == null) {
            em.persist(process);
        } else {
            em.merge(process);
        }
        logger.log(Level.FINE, "process requirement saved - {0}", process.getId());
    }

    public void saveRequirement(PhaseAssignment assignment, List<User> approvers) {
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
        logger.log(Level.FINE, "process requirement saved - {0}", assignment.getId());
    }
    
    /**
     * delete a given process
     *
     * @param process
     */
    public void deleteRequirement(PhaseAssignment process) {
        PhaseAssignment src = em.find(PhaseAssignment.class, process.getId());
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
    
    // --------------------
    
    /**
     * find a approval given its id
     *
     * @param id
     * @return the process
     */
    public PhaseApproval findReviewApproval(Long id) {
        return em.find(PhaseApproval.class, id);
    }
}
