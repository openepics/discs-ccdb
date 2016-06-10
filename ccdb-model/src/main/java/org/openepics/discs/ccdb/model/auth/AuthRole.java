/*
 * This software is Copyright by the Board of Trustees of Michigan
 *  State University (c) Copyright 2014, 2015.
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
package org.openepics.discs.ccdb.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 *
 * @author vuppala
 */
@Entity
@Table(name = "auth_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthRole.findAll", query = "SELECT a FROM AuthRole a"),
    @NamedQuery(name = "AuthRole.findByName", query = "SELECT a FROM AuthRole a WHERE a.name = :name"),
    })
public class AuthRole extends ConfigurationEntity {
    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name", unique = true)
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description")
    private String description;
    
   
    
//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE} , mappedBy = "role")
//    private List<AuthPermission> authPermissionList;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE} , mappedBy = "role")
    private List<AuthUserRole> authUserRoleList;

    public AuthRole() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
                                                                                                                                                                                          

//    @XmlTransient
//    @JsonIgnore
//    public List<AuthPermission> getAuthPermissionList() {
//        return authPermissionList;
//    }
//
//    public void setAuthPermissionList(List<AuthPermission> authPermissionList) {
//        this.authPermissionList = authPermissionList;
//    }
//
    @XmlTransient
    @JsonIgnore
    public List<AuthUserRole> getAuthUserRoleList() {
        return authUserRoleList;
    }

    public void setAuthUserRoleList(List<AuthUserRole> authUserRoleList) {
        this.authUserRoleList = authUserRoleList;
    }


    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.auth.AuthRole[ roleId=" + id + " ]";
    }
    
}
