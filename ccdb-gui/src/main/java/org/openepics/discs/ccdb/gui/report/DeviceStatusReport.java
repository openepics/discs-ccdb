/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.ccdb.gui.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.openepics.discs.ccdb.core.ejb.SlotEJB;
import org.openepics.discs.ccdb.model.Device;
import org.openepics.discs.ccdb.model.Slot;

/**
 *
 * @author vuppala
 */
@Named
@ViewScoped
public class DeviceStatusReport implements Serializable {
    
    @Inject private SlotEJB slotEJB;
    private static final Logger logger = Logger.getLogger(DeviceStatusReport.class.getName());

    
    
    public static class SlotReportEntry {
        private Slot slot;
        private String area;
        private Device installedDevice = null;
        private boolean devOwnerApproval = false;

        private SlotReportEntry() {
            
        }
        
        public static SlotReportEntry generateInstance(Slot slot) {
            SlotReportEntry sre = new SlotReportEntry();
            
            sre.slot = slot;
            sre.area = "test";
            if (slot.getInstallationRecordList() != null &&  !slot.getInstallationRecordList().isEmpty()) {
                sre.installedDevice = slot.getInstallationRecordList().get(0).getDevice();
            }
            
            return sre;
        }
        // ----
        public Slot getSlot() {
            return slot;
        }

        public String getArea() {
            return area;
        }

        public Device getInstalledDevice() {
            return installedDevice;
        }

        public boolean isDevOwnerApproval() {
            return devOwnerApproval;
        }     
    }
    
    private List<Slot> slots;
    private Slot selectedSlot;
    private List<SlotReportEntry> entries = new ArrayList<>();
    private List<SlotReportEntry> filteredEntries;
    
    public DeviceStatusReport() {
    }

    @PostConstruct
    public void init() {
        slots = slotEJB.findByIsHostingSlot(true);
        logger.log(Level.FINE, "Found number of slots: {0}", slots.size());
        for (Slot slot: slots) {
            entries.add(SlotReportEntry.generateInstance(slot));
        }
    }
    
    // --- getters/setters

    public List<Slot> getSlots() {
        return slots;
    } 

    public Slot getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(Slot selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public List<SlotReportEntry> getEntries() {
        return entries;
    }

    public List<SlotReportEntry> getFilteredEntries() {
        return filteredEntries;
    }

    public void setFilteredEntries(List<SlotReportEntry> filteredEntries) {
        this.filteredEntries = filteredEntries;
    }

}
