/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.ccdb.gui.ui;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.openepics.discs.ccdb.model.cm.LevelOfCare;
import org.openepics.discs.ccdb.model.cm.PhaseTag;

/**
 * Bean for basic data like level of care, checklists etc
 * 
 * @author vuppala
 */
@Named(value = "baseDataManager")
@RequestScoped
public class BaseDataManager {

    /**
     * Creates a new instance of BaseDataManager
     */
    public BaseDataManager() {
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    /**
     * Level of Care options
     * @return 
     */
    public LevelOfCare[] getLevelOfCares() {
        return LevelOfCare.values();
    }
    
    /**
     * Level of Care options
     * @return 
     */
    public PhaseTag[] getPhaseTags() {
        return PhaseTag.values();
    }
}
