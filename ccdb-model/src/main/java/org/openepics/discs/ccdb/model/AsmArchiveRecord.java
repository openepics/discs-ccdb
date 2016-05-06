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

package org.openepics.discs.ccdb.model;

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
import static javax.persistence.TemporalType.DATE;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author <a href="mailto:vuppala@frib.msu.edu">Vasu Vuppala</a>
 */
@Entity
@Table(name = "asm_archive_record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsmArchiveRecord.findAll", query = "SELECT a FROM AsmArchiveRecord a ORDER BY a.id DESC"),
    @NamedQuery(name = "AsmArchiveRecord.findByDevice", query = "SELECT a FROM AsmArchiveRecord a WHERE a.parent = :parent ORDER BY a.installedAt DESC")
})
public class AsmArchiveRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne(optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private Device parent;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "child", nullable = false)
    private Device child;
    
    @Basic(optional = false)
    @Column(name = "slot_name", nullable = false)
    private String slotName;
    
    @Basic(optional = false)
    @Column(name = "installed_at")
    @Temporal(DATE)
    private Date installedAt;
    
    @Basic(optional = false)
    @Column(name = "uninstalled_at")
    @Temporal(DATE)
    private Date uninstalledAt;
    
    @Basic(optional = false)
    @Column(name = "installed_by")
    private String installedBy;
    
    @Basic(optional = false)
    @Column(name = "uninstalled_by")
    private String uninstalledBy;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsmArchiveRecord)) {
            return false;
        }
        AsmArchiveRecord other = (AsmArchiveRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.discs.ccdb.model.AsmArchiveRecord[ id=" + id + " ]";
    }
    
    //
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getParent() {
        return parent;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    public Device getChild() {
        return child;
    }

    public void setChild(Device child) {
        this.child = child;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public Date getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(Date installedAt) {
        this.installedAt = installedAt;
    }

    public Date getUninstalledAt() {
        return uninstalledAt;
    }

    public void setUninstalledAt(Date uninstalledAt) {
        this.uninstalledAt = uninstalledAt;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public String getUninstalledBy() {
        return uninstalledBy;
    }

    public void setUninstalledBy(String uninstalledBy) {
        this.uninstalledBy = uninstalledBy;
    }

}
