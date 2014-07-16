--   -------------------------------------------------- 
--   Generated by Enterprise Architect Version 9.2.921
--   Created On : Thursday, 08 May, 2014 
--   DBMS       : MySql 
--   -------------------------------------------------- 


SET FOREIGN_KEY_CHECKS=0;


--  Drop Tables, Stored Procedures and Views 

DROP TABLE IF EXISTS alignment_artifact CASCADE;
DROP TABLE IF EXISTS alignment_property CASCADE;
DROP TABLE IF EXISTS alignment_record CASCADE;
DROP TABLE IF EXISTS component_type CASCADE;
DROP TABLE IF EXISTS comptype_artifact CASCADE;
DROP TABLE IF EXISTS comptype_asm CASCADE;
DROP TABLE IF EXISTS comptype_log_rec CASCADE;
DROP TABLE IF EXISTS comptype_property CASCADE;
DROP TABLE IF EXISTS config CASCADE;
DROP TABLE IF EXISTS data_type CASCADE;
DROP TABLE IF EXISTS device CASCADE;
DROP TABLE IF EXISTS device_artifact CASCADE;
DROP TABLE IF EXISTS device_log_rec CASCADE;
DROP TABLE IF EXISTS device_property CASCADE;
DROP TABLE IF EXISTS installation_artifact CASCADE;
DROP TABLE IF EXISTS installation_record CASCADE;
DROP TABLE IF EXISTS property CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS slot CASCADE;
DROP TABLE IF EXISTS slot_artifact CASCADE;
DROP TABLE IF EXISTS slot_log_rec CASCADE;
DROP TABLE IF EXISTS slot_pair CASCADE;
DROP TABLE IF EXISTS slot_property CASCADE;
DROP TABLE IF EXISTS slot_relation CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS user CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;

--  Create Tables 
CREATE TABLE alignment_artifact
(
	artifact_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	alignment_record INTEGER UNSIGNED NOT NULL,
	name VARCHAR(128) NOT NULL,
	is_internal BOOL NOT NULL DEFAULT TRUE COMMENT 'if true, it is an internal artifact which can be downloaded. otherwise, it is a link to an external resource.',
	description VARCHAR(255),
	uri TEXT NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	modified_at TIMESTAMP NOT NULL,
	PRIMARY KEY (artifact_id),
	KEY (alignment_record)

) ENGINE=InnoDB COMMENT='Each row is an artifact corresponding to an alignment rec';


CREATE TABLE alignment_property
(
	align_prop_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	alignment_record INTEGER UNSIGNED NOT NULL,
	property INTEGER UNSIGNED NOT NULL,
	prop_value TEXT,
	in_repository BOOL NOT NULL DEFAULT FALSE COMMENT 'actual value is stored in content repository',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (align_prop_id),
	KEY (alignment_record),
	KEY (property),
	INDEX UQ_uniq_align_prop (alignment_record ASC, property ASC)

) ENGINE=InnoDB COMMENT='Each row is an alignment property.';


CREATE TABLE alignment_record
(
	alignment_record_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	record_number VARCHAR(64) NOT NULL,
	slot INTEGER UNSIGNED NOT NULL,
	device INTEGER UNSIGNED NOT NULL,
	alignment_date DATETIME NOT NULL,
	global_x DOUBLE,
	global_y DOUBLE,
	global_z DOUBLE,
	global_pitch DOUBLE,
	global_yaw DOUBLE,
	global_roll DOUBLE,
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (alignment_record_id),
	UNIQUE UQ_alignment_record_record_number(record_number),
	KEY (slot),
	KEY (device)

) ENGINE=InnoDB;


CREATE TABLE component_type
(
	component_type_id CHAR(8) NOT NULL,
	description VARCHAR(255),
	super_component_type CHAR(8) COMMENT 'super (more general) type',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (component_type_id),
	KEY (super_component_type)

) ENGINE=InnoDB COMMENT='Each row is a component type';


CREATE TABLE comptype_artifact
(
	artifact_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	component_type CHAR(8) NOT NULL,
	name VARCHAR(128) NOT NULL,
	is_internal BOOL NOT NULL DEFAULT TRUE COMMENT 'if true, it is an internal artifact which can be downloaded. otherwise, it is a link to an external resource.',
	description VARCHAR(255) NOT NULL,
	uri TEXT NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	modified_at DATETIME NOT NULL,
	PRIMARY KEY (artifact_id),
	KEY (component_type)

) ENGINE=InnoDB COMMENT='Each row is an artifact related to component type';


