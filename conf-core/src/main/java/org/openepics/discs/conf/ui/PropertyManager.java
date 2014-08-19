/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openepics.discs.conf.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.openepics.discs.conf.dl.PropertiesLoaderQualifier;
import org.openepics.discs.conf.dl.common.DataLoader;
import org.openepics.discs.conf.dl.common.DataLoaderResult;
import org.openepics.discs.conf.ejb.ConfigurationEJB;
import org.openepics.discs.conf.ent.Property;
import org.openepics.discs.conf.ent.PropertyAssociation;
import org.openepics.discs.conf.security.SecurityPolicy;
import org.openepics.discs.conf.ui.common.DataLoaderHandler;
import org.openepics.discs.conf.util.Utility;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import com.google.common.io.ByteStreams;

/**
 *
 * @author vuppala
 * @author Miroslav Pavleski <miroslav.pavleski@cosylab.com>
 * 
 */
@Named
@ViewScoped
public class PropertyManager implements Serializable {
    @Inject private ConfigurationEJB configurationEJB;
    @EJB private SecurityPolicy securityPolicy;
    
    @Inject private DataLoaderHandler dataLoaderHandler;
    @Inject @PropertiesLoaderQualifier private DataLoader propertiesDataLoader;
    private static final Logger logger = Logger.getLogger(PropertyManager.class.getCanonicalName());

    private List<Property> objects;
    private List<Property> sortedObjects;
    private List<Property> filteredObjects;
    private Property selectedObject;
    private Property inputObject;

    private byte[] importData;
    private String importFileName;
    private DataLoaderResult loaderResult;

    private boolean inTrans = false; // in the middle of an operations
    private char selectedOp = 'n'; // selected operation: [a]dd, [e]dit, [d]elete, [n]one

    /**
     * Creates a new instance of PropertyManager
     */
    public PropertyManager() {
    }

    @PostConstruct
    public void init() {
        try {
            objects = configurationEJB.findProperties();
            // objects.add(new Property());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) {
        inputObject = selectedObject;
        Utility.showMessage(FacesMessage.SEVERITY_INFO, "Selected", "");
    }

    public void onAdd(ActionEvent event) {
        selectedOp = 'a';
        inTrans = true;
        // TODO replaced void constructor (now protected) with default values. Check!
        inputObject = new Property("", "", PropertyAssociation.ALL, securityPolicy.getUserId());
        Utility.showMessage(FacesMessage.SEVERITY_INFO, "Add", "");
    }

    public void onEdit(ActionEvent event) {
        selectedOp = 'e';
        inTrans = true;
        inputObject = selectedObject;
        Utility.showMessage(FacesMessage.SEVERITY_INFO, "Edit", "");
    }

    public void onDelete(ActionEvent event) {
        try {
            configurationEJB.deleteProperty(selectedObject);
            objects.remove(selectedObject);
            selectedObject = null;
            inputObject = null;
            Utility.showMessage(FacesMessage.SEVERITY_INFO, "Deleted", "");
        } catch (Exception e) {
            Utility.showMessage(FacesMessage.SEVERITY_INFO, "Deleted", "");
        } finally {

        }
    }

    public void onSave(ActionEvent event) {
        try {
            inputObject.setAssociation(PropertyAssociation.TYPE);
            inputObject.setModifiedBy("test-user");

            if (selectedOp == 'a') {
                configurationEJB.addProperty(inputObject);
                selectedObject = inputObject;
                objects.add(selectedObject);
            } else if (selectedOp == 'e') {
                configurationEJB.saveProperty(inputObject);
            }
            Utility.showMessage(FacesMessage.SEVERITY_INFO, "Saved", "");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            Utility.showMessage(FacesMessage.SEVERITY_INFO, "Error", "");
        } finally {
            inTrans = false;
            selectedOp = 'n';
        }
    }

    public void onCancel(ActionEvent event) {
        selectedOp = 'n';
        inputObject = selectedObject;
        inTrans = false;
        Utility.showMessage(FacesMessage.SEVERITY_INFO, "Cancelled", "");
    }

    // ---------------------------------
    public List<Property> getSortedObjects() {
        return sortedObjects;
    }

    public void setSortedObjects(List<Property> sortedObjects) {
        this.sortedObjects = sortedObjects;
    }

    public List<Property> getFilteredObjects() {
        return filteredObjects;
    }

    public void setFilteredObjects(List<Property> filteredObjects) {
        this.filteredObjects = filteredObjects;
    }

    public Property getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(Property selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Property getInputObject() {
        return inputObject;
    }

    public void setInputObject(Property inputObject) {
        this.inputObject = inputObject;
    }

    public List<Property> getObjects() {
        return objects;
    }

    public boolean isInTrans() {
        return inTrans;
    }

    public String getImportFileName() { return importFileName; }

    public void doImport() {
        final InputStream inputStream = new ByteArrayInputStream(importData);
        loaderResult = dataLoaderHandler.loadData(inputStream, propertiesDataLoader);
    }

    public void prepareImportPopup() {
        importData = null;
        importFileName = null;
    }

    public DataLoaderResult getLoaderResult() { return loaderResult; }

    public void handleImportFileUpload(FileUploadEvent event) {
        try (InputStream inputStream = event.getFile().getInputstream()) {
            this.importData = ByteStreams.toByteArray(inputStream);
            this.importFileName = FilenameUtils.getName(event.getFile().getFileName());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
