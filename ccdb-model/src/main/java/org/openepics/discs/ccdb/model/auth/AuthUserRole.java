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

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 *
 * @author vuppala
 */
@Entity
@Table(name = "auth_user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthUserRole.findAll", query = "SELECT a FROM AuthUserRole a"),
    @NamedQuery(name = "AuthUserRole.findByUserRoleId", query = "SELECT a FROM AuthUserRole a WHERE a.userRoleId = :userRoleId"),
    @NamedQuery(name = "AuthUserRole.findByCanDelegate", query = "SELECT a FROM AuthUserRole a WHERE a.canDelegate = :canDelegate"),
    @NamedQuery(name = "AuthUserRole.findByIsRoleManager", query = "SELECT a FROM AuthUserRole a WHERE a.isRoleManager = :isRoleManager"),
    @NamedQuery(name = "AuthUserRole.findByStartTime", query = "SELECT a FROM AuthUserRole a WHERE a.startTime = :startTime"),
    @NamedQuery(name = "AuthUserRole.findByEndTime", query = "SELECT a FROM AuthUserRole a WHERE a.endTime = :endTime"),
    @NamedQuery(name = "AuthUserRole.findByComment", query = "SELECT a FROM AuthUserRole a WHERE a.comment = :comment"),
    @NamedQuery(name = "AuthUserRole.findByVersion", query = "SELECT a FROM AuthUserRole a WHERE a.version = :version")})
public class AuthUserRole extends ConfigurationEntity {
    private static final long serialVersionUID = 1L;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "can_delegate")
    private boolean canDelegate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_role_manager")
    private boolean isRoleManager;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    
    @Size(max = 255)
    @Column(name = "comment")
    private String comment;
    
    @JoinColumn(name = "user")
    @ManyToOne(optional = false)
    private User user;
    
    @JoinColumn(name = "role")
    @ManyToOne(optional = false)
    private AuthRole role;

    public AuthUserRole() {
    }

   

    public boolean getCanDelegate() {
        return canDelegate;
    }

    public void setCanDelegate(boolean canDelegate) {
        this.canDelegate = canDelegate;
    }

    public boolean getIsRoleManager() {
        return isRoleManager;
    }

    public void setIsRoleManager(boolean isRoleManager) {
        this.isRoleManager = isRoleManager;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthRole getRole() {
        return role;
    }

    public void setRole(AuthRole role) {
        this.role = role;
    }

 

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.auth.AuthUserRole[ id=" + id + " ]";
    }
    
}