CREATE TABLE comptype_asm
(
	parent_type CHAR(8) NOT NULL COMMENT 'id of the parent type',
	child_position CHAR(16) NOT NULL COMMENT 'identifier for position of a child type in an assembly or composition',
	child_type CHAR(8) NOT NULL COMMENT 'id of the child type',
	description VARCHAR(255),
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (parent_type, child_position),
	KEY (child_type),
	KEY (parent_type)

) ENGINE=InnoDB COMMENT='Each row is a member in a composite type (type assembly)';


CREATE TABLE comptype_log_rec
(
	ctype_log_rec_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Each row is a ',
	component_type CHAR(8) NOT NULL,
	log_time TIMESTAMP NOT NULL,
	user VARCHAR(64) NOT NULL,
	entry TEXT NOT NULL,
	PRIMARY KEY (ctype_log_rec_id)

) ENGINE=InnoDB COMMENT='Each row is a change log record of a component type';


CREATE TABLE comptype_property
(
	ctype_prop_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	component_type CHAR(8) NOT NULL,
	property INTEGER UNSIGNED NOT NULL,
	type CHAR(4) COMMENT 'T -for Component Types L -  for Logical Component P - for Physical Component PT -  for both Physical Component and Component Type LP - for both Logical and Physical Components S -  It is a Control signal (PV) (associated with Logical Components)',
	prop_value TEXT COMMENT 'the design value',
	in_repository BOOL NOT NULL DEFAULT FALSE COMMENT 'actual value is stored in content repository',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (ctype_prop_id),
	KEY (property),
	KEY (component_type),
	INDEX UQ_uniq_ctype_prop (component_type ASC, property ASC)

) ENGINE=InnoDB COMMENT='Each row is a property of a component type';


CREATE TABLE config
(
	name VARCHAR(64) NOT NULL,
	prop_value VARCHAR(128),
	PRIMARY KEY (name)

) ENGINE=InnoDB COMMENT='Configuration info for this module ';


CREATE TABLE data_type
(
	data_type_id CHAR(32) NOT NULL,
	description VARCHAR(255) NOT NULL,
	scalar BOOL NOT NULL DEFAULT TRUE,
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency',
	PRIMARY KEY (data_type_id)

) ENGINE=InnoDB COMMENT='Each row is a standard data type';


CREATE TABLE device
(
	device_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	serial_number VARCHAR(64) NOT NULL,
	component_type CHAR(8) NOT NULL,
	description VARCHAR(255),
	status CHAR(1) COMMENT 'f - in fabrication p - under repair r - ready (in use) s - spare',
	manufacturer VARCHAR(64),
	manuf_model VARCHAR(64) COMMENT 'manufacturer''s model name',
	manuf_serial_number VARCHAR(64) COMMENT 'manufacturer''s serial number',
	location VARCHAR(64) COMMENT 'location, if component is not installed',
	purchase_order VARCHAR(64) COMMENT 'PO number',
	asm_parent INTEGER UNSIGNED COMMENT 'The assembly device of which this device is a part',
	asm_position VARCHAR(16) COMMENT 'This device''s position in its assembly',
	asm_description VARCHAR(255) COMMENT 'Any comments about its position in assembly',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (device_id),
	UNIQUE UQ_physical_component_serial_number(serial_number),
	KEY (asm_parent),
	KEY (component_type)

) ENGINE=InnoDB COMMENT='Each row represents a physical device and device assemblies.';


CREATE TABLE device_artifact
(
	artifact_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	device INTEGER UNSIGNED NOT NULL,
	name VARCHAR(128) NOT NULL,
	is_internal BOOL NOT NULL DEFAULT TRUE COMMENT 'if true, it is an internal artifact which can be downloaded. otherwise, it is a link to an external resource.',
	description VARCHAR(255) NOT NULL,
	uri TEXT NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	modified_at DATETIME NOT NULL,
	PRIMARY KEY (artifact_id),
	KEY (device)

) ENGINE=InnoDB COMMENT='Each row is an artifact related to a physical component';


