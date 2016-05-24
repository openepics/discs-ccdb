/*
 * This software is Copyright by the Board of Trustees of Michigan
 *  State University (c) Copyright 2013, 2014.
 *  
 *  You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *    http://www.gnu.org/licenses/gpl.txt
 *  
 *  Contact Information:
 *       Facility for Rare Isotope Beam
 *       Michigan State University
 *       East Lansing, MI 48824-1321
 *        http://frib.msu.edu
 */

package org.openepics.discs.ccdb.gui.lc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.openepics.discs.ccdb.core.ejb.AuthEJB;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.core.ejb.SlotEJB;
import org.openepics.discs.ccdb.core.security.SecurityPolicy;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.User;
import org.openepics.discs.ccdb.model.cm.Phase;
import org.openepics.discs.ccdb.model.cm.PhaseApproval;
import org.openepics.discs.ccdb.model.cm.PhaseAssignment;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * Description: State for Manage Process View
 *
 * Methods:
 * <p>
 * Init: to initialize the state
 * <p>
 * resetInput: reset all inputs on the view
 * <p>
 * onRowSelect: things to do when an item is selected
 * <p>
 * onAddCommand: things to do before adding an item
 * <p>
 * onEditCommand: things to do before editing an item
 * <p>
 * onDeleteCommand: things to do before deleting an item
 * <p>
 * saveXXXX: save the input or edited item
 * <p>
 * deleteXXXX: delete the selected item
 *
 * @author vuppala
 *
 */

@Named
@ViewScoped
public class AssignmentManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;
    @EJB
    private LifecycleEJB reviewEJB;
    @EJB 
    private SlotEJB slotEJB;
    @EJB 
    private AuthEJB authEJB;
    @Inject
    private SecurityPolicy securityPolicy;
    
    private static final Logger LOGGER = Logger.getLogger(AssignmentManager.class.getName());
//    @Inject
//    UserSession userSession;
      
    private List<PhaseAssignment> entities;    
    private List<PhaseAssignment> filteredEntities;    
    private PhaseAssignment inputEntity;
    private PhaseAssignment selectedEntity;
    private InputAction inputAction;
    private List<User> inputApprovers = new ArrayList<>();
    
    private List<Slot> slots;
    private List<Phase> phases;
    private List<User> users;
    
    public AssignmentManager() {
    }
    
    @PostConstruct
    public void init() {      
        entities = reviewEJB.findAllAssignments();
        phases = reviewEJB.findAllPhases();
        slots = slotEJB.findAll();
        users = authEJB.findAllUsers();
        resetInput();
    }
    
    private void resetInput() {                
        inputAction = InputAction.READ;
    }
    
    public void onRowSelect(SelectEvent event) {
        inputApprovers.clear();
        for (PhaseApproval approval: selectedEntity.getApprovals()) {
            if (approval.getAssignedApprover() != null) inputApprovers.add(approval.getAssignedApprover());
        }
        // inputRole = selectedRole;
        // Utility.showMessage(FacesMessage.SEVERITY_INFO, "Role Selected", "");
    }
    
    public void onAddCommand(ActionEvent event) {
        inputEntity = new PhaseAssignment();
        inputAction = InputAction.CREATE;
        String userId = securityPolicy.getUserId();
        if (userId == null) {
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                    "Not authorized. User Id is null.");
            return;
        }
        User user = new User(userId);
        inputEntity.setRequestor(user);
    }
    
    public void onEditCommand(ActionEvent event) {
        inputAction = InputAction.UPDATE;
    }
    
    public void onDeleteCommand(ActionEvent event) {
        inputAction = InputAction.DELETE;
    }
    
    public void saveEntity() {
        try {            
            if (inputAction == InputAction.CREATE) {
                reviewEJB.saveAssignment(inputEntity, inputApprovers);
                entities.add(inputEntity);                
            } else {
                reviewEJB.saveAssignment(selectedEntity, inputApprovers);
            }
            resetInput();
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Success", "Saved");
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Failure", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }
    
    public void deleteEntity() {
        try {
            reviewEJB.deleteAssignment(selectedEntity);
            entities.remove(selectedEntity);  
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Success", "Deletion was successful. However, you may have to refresh the page.");
            resetInput();
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Failure", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }
    
    //-- Getters/Setters 
    
    public InputAction getInputAction() {
        return inputAction;
    }

    public List<PhaseAssignment> getEntities() {
        return entities;
    }

    public List<PhaseAssignment> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<PhaseAssignment> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public PhaseAssignment getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(PhaseAssignment inputEntity) {
        this.inputEntity = inputEntity;
    }

    public PhaseAssignment getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(PhaseAssignment selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<User> getInputApprovers() {
        return inputApprovers;
    }

    public void setInputApprovers(List<User> inputApprovers) {
        this.inputApprovers = inputApprovers;
    }

    
}
