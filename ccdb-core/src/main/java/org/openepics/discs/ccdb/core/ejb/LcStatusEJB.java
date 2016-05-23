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
import java.util.logging.Logger;

import javax.ejb.Stateless;
import org.openepics.discs.ccdb.model.cm.PhaseStatus;

/**
 *
 *  Status of life cycle phases of slots or devices
 * 
 * @author vuppala
 *
 */
@Stateless
public class LcStatusEJB extends DAO<PhaseStatus> {    
    private static final Logger logger = Logger.getLogger(LcStatusEJB.class.getName());
    
    @Override
    protected Class<PhaseStatus> getEntityClass() {
        return PhaseStatus.class;
    }

    /**
     * All the tacks
     * 
     * @return a list of all {@link Rack}s ordered by name.
     */
    @Override
    public List<PhaseStatus> findAll() {
        return em.createNamedQuery("LifecycleStatus.findAll", PhaseStatus.class).getResultList();
    }    
    
}