CREATE TABLE device_log_rec
(
	dev_log_rec_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	device INTEGER UNSIGNED NOT NULL,
	log_time TIMESTAMP NOT NULL,
	user VARCHAR(64) NOT NULL,
	entry TEXT NOT NULL,
	PRIMARY KEY (dev_log_rec_id),
	KEY (device)

) ENGINE=InnoDB COMMENT='Each row is a change record of a device assembly';


CREATE TABLE device_property
(
	dev_prop_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	device INTEGER UNSIGNED NOT NULL,
	property INTEGER UNSIGNED NOT NULL,
	prop_value TEXT,
	in_repository BOOL NOT NULL DEFAULT FALSE COMMENT 'actual value is stored in content repository',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (dev_prop_id),
	KEY (property),
	KEY (device),
	INDEX UQ_uniq_dev_prop (device ASC, property ASC)

) ENGINE=InnoDB COMMENT='Each row is the measured value of a component''s property.';


CREATE TABLE installation_artifact
(
	artifact_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	installation_record INTEGER UNSIGNED NOT NULL,
	name VARCHAR(128) NOT NULL,
	is_internal BOOL NOT NULL DEFAULT TRUE COMMENT 'if true, it is an internal artifact which can be downloaded. otherwise, it is a link to an external resource.',
	description VARCHAR(255),
	uri TEXT NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	modified_at TIMESTAMP NOT NULL,
	PRIMARY KEY (artifact_id),
	KEY (installation_record)

) ENGINE=InnoDB COMMENT='Each row is an artifact corresponding to an installation';


CREATE TABLE installation_record
(
	installation_record_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	record_number VARCHAR(64) NOT NULL,
	slot INTEGER UNSIGNED NOT NULL,
	device INTEGER UNSIGNED NOT NULL,
	install_date DATE NOT NULL,
	uninstall_date DATE,
	notes TEXT,
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (installation_record_id),
	UNIQUE UQ_installation_record_record_number(record_number),
	KEY (slot),
	KEY (device)

) ENGINE=InnoDB COMMENT='Associates a logical component with a physical one. ';


CREATE TABLE property
(
	property_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(64) NOT NULL,
	description VARCHAR(255) NOT NULL COMMENT 'Description of the property',
	association VARCHAR(3) NOT NULL COMMENT 'T-only for Component Types L-only for Logical Component P-only for Physical Component PT-for both Physical Component and Component Type LP-for both Logical and Physical Components',
	unit CHAR(32) COMMENT 'Units of measurement',
	data_type CHAR(32) NOT NULL,
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (property_id),
	UNIQUE UQ_property_name(name),
	KEY (data_type),
	KEY (unit)

) ENGINE=InnoDB COMMENT='Each row is a property of a component';


CREATE TABLE role
(
	role_id VARCHAR(64) NOT NULL,
	description VARCHAR(255) NOT NULL,
	version INTEGER UNSIGNED NOT NULL DEFAULT 0,
	PRIMARY KEY (role_id)

) ENGINE=InnoDB COMMENT='Each row represents a role';


CREATE TABLE slot
(
	slot_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	component_type CHAR(8) NOT NULL COMMENT 'Associated component type',
	name VARCHAR(128) NOT NULL COMMENT 'The name of the logical component',
	description VARCHAR(255),
	is_hosting_slot BOOL NOT NULL DEFAULT FALSE COMMENT 'True - can be associated with a physical component False - cannot be associated with a physical component',
	beamline_position DOUBLE COMMENT 'position on the beamline',
	global_x DOUBLE COMMENT 'as-designed alignment',
	global_y DOUBLE COMMENT 'as-designed alignment',
	global_z DOUBLE COMMENT 'as-designed alignment',
	global_roll DOUBLE COMMENT 'as-designed alignment',
	global_yaw DOUBLE COMMENT 'as-designed alignment',
	global_pitch DOUBLE COMMENT 'as-designed alignment',
	asm_slot INTEGER UNSIGNED COMMENT 'The assembly slot in which this slot belongs',
	asm_comment VARCHAR(255) COMMENT 'Any comment regarding this slot in its assembly',
	asm_position VARCHAR(16) COMMENT 'Position of this slot in its assembly',
	comment VARCHAR(255),
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'used for concurrency control. is not component version.',
	PRIMARY KEY (slot_id),
	UNIQUE UQ_logical_component_name(name),
	KEY (asm_slot),
	KEY (component_type)

) ENGINE=InnoDB COMMENT='Each row represents an element on the blueprint';


