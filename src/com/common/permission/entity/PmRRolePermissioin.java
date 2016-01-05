package com.common.permission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PmRRolePermissioin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pm_r_role_permissioin")
public class PmRRolePermissioin implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8073452444354185L;
	/**
	 * 
	 */
	/**
	 * 
	 */
	private Integer rolePermissionId;
	private Integer rolePermissionPid;
	private Integer rolePermissionRid;
	private Integer rolePermissionStatus;
	private String rolePermissionValue;

	// Constructors
	public boolean equals(Object obj) {
		if(obj instanceof PmPermission)
			return this.rolePermissionId.equals(((PmRRolePermissioin)obj).rolePermissionId);
		else
			return super.equals(obj);
	}
	/** default constructor */
	public PmRRolePermissioin() {
	}

	/** full constructor */
	public PmRRolePermissioin(Integer rolePermissionPid,
			Integer rolePermissionRid, Integer rolePermissionStatus,
			String rolePermissionValue) {
		this.rolePermissionPid = rolePermissionPid;
		this.rolePermissionRid = rolePermissionRid;
		this.rolePermissionStatus = rolePermissionStatus;
		this.rolePermissionValue = rolePermissionValue;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ROLE_PERMISSION_ID", unique = true, nullable = false)
	public Integer getRolePermissionId() {
		return this.rolePermissionId;
	}

	public void setRolePermissionId(Integer rolePermissionId) {
		this.rolePermissionId = rolePermissionId;
	}

	@Column(name = "ROLE_PERMISSION_PID", nullable = false)
	public Integer getRolePermissionPid() {
		return this.rolePermissionPid;
	}

	public void setRolePermissionPid(Integer rolePermissionPid) {
		this.rolePermissionPid = rolePermissionPid;
	}

	@Column(name = "ROLE_PERMISSION_RID", nullable = false)
	public Integer getRolePermissionRid() {
		return this.rolePermissionRid;
	}

	public void setRolePermissionRid(Integer rolePermissionRid) {
		this.rolePermissionRid = rolePermissionRid;
	}

	@Column(name = "ROLE_PERMISSION_STATUS", nullable = false)
	public Integer getRolePermissionStatus() {
		return this.rolePermissionStatus;
	}

	public void setRolePermissionStatus(Integer rolePermissionStatus) {
		this.rolePermissionStatus = rolePermissionStatus;
	}

	@Column(name = "ROLE_PERMISSION_VALUE",  length = 100)
	public String getRolePermissionValue() {
		return this.rolePermissionValue;
	}

	public void setRolePermissionValue(String rolePermissionValue) {
		this.rolePermissionValue = rolePermissionValue;
	}

}