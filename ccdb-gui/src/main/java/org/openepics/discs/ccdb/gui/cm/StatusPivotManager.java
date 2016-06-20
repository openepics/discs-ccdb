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

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.openepics.discs.ccdb.core.ejb.AuthEJB;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.core.security.SecurityPolicy;
import org.openepics.discs.ccdb.gui.ui.util.UiUtility;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.auth.User;
import org.openepics.discs.ccdb.model.cm.Phase;
import org.openepics.discs.ccdb.model.cm.PhaseAssignment;
import org.openepics.discs.ccdb.model.cm.PhaseGroup;
import org.openepics.discs.ccdb.model.cm.PhaseStatus;
import org.openepics.discs.ccdb.model.cm.SlotGroup;
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
public class StatusPivotManager implements Serializable {
//    @EJB
//    private AuthEJB authEJB;

    public static class SelectablePhase implements Serializable {

        private Boolean selected = false;
        private Phase phase;

        private SelectablePhase() {

        }

        public SelectablePhase(Phase phase) {
            this.phase = phase;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public Phase getPhase() {
            return phase;
        }

        public void setPhase(Phase phase) {
            this.phase = phase;
        }
    }

    @EJB
    private LifecycleEJB lcEJB;
    @EJB
    private AuthEJB authEJB;
    @Inject
    private SecurityPolicy securityPolicy;

    private static final Logger LOGGER = Logger.getLogger(StatusPivotManager.class.getName());
//    @Inject
//    UserSession userSession;

    private String entityType = "g";
    private List<SlotGroup> slotGroups;
    private List<Phase> phases;
    private List<SelectablePhase> selectablePhases;
    private List<PhaseAssignment> entities;
    private List<PhaseAssignment> filteredEntities;
    private List<StatusOption> statusOptions;
    private List<User> users;
    private PhaseAssignment inputEntity;
    // private PhaseAssignment selectedEntity;
    private InputAction inputAction;
    private StatusOption inputStatus;
    private String selectedType;
    private String inputComment;
    private User inputSME;
    // private boolean phaseNR = false; // Phase not required
    private Boolean allPhasesOptional = false;
    private Boolean phaseSelected = false;

    private List<PhaseAssignment> selectedEntities = new ArrayList<>();

    public StatusPivotManager() {
    }

    @PostConstruct
    public void init() {
        slotGroups = lcEJB.findAllSlotGroups();
        phases = lcEJB.findAllPhases();
        users = authEJB.findAllUsers();
        selectablePhases = phases == null ? Collections.<SelectablePhase>emptyList() : phases.stream().map(p -> new SelectablePhase(p)).collect(Collectors.toList());
        updatePhaseSelected();
        resetInput();
    }

    /**
     * Initialize data in view
     *
     * @return
     */
    public String initialize() {
        String nextView = null;
        PhaseGroup stype = null;

        if (selectedType != null) {
            stype = lcEJB.findPhaseGroup(selectedType);
        }

        if (stype == null) {
            // entities = lcEJB.findAllAssignments();
        } else {
//            entities = lcEJB.findAllValidStatuses();
            // entities = lcEJB.findAssignments(stype);
            statusOptions = lcEJB.findStatusOptions(stype);
        }
        
        switch (entityType) {
            case "g": entities = lcEJB.findGroupAssignments();
            break;
            case "s": entities = lcEJB.findSlotAssignments();
            break;
            case "d": entities = lcEJB.findDeviceAssignments();
            break;
            default: entities = lcEJB.findGroupAssignments();
        }
        return nextView;
    }

    public void resetInput() {
        inputAction = InputAction.READ;
        inputComment = null;
        // phaseNR = false;
        if (statusOptions != null) {
            inputStatus = statusOptions.get(0);
        }
    }

    public void onRowSelect(SelectEvent event) {
    }