CREATE TABLE slot_artifact
(
	artifact_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	slot INTEGER UNSIGNED NOT NULL,
	name VARCHAR(128) NOT NULL,
	is_internal BOOL NOT NULL DEFAULT TRUE COMMENT 'if true, it is an internal artifact which can be downloaded. otherwise, it is a link to an external resource.',
	description VARCHAR(255) NOT NULL,
	uri TEXT NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	modified_at DATETIME NOT NULL,
	PRIMARY KEY (artifact_id),
	KEY (slot)

) ENGINE=InnoDB COMMENT='Each row is an artifact related to layout slot';


CREATE TABLE slot_log_rec
(
	slot_log_rec_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	slot INTEGER UNSIGNED NOT NULL,
	log_time TIMESTAMP NOT NULL,
	user VARCHAR(64) NOT NULL,
	entry TEXT NOT NULL,
	PRIMARY KEY (slot_log_rec_id),
	KEY (slot)

) ENGINE=InnoDB COMMENT='Each row is a change log record of a slot';


CREATE TABLE slot_pair
(
	slot_pair_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	slot_relation INTEGER UNSIGNED NOT NULL,
	parent_slot INTEGER UNSIGNED NOT NULL,
	child_slot INTEGER UNSIGNED NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (slot_pair_id),
	KEY (slot_relation),
	KEY (child_slot),
	KEY (parent_slot)

) ENGINE=InnoDB COMMENT='Each row is a pair of related slots';


CREATE TABLE slot_property
(
	slot_prop_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	slot INTEGER UNSIGNED NOT NULL,
	property INTEGER UNSIGNED NOT NULL,
	prop_value TEXT,
	in_repository BOOL NOT NULL DEFAULT FALSE COMMENT 'actual value is stored in content repository',
	modified_at DATE NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (slot_prop_id),
	KEY (property),
	KEY (slot),
	INDEX UQ_uniq_slot_prop (slot ASC, property ASC)

) ENGINE=InnoDB COMMENT='Each row is the as-designed value of a component''s property';


CREATE TABLE slot_relation
(
	slot_relation_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(32) NOT NULL,
	iname VARCHAR(32) NOT NULL COMMENT 'name of inverse relationship',
	description VARCHAR(255),
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0 COMMENT 'for concurrency control',
	PRIMARY KEY (slot_relation_id)

) ENGINE=InnoDB COMMENT='Each row is a binary relationship between slots';


CREATE TABLE unit
(
	unit_id CHAR(32) NOT NULL,
	quantity VARCHAR(64) NOT NULL COMMENT 'quantity of measure such as length, current etc',
	symbol VARCHAR(128) NOT NULL COMMENT 'unit symbol in HTML markup',
	description VARCHAR(255) NOT NULL,
	base_unit_expr VARCHAR(255) COMMENT 'Equivalent expression in base units',
	modified_at DATETIME NOT NULL,
	modified_by VARCHAR(64) NOT NULL,
	version INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (unit_id)

) ENGINE=InnoDB COMMENT='Each row is a unit of measurement';


CREATE TABLE user
(
	user_id VARCHAR(64) NOT NULL,
	name VARCHAR(128) NOT NULL,
	email VARCHAR(64),
	comment VARCHAR(255),
	version INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (user_id)

) ENGINE=InnoDB COMMENT='Each row represents a user';


CREATE TABLE user_role
(
	user_role_id INTEGER UNSIGNED NOT NULL,
	user VARCHAR(64) NOT NULL,
	role VARCHAR(64) NOT NULL,
	canDelegate BOOL NOT NULL DEFAULT false,
	isRoleManager BOOL NOT NULL DEFAULT false,
	startTime TIMESTAMP,
	endTime TIMESTAMP,
	comment VARCHAR(255),
	version INTEGER UNSIGNED NOT NULL DEFAULT 0,
	PRIMARY KEY (user_role_id),
	KEY (role),
	KEY (user)

) ENGINE=InnoDB COMMENT='Each row represents a user''s assignment to a role';



SET FOREIGN_KEY_CHECKS=1;


