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
package org.openepics.discs.ccdb.gui.lc;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.ccdb.core.ejb.LcStatusEJB;
import org.openepics.discs.ccdb.model.cm.PhaseStatus;

/**
 * Bean to support rack layout view
 *
 * @author vuppala
 *
 */
@Named
@ViewScoped
public class LifecycleStatusManager implements Serializable {

    private static final Logger logger = Logger.getLogger(LifecycleStatusManager.class.getName());
    @EJB
    private LcStatusEJB lifecycleEJB;
    
    private List<PhaseStatus> statusList;
    private List<PhaseStatus> filteredStatus;
  
    public LifecycleStatusManager() {
        
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        statusList = lifecycleEJB.findAll();
        logger.log(Level.INFO, "Size of LC sttaus list: {0}", statusList.size());
    }

    // getters and setters

    public List<PhaseStatus> getFilteredStatus() {
        return filteredStatus;
    }

    public void setFilteredStatus(List<PhaseStatus> filteredStatus) {
        this.filteredStatus = filteredStatus;
    }

    public List<PhaseStatus> getStatusList() {
        return statusList;
    }
   
}
