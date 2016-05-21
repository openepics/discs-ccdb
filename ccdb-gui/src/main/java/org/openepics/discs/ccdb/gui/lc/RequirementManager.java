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
import org.openepics.discs.ccdb.core.ejb.ReviewEJB;
import org.openepics.discs.ccdb.core.ejb.SlotEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.User;
import org.openepics.discs.ccdb.model.cm.Review;
import org.openepics.discs.ccdb.model.cm.ReviewRequirement;

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
public class RequirementManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;
    @EJB
    private ReviewEJB reviewEJB;
    @EJB 
    private SlotEJB slotEJB;
    @EJB 
    private AuthEJB authEJB;
    
    private static final Logger LOGGER = Logger.getLogger(RequirementManager.class.getName());
//    @Inject
//    UserSession userSession;
      
    private List<ReviewRequirement> entities;    
    private List<ReviewRequirement> filteredEntities;    
    private ReviewRequirement inputEntity;
    private ReviewRequirement selectedEntity;
    private InputAction inputAction;
    private List<User> inputApprovers = new ArrayList<>();
    
    private List<Slot> slots;
    private List<Review> processes;
    private List<User> users;
    
    public RequirementManager() {
    }
    
    @PostConstruct
    public void init() {      
        entities = reviewEJB.findAllRequirements();
        processes = reviewEJB.findAllReviews();
        slots = slotEJB.findAll();
        users = authEJB.findAllUsers();
        resetInput();
    }
    
    private void resetInput() {                
        inputAction = InputAction.READ;
    }
    
    public void onRowSelect(SelectEvent event) {
        
        // inputRole = selectedRole;
        // Utility.showMessage(FacesMessage.SEVERITY_INFO, "Role Selected", "");
    }
    
    public void onAddCommand(ActionEvent event) {
        inputEntity = new ReviewRequirement();
        inputAction = InputAction.CREATE;       
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
                reviewEJB.saveRequirement(inputEntity, inputApprovers);
                entities.add(inputEntity);                
            } else {
                reviewEJB.saveRequirement(selectedEntity, inputApprovers);
            }
            resetInput();
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Process saved", "");
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Could not save process", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }
    
    public void deleteEntity() {
        try {
            reviewEJB.deleteRequirement(selectedEntity);
            entities.remove(selectedEntity);  
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Deletion successful", "You may have to refresh the page.");
            resetInput();
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Could not complete deletion", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }
    
    //-- Getters/Setters 
    
    public InputAction getInputAction() {
        return inputAction;
    }

    public List<ReviewRequirement> getEntities() {
        return entities;
    }

    public List<ReviewRequirement> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<ReviewRequirement> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public ReviewRequirement getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(ReviewRequirement inputEntity) {
        this.inputEntity = inputEntity;
    }

    public ReviewRequirement getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(ReviewRequirement selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public List<Review> getProcesses() {
        return processes;
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