--  Create Foreign Key Constraints 
ALTER TABLE alignment_artifact ADD CONSTRAINT FK_alignment_artifact_alignment_record 
	FOREIGN KEY (alignment_record) REFERENCES alignment_record (alignment_record_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE alignment_property ADD CONSTRAINT FK_alignment_property_alignment_record 
	FOREIGN KEY (alignment_record) REFERENCES alignment_record (alignment_record_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE alignment_property ADD CONSTRAINT FK_alignment_property_property 
	FOREIGN KEY (property) REFERENCES property (property_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE alignment_record ADD CONSTRAINT FK_alignment_record_layout_slot 
	FOREIGN KEY (slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE alignment_record ADD CONSTRAINT FK_alignment_record_physical_component 
	FOREIGN KEY (device) REFERENCES device (device_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE component_type ADD CONSTRAINT FK_component_type_component_type 
	FOREIGN KEY (super_component_type) REFERENCES component_type (component_type_id);

ALTER TABLE comptype_artifact ADD CONSTRAINT FK_ct_artifact_component_type 
	FOREIGN KEY (component_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE comptype_asm ADD CONSTRAINT FK_composition_member_child 
	FOREIGN KEY (child_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE comptype_asm ADD CONSTRAINT FK_composition_member_parent 
	FOREIGN KEY (parent_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE comptype_property ADD CONSTRAINT FK_tcomp_property_value_component_property 
	FOREIGN KEY (property) REFERENCES property (property_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE comptype_property ADD CONSTRAINT FK_tcomp_property_value_component_type 
	FOREIGN KEY (component_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device ADD CONSTRAINT FK_device_device 
	FOREIGN KEY (asm_parent) REFERENCES device (device_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device ADD CONSTRAINT FK_physical_component_type 
	FOREIGN KEY (component_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device_artifact ADD CONSTRAINT FK_pc_artifact_physical_component 
	FOREIGN KEY (device) REFERENCES device (device_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device_log_rec ADD CONSTRAINT FK_device_log_rec_device 
	FOREIGN KEY (device) REFERENCES device (device_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device_property ADD CONSTRAINT FK_pcomp_property_value_component_property 
	FOREIGN KEY (property) REFERENCES property (property_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE device_property ADD CONSTRAINT FK_physical_component_property_physical_component 
	FOREIGN KEY (device) REFERENCES device (device_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE installation_artifact ADD CONSTRAINT FK_installation_artifact_installation_record 
	FOREIGN KEY (installation_record) REFERENCES installation_record (installation_record_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE installation_record ADD CONSTRAINT FK_installation_record_logical_component 
	FOREIGN KEY (slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE installation_record ADD CONSTRAINT FK_installation_record_physical_component 
	FOREIGN KEY (device) REFERENCES device (device_id);

ALTER TABLE property ADD CONSTRAINT FK_property_data_type 
	FOREIGN KEY (data_type) REFERENCES data_type (data_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE property ADD CONSTRAINT FK_property_unit 
	FOREIGN KEY (unit) REFERENCES unit (unit_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot ADD CONSTRAINT FK_slot_slot 
	FOREIGN KEY (asm_slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot ADD CONSTRAINT FK_logical_component_type 
	FOREIGN KEY (component_type) REFERENCES component_type (component_type_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_artifact ADD CONSTRAINT FK_lc_artifact_logical_component 
	FOREIGN KEY (slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_log_rec ADD CONSTRAINT FK_slot_log_rec_slot 
	FOREIGN KEY (slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_pair ADD CONSTRAINT FK_component_pair_component_relation 
	FOREIGN KEY (slot_relation) REFERENCES slot_relation (slot_relation_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_pair ADD CONSTRAINT FK_slot_pair_child 
	FOREIGN KEY (child_slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_pair ADD CONSTRAINT FK_slot_pair_parent 
	FOREIGN KEY (parent_slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_property ADD CONSTRAINT FK_slot_property_property 
	FOREIGN KEY (property) REFERENCES property (property_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE slot_property ADD CONSTRAINT FK_slot_property_slot 
	FOREIGN KEY (slot) REFERENCES slot (slot_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE user_role ADD CONSTRAINT FK_user_role_role 
	FOREIGN KEY (role) REFERENCES role (role_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE user_role ADD CONSTRAINT FK_user_role_user 
	FOREIGN KEY (user) REFERENCES user (user_id)
	ON DELETE RESTRICT ON UPDATE CASCADE;
