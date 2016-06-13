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
package org.openepics.discs.ccdb.gui.cm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.model.Device;
import org.openepics.discs.ccdb.model.Slot;
import org.openepics.discs.ccdb.model.cm.Phase;
import org.openepics.discs.ccdb.model.cm.PhaseApproval;
import org.openepics.discs.ccdb.model.cm.PhaseGroup;

/**
 * Bean to support lifecycle phase report
 *
 * @author vuppala
 *
 */
@Named
@ViewScoped
public class ApprovalReport implements Serializable {

    static public class ColumnModel implements Serializable {
 
        private String header;
        private String property;
 
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }
    }
    
    private static final Logger LOGGER = Logger.getLogger(ApprovalReport.class.getName());
    @EJB
    private LifecycleEJB lcEJB;
    
    private List<PhaseApproval> statusList;
    private List<PhaseApproval> filteredStatus;
    private List<Phase> phases;
    private Set<Slot> slots;
    private Set<Device> devices;
    private Set<String> slotNames = new HashSet<>();
    private String selectedType;
    private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("ARR", "DHR");
     
    private String columnTemplate = "ARR DHR";
     
    private List<ColumnModel> columns;
    
    public ApprovalReport() {
        
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        initialize();
        
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
            statusList = lcEJB.findAllApprovals();
            phases = lcEJB.findAllPhases();
        } else {                 
            statusList = lcEJB.findApprovals(stype); 
            phases = lcEJB.findPhases(stype);
        }
        slots = statusList.stream().filter(stat -> stat.getAssignment().getSlot() != null).map(stat -> stat.getAssignment().getSlot()).collect(Collectors.toSet());
        devices = statusList.stream().filter(stat -> stat.getAssignment().getDevice() != null).map(stat -> stat.getAssignment().getDevice()).collect(Collectors.toSet());
//        for(PhaseApproval lcstat: statusList) {
//              if (lcstat.getAssignment().getSlot() != null) {
//                  slotNames.add(lcstat.getAssignment().getSlot().getName());
//              }
//          }
//        createDynamicColumns();
        return nextView;
    }
    
//    public void updateColumns() {
//        //reset table state
//        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:cars");
//        table.setValueExpression("sortBy", null);
//         
//        //update columns
//        createDynamicColumns();
//    }
//    
//    private void createDynamicColumns() {
//        String[] columnKeys = columnTemplate.split(" ");
//        columns = new ArrayList<>();   
//        
//        phases.stream().map(phase -> phase.getName())
//        for(String columnKey : columnKeys) {
//            String key = columnKey.trim();
//             
//            if(VALID_COLUMN_KEYS.contains(key)) {
//                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
//            }
//        }
//    }
    
//    public String getSlotSumOk(Slot slot) {
//       
//          if (slot == null ) {
//              return "";
//          }
//          
//          for(PhaseApproval lcstat: statusList) {
//              if (slot.equals(lcstat.getAssignment().getSlot())) {
//                  if (lcstat.getStatus() == null || "No".equals(lcstat.getStatus().getName())) {
//                      return "No";
//                  }                
//             }
//          }
//          
//        return "Yes";
//    }
//
//    public String getDeviceSumOk(Device device) {
//       
//          if (device == null ) {
//              return "";
//          }
//          
//          for(PhaseApproval lcstat: statusList) {
//              if (device.equals(lcstat.getAssignment().getDevice())) {
//                  if (lcstat.getStatus() == null || "No".equals(lcstat.getStatus().getName())) {
//                      return "No";
//                  }                
//             }
//          }
//          
//        return "Yes";
//    }   
//    
    public PhaseApproval getStatusRec(Slot slot, Phase phase) {
       
          if (slot == null || phase == null ) {
              return null;
          }
          
          for(PhaseApproval lcstat: statusList) {
              if (slot.equals(lcstat.getAssignment().getSlot()) && phase.equals(lcstat.getPhaseOfGroup().getPhase())) {
                  return lcstat;
             }
          }
          
        return null;
    }
    
//    public String getDeviceStatus(Device device, Phase phase) {
//       
//          if (device == null || phase == null ) {
//              return "";
//          }
//          
//          for(PhaseApproval lcstat: statusList) {
//              if (device.equals(lcstat.getAssignment().getDevice()) && phase.equals(lcstat.getAssignment().getPhase())) {
//                  return lcstat.getStatus() == null? "" : lcstat.getStatus().getName();
//             }
//          }
//          
//        return "N/R";
//    }
//    
    public PhaseApproval getDeviceStatusRec(Device device, Phase phase) {
       
          if (device == null || phase == null ) {
              return null;
          }
          
          for(PhaseApproval lcstat: statusList) {
              if (device.equals(lcstat.getAssignment().getDevice()) && phase.equals(lcstat.getPhaseOfGroup().getPhase())) {
                  return lcstat;
             }
          }
          
        return null;
    }
    
    // getters and setters

    public List<PhaseApproval> getFilteredStatus() {
        return filteredStatus;
    }

    public void setFilteredStatus(List<PhaseApproval> filteredStatus) {
        this.filteredStatus = filteredStatus;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public Set<Slot> getSlots() {
        return slots;
    }   

    public Set<Device> getDevices() {
        return devices;
    }
    
}
