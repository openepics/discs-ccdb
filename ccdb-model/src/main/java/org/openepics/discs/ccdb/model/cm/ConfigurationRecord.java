/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.ccdb.model.cm;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.openepics.discs.ccdb.model.ConfigurationEntity;

/**
 *
 * @author vuppala
 */
@Table(name = "configuration_record")
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfigurationRecord.findAll", query = "SELECT d FROM ConfigurationRecord d"),
    @NamedQuery(name = "ConfigurationRecord.findByName", query = "SELECT d FROM ConfigurationRecord d WHERE d.name = :name")
})
public class ConfigurationRecord extends ConfigurationEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name", nullable = false)
    @NotNull
    @Basic
    @Size(min = 1, max = 64)
    private String name;
 
    @Column(name="comment")
    @Basic
    @Size(min = 1, max = 255)
    private String comment;
    
    
    // ---
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
