package com.common.permission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.common.permission.entity.PmPermission;
import com.common.permission.entity.PmRole;

/**
 * PmRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pm_role")
public class PmRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5933697993448363615L;
	private Integer roleId;
	private String roleName;
	private String roleDescribe;
	private Integer roleStatus;
	private String roleType;

	// Constructors

	/** default constructor */
	public PmRole() {
	}

	/** minimal constructor */
	public PmRole(Integer roleStatus) {
		this.roleStatus = roleStatus;
	}
	public boolean equals(Object obj) {
		if(obj instanceof PmRole)
			return this.roleId.equals(((PmRole)obj).roleId);
		else
			return super.equals(obj);
	}
	/** full constructor */
	public PmRole(String roleName, String roleDescribe, Integer roleStatus,
			String roleType) {
		this.roleName = roleName;
		this.roleDescribe = roleDescribe;
		this.roleStatus = roleStatus;
		this.roleType = roleType;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ROLE_ID", unique = true, nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME", length = 50)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "ROLE_DESCRIBE", length = 254)
	public String getRoleDescribe() {
		return this.roleDescribe;
	}

	public void setRoleDescribe(String roleDescribe) {
		this.roleDescribe = roleDescribe;
	}

	@Column(name = "ROLE_STATUS")
	public Integer getRoleStatus() {
		return this.roleStatus;
	}

	public void setRoleStatus(Integer roleStatus) {
		this.roleStatus = roleStatus;
	}

	@Column(name = "ROLE_TYPE", length = 50)
	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

}