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
package org.openepics.discs.ccdb.gui.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.ccdb.core.ejb.LifecycleEJB;
import org.openepics.discs.ccdb.model.cm.PhaseStatus;

/**
 * Bean to support lifecycle phase report
 *
 * @author vuppala
 *
 */
@Named
@ViewScoped
public class StatusReport implements Serializable {

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
    
    private static final Logger LOGGER = Logger.getLogger(StatusReport.class.getName());
    @EJB
    private LifecycleEJB lifecycleEJB;
    
    private List<PhaseStatus> statusList;
    private List<PhaseStatus> filteredStatus;
    private Set<String> slotNames = new HashSet<>();
  
    private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("ARR", "DHR");
     
    private String columnTemplate = "ARR DHR";
     
    private List<ColumnModel> columns;
    
    public StatusReport() {
        
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        statusList = lifecycleEJB.findAllStatuses();
        LOGGER.log(Level.INFO, "Size of LC sttaus list: {0}", statusList.size());
        for(PhaseStatus lcstat: statusList) {
              if (lcstat.getAssignment().getSlot() != null) {
                  slotNames.add(lcstat.getAssignment().getSlot().getName());
              }
          }
        createDynamicColumns();
    }

    public void updateColumns() {
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:cars");
        table.setValueExpression("sortBy", null);
         
        //update columns
        createDynamicColumns();
    }
    
    private void createDynamicColumns() {
        String[] columnKeys = columnTemplate.split(" ");
        columns = new ArrayList<>();   
         
        for(String columnKey : columnKeys) {
            String key = columnKey.trim();
             
            if(VALID_COLUMN_KEYS.contains(key)) {
                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
            }
        }
    }
    
    public String getStatus(String slot, String phase) {
       
          if (slot == null || phase == null ) {
              return "";
          }
          
          for(PhaseStatus lcstat: statusList) {
//              if (slot.equals(lcstat.getRequirement().getSlot().getName()) && phase.equals(lcstat.getRequirement().getPhase().getName())) {
                  return lcstat.getStatus().getName();
//              }
          }
          
        return "N/R";
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

    public String getColumnTemplate() {
        return columnTemplate;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public Set<String> getSlotNames() {
        return slotNames;
    }

    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }
   
}
