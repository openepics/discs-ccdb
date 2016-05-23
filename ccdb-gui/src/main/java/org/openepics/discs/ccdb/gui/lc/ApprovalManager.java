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

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.User;
import org.openepics.discs.ccdb.model.cm.PhaseApproval;

/**
 * Bean to support rack layout view
 *
 * @author vuppala
 *
 */
@Named
@ViewScoped
public class ApprovalManager implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ApprovalManager.class.getName());
    @EJB
    private LifecycleEJB lifecycleEJB;
    
    private List<PhaseApproval> approvalList;
    private List<PhaseApproval> filteredList;
    private List<PhaseApproval> selectedApprovals;
    
    public ApprovalManager() {
        
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        approvalList = lifecycleEJB.findAllApprovals();
        LOGGER.log(Level.INFO, "Size of LC approval list: {0}", approvalList.size());
    }

    
    public void onApprove() {
        try {
            Preconditions.checkNotNull(selectedApprovals);
            User user = new User("admin"); 
            for (PhaseApproval selectedApproval: selectedApprovals) {
                selectedApproval.setApproved_at(new Date());
                selectedApproval.setApproved_by(user);
                selectedApproval.setApproved(true);
                lifecycleEJB.saveApproval(selectedApproval);
            }             
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                                                                "Approval successful.");
        } finally {
//            selectedApproval = null;           
        }
    }
    
    public void onDisapprove() {
        try {
            Preconditions.checkNotNull(selectedApprovals);
            User user = new User("admin"); 
            for (PhaseApproval selectedApproval: selectedApprovals) {
                selectedApproval.setApproved_at(new Date());
                selectedApproval.setApproved_by(user);
                selectedApproval.setApproved(false);
                lifecycleEJB.saveApproval(selectedApproval);
            }         
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                                                                "Disappproval successful.");
        } finally {
//            selectedApproval = null;           
        }
    }
    // getters and setters

    public List<PhaseApproval> getApprovalList() {
        return approvalList;
    }

    public List<PhaseApproval> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<PhaseApproval> filteredList) {
        this.filteredList = filteredList;
    }

    public List<PhaseApproval> getSelectedApprovals() {
        return selectedApprovals;
    }

    public void setSelectedApprovals(List<PhaseApproval> selectedApprovals) {
        this.selectedApprovals = selectedApprovals;
    }  
}
