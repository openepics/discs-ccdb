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
import org.openepics.discs.ccdb.core.ejb.LcApprovalEJB;
import org.openepics.discs.ccdb.core.ejb.LcStatusEJB;
import org.openepics.discs.ccdb.model.LifecycleApprovalRecord;
import org.openepics.discs.ccdb.model.LifecycleStatus;
import org.openepics.discs.ccdb.model.Slot;

/**
 * Bean to support lifecycle approval report
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
    
    private static final Logger logger = Logger.getLogger(ApprovalReport.class.getName());
    @EJB
    private LcApprovalEJB lifecycleEJB;
    
    private List<LifecycleApprovalRecord> statusList;
    private List<LifecycleApprovalRecord> filteredStatus;
    private Set<String> slotNames = new HashSet<>();
  
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
        statusList = lifecycleEJB.findAll();
        logger.log(Level.INFO, "Size of LC sttaus list: {0}", statusList.size());
        for(LifecycleApprovalRecord lcstat: statusList) {
              if (lcstat.getRequirement().getSlot() != null) {
                  slotNames.add(lcstat.getRequirement().getSlot().getName());
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
          
          for(LifecycleApprovalRecord lcstat: statusList) {
              if (slot.equals(lcstat.getRequirement().getSlot().getName()) && phase.equals(lcstat.getRequirement().getPhase().getName())) {
                  if (lcstat.isApproved()) {
                      return "Approved by " + (lcstat.getApproved_by() == null? " " : lcstat.getApproved_by().getUserId()) + ", " + lcstat.getApproved_at();
                  };
              }
          }
          
        return "";
    }
    
    // getters and setters

    public List<LifecycleApprovalRecord> getFilteredStatus() {
        return filteredStatus;
    }

    public void setFilteredStatus(List<LifecycleApprovalRecord> filteredStatus) {
        this.filteredStatus = filteredStatus;
    }

    public List<LifecycleApprovalRecord> getStatusList() {
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
