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
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.openepics.discs.ccdb.core.ejb.ReviewEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.cm.Review;

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
public class ProcessManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;
    @EJB
    private ReviewEJB reviewEJB;
            
    private static final Logger LOGGER = Logger.getLogger(ProcessManager.class.getName());
//    @Inject
//    UserSession userSession;
      
    private List<Review> entities;    
    private List<Review> filteredEntities;    
    private Review inputEntity;
    private Review selectedEntity;
    private InputAction inputAction;
    
    
    public ProcessManager() {
    }
    
    @PostConstruct
    public void init() {      
        entities = reviewEJB.findAllReviews();     
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
        inputEntity = new Review();
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
                reviewEJB.saveProcess(inputEntity);
                entities.add(inputEntity);                
            } else {
                reviewEJB.saveProcess(selectedEntity);
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
            reviewEJB.deleteProcess(selectedEntity);
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

    public List<Review> getEntities() {
        return entities;
    }

    public List<Review> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<Review> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public Review getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(Review inputEntity) {
        this.inputEntity = inputEntity;
    }

    public Review getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Review selectedEntity) {
        this.selectedEntity = selectedEntity;
    }
}
