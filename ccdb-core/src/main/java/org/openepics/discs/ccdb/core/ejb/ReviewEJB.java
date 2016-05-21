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
import org.openepics.discs.ccdb.model.cm.Review;
import org.openepics.discs.ccdb.model.cm.ReviewApproval;
import org.openepics.discs.ccdb.model.cm.ReviewRequirement;

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
     * @return a list of all {@link ReviewApproval}s ordered by name.
     */
    public List<ReviewApproval> findAllApprovals() {
        return em.createNamedQuery("ReviewApproval.findAll", ReviewApproval.class).getResultList();
    }    
    
    /**
     * All reviews
     * 
     * @return a list of all {@link Review}s ordered by name.
     */
    public List<Review> findAllReviews() {
        return em.createNamedQuery("Review.findAll", Review.class).getResultList();
    } 
    
    /**
     * All reviews
     * 
     * @return a list of all {@link Review}s ordered by name.
     */
    public List<ReviewRequirement> findAllRequirements() {
        return em.createNamedQuery("ReviewRequirement.findAll", ReviewRequirement.class).getResultList();
    }
    
    /**
     * save a process
     *
     * @param process
     */
    public void saveProcess(Review process) {
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
    public void deleteProcess(Review process) {
        Review src = em.find(Review.class, process.getId());
        em.remove(src);
    }

    /**
     * find a process given its id
     *
     * @param id
     * @return the process
     */
    public Review findProcess(Long id) {
        return em.find(Review.class, id);
    }
    
    // ----------------- Requirements
    
    /**
     * save a process
     *
     * @param process
     */
    public void saveRequirement(ReviewRequirement process) {
        if (process.getId() == null) {
            em.persist(process);
        } else {
            em.merge(process);
        }
        logger.log(Level.FINE, "process requirement saved - {0}", process.getId());
    }

    public void saveRequirement(ReviewRequirement process, List<User> approvers) {
        if (process.getId() == null) {
            for (User user: approvers) {
                ReviewApproval approval = new ReviewApproval();
                approval.setAssignedApprover(user);
                approval.setRequirement(process);
                em.persist(approval);
            }
            em.persist(process);
        } else {
            em.merge(process);
        }
        logger.log(Level.FINE, "process requirement saved - {0}", process.getId());
    }
    
    /**
     * delete a given process
     *
     * @param process
     */
    public void deleteRequirement(ReviewRequirement process) {
        ReviewRequirement src = em.find(ReviewRequirement.class, process.getId());
        em.remove(src);
    }

    /**
     * find a process given its id
     *
     * @param id
     * @return the process
     */
    public ReviewRequirement findRequirement(Long id) {
        return em.find(ReviewRequirement.class, id);
    }
    
    // --------------------
    
    /**
     * find a approval given its id
     *
     * @param id
     * @return the process
     */
    public ReviewApproval findReviewApproval(Long id) {
        return em.find(ReviewApproval.class, id);
    }
}
