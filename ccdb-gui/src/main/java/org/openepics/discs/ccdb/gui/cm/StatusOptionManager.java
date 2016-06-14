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

package org.openepics.discs.ccdb.gui.cm;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.cm.Phase;
import org.openepics.discs.ccdb.model.cm.PhaseGroup;
import org.openepics.discs.ccdb.model.cm.StatusOption;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * Description: State for Manage Phase View
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
public class StatusOptionManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;
    @EJB
    private LifecycleEJB lcEJB;
            
    private static final Logger LOGGER = Logger.getLogger(StatusOptionManager.class.getName());
//    @Inject
//    UserSession userSession;
      
    private List<StatusOption> entities;    
    private List<StatusOption> filteredEntities;    
    private StatusOption inputEntity;
    private StatusOption selectedEntity;
    private InputAction inputAction;
    
    private List<PhaseGroup> phaseGroups;
    
    public StatusOptionManager() {
    }
    
    @PostConstruct
    public void init() { 
        phaseGroups = lcEJB.findAllPhaseGroups();
        entities = lcEJB.findAllStatusOptions();
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
        inputEntity = new StatusOption();
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
                lcEJB.saveStatusOption(inputEntity);
                entities.add(inputEntity);                
            } else {
                lcEJB.saveStatusOption(selectedEntity);
            }
            resetInput();
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Saved", "");
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Could not save", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }
    
    public void deleteEntity() {
        try {
            lcEJB.deleteStatusOption(selectedEntity);
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

    public List<StatusOption> getEntities() {
        return entities;
    }

    public List<StatusOption> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<StatusOption> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public StatusOption getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(StatusOption inputEntity) {
        this.inputEntity = inputEntity;
    }

    public StatusOption getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(StatusOption selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<PhaseGroup> getPhaseGroups() {
        return phaseGroups;
    }
}
