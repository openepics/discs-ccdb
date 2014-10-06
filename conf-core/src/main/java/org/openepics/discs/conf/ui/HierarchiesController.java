/*
 * Copyright (c) 2014 European Spallation Source
 * Copyright (c) 2014 Cosylab d.d.
 *
 * This file is part of Controls Configuration Database.
 *
 * Controls Configuration Database is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the License,
 * or any newer version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see https://www.gnu.org/licenses/gpl-2.0.txt
 */
package org.openepics.discs.conf.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.openepics.discs.conf.ejb.InstallationEJB;
import org.openepics.discs.conf.ejb.SlotEJB;
import org.openepics.discs.conf.ejb.SlotPairEJB;
import org.openepics.discs.conf.ent.ComptypeArtifact;
import org.openepics.discs.conf.ent.ComptypePropertyValue;
import org.openepics.discs.conf.ent.Device;
import org.openepics.discs.conf.ent.DeviceArtifact;
import org.openepics.discs.conf.ent.DevicePropertyValue;
import org.openepics.discs.conf.ent.InstallationRecord;
import org.openepics.discs.conf.ent.Slot;
import org.openepics.discs.conf.ent.SlotArtifact;
import org.openepics.discs.conf.ent.SlotPair;
import org.openepics.discs.conf.ent.SlotPropertyValue;
import org.openepics.discs.conf.ent.Tag;
import org.openepics.discs.conf.views.EntityAttributeView;
import org.openepics.discs.conf.views.SlotRelationshipView;
import org.openepics.discs.conf.views.SlotView;
import org.primefaces.model.TreeNode;

import com.google.common.base.Preconditions;

/**
 * @author Miha Vitorovič <miha.vitorovic@cosylab.com>
 *
 */
@Named
@ViewScoped
public class HierarchiesController implements Serializable {
    private static final Logger logger = Logger.getLogger(HierarchiesController.class.getCanonicalName());

    @Inject private SlotsTreeBuilder slotsTreeBuilder;
    @Inject private SlotEJB slotEJB;
    @Inject private SlotPairEJB slotPairEJB;
    @Inject private InstallationEJB installationEJB;

    private TreeNode rootNode;
    private TreeNode selectedNode;
    private InstallationRecord installationRecord;
    private Device deviceToInstall;
    private Slot selectedSlot;

    @PostConstruct
    public void init() {
        rootNode = slotsTreeBuilder.newSlotsTree(slotEJB.findAll(), null, true);
    }

    public List<EntityAttributeView> getAttributes() {
        final List<EntityAttributeView> attributesList = new ArrayList<>();

        if (selectedNode != null) {
            final String slotType = selectedSlot.isHostingSlot() ? "Installation slot" : "Container";

            for (ComptypePropertyValue value : selectedSlot.getComponentType().getComptypePropertyList()) {
                attributesList.add(new EntityAttributeView(value, "Type property"));
            }

            for (SlotPropertyValue value : selectedSlot.getSlotPropertyList()) {
                attributesList.add(new EntityAttributeView(value, slotType + " property"));
            }

            if (installationRecord != null) {
                for (DevicePropertyValue value : installationRecord.getDevice().getDevicePropertyList()) {
                    attributesList.add(new EntityAttributeView(value, "Device property"));
                }
            }

            for (ComptypeArtifact artifact : selectedSlot.getComponentType().getComptypeArtifactList()) {
                attributesList.add(new EntityAttributeView(artifact, "Type artifact"));
            }

            for (SlotArtifact artifact : selectedSlot.getSlotArtifactList()) {
                attributesList.add(new EntityAttributeView(artifact, slotType + " artifact"));
            }

            if (installationRecord != null) {
                for (DeviceArtifact artifact : installationRecord.getDevice().getDeviceArtifactList()) {
                    attributesList.add(new EntityAttributeView(artifact, "Device artifact"));
                }
            }

            for (Tag tag : selectedSlot.getComponentType().getTags()) {
                attributesList.add(new EntityAttributeView(tag, "Type tag"));
            }

            for (Tag tag : selectedSlot.getTags()) {
                attributesList.add(new EntityAttributeView(tag, slotType + " tag"));
            }

            if (installationRecord != null) {
                for (Tag tag : installationRecord.getDevice().getTags()) {
                    attributesList.add(new EntityAttributeView(tag, "Device tag"));
                }
            }
        }
        return attributesList;
    }

    public Slot getSelectedNodeSlot() {
        return selectedSlot;
    }

    public List<SlotRelationshipView> getRelationships() {
        final List<SlotRelationshipView> relationships = new ArrayList<>();
        if (selectedNode != null) {
            final List<SlotPair> slotPairs = slotPairEJB.getSlotRleations(selectedSlot);

            for (SlotPair slotPair : slotPairs) {
                relationships.add(new SlotRelationshipView(slotPair, selectedSlot));
            }
        }
        return relationships;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        this.selectedSlot = selectedNode == null ? null : ((SlotView)selectedNode.getData()).getSlot();
        this.installationRecord = selectedNode == null ? null
                                    : installationEJB.getActiveInstallationRecordForSlot(selectedSlot);
    }

    public Device getInstalledDevice() {
        return installationRecord == null ? null : installationRecord.getDevice();
    }


    public List<Device> getUninstalledDevices() {
        if (selectedSlot == null || !selectedSlot.isHostingSlot()) {
            return new ArrayList<Device>();
        }
        return installationEJB.getUninstalledDevices(selectedSlot.getComponentType());
    }

    /**
     * @return the deviceToInstall
     */
    public Device getDeviceToInstall() {
        return deviceToInstall;
    }

    /**
     * @param deviceToInstall the deviceToInstall to set
     */
    public void setDeviceToInstall(Device deviceToInstall) {
        this.deviceToInstall = deviceToInstall;
    }

    public void installDevice() {
        Preconditions.checkNotNull(deviceToInstall);
        Preconditions.checkNotNull(selectedSlot);
        // we must check whether the selected slot is already occupied or selected device is already installed
        final InstallationRecord slotCheck = installationEJB.getActiveInstallationRecordForSlot(selectedSlot);
        if (slotCheck != null) {
            logger.log(Level.WARNING, "An attempt was made to install a device in an already occupied slot.");
            throw new RuntimeException("Slot already occupied.");
        }
        final InstallationRecord deviceCheck = installationEJB.getActiveInstallationRecordForDevice(deviceToInstall);
        if (deviceCheck != null) {
            logger.log(Level.WARNING, "An attempt was made to install a device that is already installed.");
            throw new RuntimeException("Device already installed.");
        }

        final Date today = new Date();
        final InstallationRecord newRecord = new InstallationRecord(Long.toString(today.getTime()), today);
        newRecord.setDevice(deviceToInstall);
        newRecord.setSlot(selectedSlot);
        installationEJB.add(newRecord);

        deviceToInstall = null;
        installationRecord = installationEJB.getActiveInstallationRecordForSlot(selectedSlot);
    }
}