    /**
     * Are all the selected phases optional?
     *
     * @return
     */
    private boolean findOptional() {
        // LOGGER.log(Level.INFO, "get all optional", "enter");
        for (SelectablePhase selectedPhase : selectablePhases) {
            if (!selectedPhase.selected) {
                continue;
            }
            for (PhaseAssignment record : selectedEntities) {
                for (PhaseStatus status : record.getStatuses()) {
                    // LOGGER.log(Level.INFO, "checking {0}", status.getGroupMember().getPhase().getName());
                    if (selectedPhase.phase.equals(status.getGroupMember().getPhase()) && status.getGroupMember().getOptional() == false) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updatePhaseSelected() {
        phaseSelected = selectablePhases.stream().anyMatch(p -> p.selected == true);
    }

    public void onAddCommand(ActionEvent event) {
        inputEntity = new PhaseAssignment();
        inputAction = InputAction.CREATE;

    }

    public void onEditCommand(ActionEvent event) {
        inputAction = InputAction.UPDATE;
        allPhasesOptional = findOptional();
        LOGGER.log(Level.INFO, "allphaseOptional {0}", allPhasesOptional);
    }

    public void onDeleteCommand(ActionEvent event) {
        inputAction = InputAction.DELETE;
    }

    /**
     * is input valid?
     *
     * @return
     */
    private boolean inputValid() {
        for (PhaseAssignment record : selectedEntities) {
            for (PhaseStatus status : record.getStatuses()) {
                if (status.getGroupMember().getSummaryPhase() && !isValid(status)) {
                    UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Invalid summary status",
                            "Make sure status for summary (eg AM OK) is valid.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     *
     * @param status
     * @return
     */
    private Integer toInt(StatusOption status) {
        switch (status.getName()) {
            case "NR":
                return 2;
            case "Y":
                return 1;
            case "YC":
                return 0;
            case "N":
                return 0;
        }

        return 0;
    }

    private Integer toInt(PhaseStatus status) {
        for(SelectablePhase selPhase: selectablePhases) {
            if (selPhase.getSelected() && selPhase.getPhase().equals(status.getGroupMember().getPhase())) {
                return toInt(inputStatus);
            }
        }
        return toInt(status.getStatus());
    }
    /**
     *
     * @param summaryStatus
     * @return
     */
    private boolean isValid(PhaseStatus summaryStatus) {
        Integer summaryWeight = summaryWeight(summaryStatus);
        Integer inputWeight = toInt(summaryStatus);

        // LOGGER.log(Level.INFO, "summary status {0}", summaryStatus.getStatus().getName());
        // LOGGER.log(Level.INFO, "weights summary, min: {0} {1} ", new Object[]{inputWeight, summaryWeight});
        return inputWeight <= summaryWeight;
    }

    /**
     *
     * @param status
     * @return
     */
    private Integer summaryWeight(PhaseStatus status) {
        List<PhaseStatus> statuses = lcEJB.findAllStatuses(status.getAssignment());
        Integer minWeight = statuses.stream()
                .filter(stat -> stat.getGroupMember().getSummaryPhase() == false)
                .filter(stat -> stat.getStatus() != null)
                .map(stat -> toInt(stat.getStatus()))
                .min(Integer::compare)
                .get();

        return minWeight;
    }

    /**
     *
     * @param phase
     */
    public void onTogglePhase(Phase phase) {
        selectablePhases.stream().filter(p -> p.phase.equals(phase)).forEach(u -> u.setSelected(!u.selected));
        updatePhaseSelected();
        // UiUtility.showMessage(FacesMessage.SEVERITY_INFO, "Toggle phase ", phase.getName());
    }

    /**
     *
     * @param status
     * @return
     */
    public String summaryStatus(PhaseStatus status) {
        if (!status.getGroupMember().getSummaryPhase()) {
            return "";
        }

        for (PhaseStatus stat : lcEJB.findAllStatuses(status.getAssignment())) {
            if (!stat.getGroupMember().getSummaryPhase() && stat.getStatus() != null && "N".equals(stat.getStatus().getName())) {
                return "N";
            }
        }

        for (PhaseStatus stat : lcEJB.findAllStatuses(status.getAssignment())) {
            if (!stat.getGroupMember().getSummaryPhase() && stat.getStatus() != null && "YC".equals(stat.getStatus().getName())) {
                return "YC";
            }
        }

        return "Y";
    }

    /**
     * When a cell is clicked for edits
     *
     * @param assignment
     * @param phase
     */
    public void onCellEdit(PhaseAssignment assignment, Phase phase) {
        PhaseStatus statusRec = getStatusRec(assignment, phase);
        inputStatus = null;
        inputSME = null;
        if (statusRec != null) {
            inputStatus = statusRec.getStatus();
            inputSME = statusRec.getAssignedSME();
        }
        selectedEntities.clear();
        selectedEntities.add(assignment);

        for (SelectablePhase selectedPhase : selectablePhases) {
            selectedPhase.setSelected(selectedPhase.phase.equals(phase));
        }
        allPhasesOptional = findOptional();
    }

    /**
     *
     */
    public void saveEntity() {
        try {
            Preconditions.checkNotNull(selectedEntities);
            if (!inputValid()) {
                UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Fix validation errors", "");
                RequestContext.getCurrentInstance().addCallbackParam("success", false);
                return;
            }
            String userId = securityPolicy.getUserId();
            if (userId == null) {
                UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Update Failed",
                        "You are not authorized. User Id is null.");
                RequestContext.getCurrentInstance().addCallbackParam("success", false);
                return;
            }
            User user = new User(userId);

            // ToDo: Improve. really bad code
            for (SelectablePhase selectedPhase : selectablePhases) {
                if (!selectedPhase.selected) {
                    continue;
                }
                for (PhaseAssignment record : selectedEntities) {
                    for (PhaseStatus status : record.getStatuses()) {
                        if (selectedPhase.phase.equals(status.getGroupMember().getPhase())) {
                            if (status.getAssignedSME() != null && !status.getAssignedSME().equals(user)) {
                                UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Update Failed",
                                        "You are not authorized to update one or more of the statuses.");
                                RequestContext.getCurrentInstance().addCallbackParam("success", false);
                                return;
                            }
                        }
                    }
                }
            }

            // ToDo: Improve. really bad code
            for (SelectablePhase selectedPhase : selectablePhases) {
                if (!selectedPhase.selected) {
                    continue;
                }
                for (PhaseAssignment record : selectedEntities) {
                    for (PhaseStatus status : record.getStatuses()) {
                        if (selectedPhase.phase.equals(status.getGroupMember().getPhase())) {
                            status.setModifiedAt(new Date());
                            status.setModifiedBy(user.getUserId());
                            status.setStatus(inputStatus);
                            status.setComment(inputComment);
                            status.setAssignedSME(inputSME);
                            lcEJB.savePhaseStatus(status);
                        }
                    }
                }
            }
            RequestContext.getCurrentInstance().addCallbackParam("success", true);
            UiUtility.showMessage(FacesMessage.SEVERITY_INFO, UiUtility.MESSAGE_SUMMARY_SUCCESS,
                    "Update successful.");
        } catch (Exception e) {
            UiUtility.showMessage(FacesMessage.SEVERITY_ERROR, "Could not complete approval", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("success", false);
            System.out.println(e);

        } finally {
//            selectedApproval = null;        
               resetInput();
        }
    }

    public PhaseStatus getStatusRec(PhaseAssignment assignment, Phase phase) {

        if (assignment == null || phase == null) {
            LOGGER.log(Level.WARNING, "assignment or phase is null");
            return null;
        }
        for (PhaseStatus status : assignment.getStatuses()) {
            // LOGGER.log(Level.INFO, "phase {0}", status.getGroupMember().getPhase());
            if (phase.equals(status.getGroupMember().getPhase())) {
                return status;
            }
        }

        return null;
    }
    //-- Getters/Setters 

    public InputAction getInputAction() {
        return inputAction;
    }

    public List<PhaseAssignment> getEntities() {
        return entities;
    }

    public void setEntities(List<PhaseAssignment> entities) {
        this.entities = entities;
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

    public List<PhaseAssignment> getSelectedEntities() {
        return selectedEntities;
    }

    public void setSelectedEntities(List<PhaseAssignment> selectedEntities) {
        this.selectedEntities = selectedEntities;
    }

    public List<SlotGroup> getSlotGroups() {
        return slotGroups;
    }

    public StatusOption getInputStatus() {
        return inputStatus;
    }

    public void setInputStatus(StatusOption inputStatus) {
        this.inputStatus = inputStatus;
    }

    public String getInputComment() {
        return inputComment;
    }

    public void setInputComment(String inputComment) {
        this.inputComment = inputComment;
    }

    public List<StatusOption> getStatusOptions() {
        return statusOptions;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public Boolean getAllPhasesOptional() {
        return allPhasesOptional;
    }

    public List<SelectablePhase> getSelectablePhases() {
        return selectablePhases;
    }

    public void setSelectablePhases(List<SelectablePhase> selectablePhases) {
        this.selectablePhases = selectablePhases;
    }

    public Boolean getPhaseSelected() {
        return phaseSelected;
    }

    public User getInputSME() {
        return inputSME;
    }

    public void setInputSME(User inputSME) {
        this.inputSME = inputSME;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

}
