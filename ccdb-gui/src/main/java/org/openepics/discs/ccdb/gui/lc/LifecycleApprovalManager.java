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
import org.openepics.discs.ccdb.core.ejb.LcApprovalEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.User;
import org.openepics.discs.ccdb.model.cm.ReviewApproval;

/**
 * Bean to support rack layout view
 *
 * @author vuppala
 *
 */
@Named
@ViewScoped
public class LifecycleApprovalManager implements Serializable {

    private static final Logger logger = Logger.getLogger(LifecycleApprovalManager.class.getName());
    @EJB
    private LcApprovalEJB lifecycleEJB;
    
    private List<ReviewApproval> approvalList;
    private List<ReviewApproval> filteredList;
    private List<ReviewApproval> selectedApprovals;
    
    public LifecycleApprovalManager() {
        
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        approvalList = lifecycleEJB.findAll();
        logger.log(Level.INFO, "Size of LC approval list: {0}", approvalList.size());
    }

    
    public void onApprove() {
        try {
            Preconditions.checkNotNull(selectedApprovals);
            User user = new User("admin"); 
            for (ReviewApproval selectedApproval: selectedApprovals) {
                selectedApproval.setApproved_at(new Date());
                selectedApproval.setApproved_by(user);
                selectedApproval.setApproved(true);
            }
            // final Unit unitToSave = selectedApproval.getUnit();
//            selectedApproval.setApproved_at(new Date());
//            servletRequest.getUserPrincipal() != null ? servletRequest.getUserPrincipal().getName() : null
//            User user = new User("admin");       
//             selectedApproval.setApproved_by(user);
            //lifecycleEJB.save(selectedApproval);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                                                                "Approval has been successfully modified.");
        } finally {
//            selectedApproval = null;
            
        }
    }
    
    public void onDisapprove() {
        try {
            Preconditions.checkNotNull(selectedApprovals);
            User user = new User("admin"); 
            for (ReviewApproval selectedApproval: selectedApprovals) {
                selectedApproval.setApproved_at(new Date());
                selectedApproval.setApproved_by(user);
                selectedApproval.setApproved(false);
            }
            // final Unit unitToSave = selectedApproval.getUnit();
//            selectedApproval.setApproved_at(new Date());
//            servletRequest.getUserPrincipal() != null ? servletRequest.getUserPrincipal().getName() : null
//            User user = new User("admin");       
//             selectedApproval.setApproved_by(user);
            //lifecycleEJB.save(selectedApproval);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                                                                "Approval has been successfully modified.");
        } finally {
//            selectedApproval = null;
            
        }
    }
    // getters and setters

    public List<ReviewApproval> getApprovalList() {
        return approvalList;
    }

    public List<ReviewApproval> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<ReviewApproval> filteredList) {
        this.filteredList = filteredList;
    }

    public List<ReviewApproval> getSelectedApprovals() {
        return selectedApprovals;
    }

    public void setSelectedApprovals(List<ReviewApproval> selectedApprovals) {
        this.selectedApprovals = selectedApprovals;
    }
   
}
