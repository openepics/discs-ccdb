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
import org.openepics.discs.ccdb.core.ejb.SlotEJB;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.cm.SlotGroup;

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
public class CmSlotManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;

    @EJB
    private SlotEJB slotEJB;
    @EJB
    private LifecycleEJB lcEJB;

    private static final Logger LOGGER = Logger.getLogger(CmSlotManager.class.getName());
//    @Inject
//    UserSession userSession;

    private List<Slot> entities;
    private List<Slot> filteredEntities;
    private Slot inputEntity;
    private Slot selectedEntity;
    private InputAction inputAction;
    private List<SlotGroup> slotGroups;

    public CmSlotManager() {
    }

    @PostConstruct
    public void init() {
        entities = slotEJB.findByIsHostingSlot(true);
        slotGroups = lcEJB.findAllSlotGroups();
        selectedEntity = entities.get(0);
        resetInput();
    }

    private void resetInput() {
        inputAction = InputAction.READ;
    }

    public void onRowSelect(SelectEvent event) {
        // inputRole = selectedRole;
        // Utility.showMessage(FacesMessage.SEVERITY_INFO, "Role Selected", "");
    }

//    public void onAddCommand(ActionEvent event) {
//        inputEntity = new Slot();
//        inputAction = InputAction.CREATE;       
//    }
    public void onEditCommand(ActionEvent event) {
        inputAction = InputAction.UPDATE;
    }

    public void onDeleteCommand(ActionEvent event) {
        inputAction = InputAction.DELETE;
    }

    public void saveEntity() {
        try {
            Slot slot; 
            if (inputAction == InputAction.CREATE) {
                slotEJB.save(inputEntity);
                entities.add(inputEntity);
                slot = inputEntity;
            } else {
                slotEJB.save(selectedEntity);
                slot = selectedEntity;
            }
//            if (slot.getCmGroup() == null) {
//                UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Make sure that corresponding checklist is updated", "");
//            } else {
//                if (lcEJB.findAssignment(slot.getCmGroup()) != null) {
//                    
//                }
//            }
            resetInput();
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Make sure that corresponding checklist is updated", "");
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Could not save ", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);
        }
    }

    public void deleteEntity() {
        try {
            slotEJB.delete(selectedEntity);
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

    public List<Slot> getEntities() {
        return entities;
    }

    public List<Slot> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<Slot> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public Slot getInputEntity() {
        return inputEntity;
    }

    public void setInputEntity(Slot inputEntity) {
        this.inputEntity = inputEntity;
    }

    public Slot getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Slot selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<SlotGroup> getSlotGroups() {
        return slotGroups;
    }
}
