package com.common.permission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PmRUserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pm_r_user_role")
public class PmRUserRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595773373161909446L;
	/**
	 * 
	 */
	private Integer roleUserId;
	private Integer roleUserRid;
	private String roleUserUid;
	private String userName;

	// Constructors
	public boolean equals(Object obj) {
		if(obj instanceof PmRUserRole)
			return this.roleUserId.equals(((PmRUserRole)obj).roleUserId);
		else
			return super.equals(obj);
	}
	/** default constructor */
	public PmRUserRole() {
	}

	/** minimal constructor */
	public PmRUserRole(Integer roleUserRid, String roleUserUid) {
		this.roleUserRid = roleUserRid;
		this.roleUserUid = roleUserUid;
	}

	/** full constructor */
	public PmRUserRole(Integer roleUserRid, String roleUserUid, String userName) {
		this.roleUserRid = roleUserRid;
		this.roleUserUid = roleUserUid;
		this.userName = userName;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ROLE_USER_ID", unique = true, nullable = false)
	public Integer getRoleUserId() {
		return this.roleUserId;
	}

	public void setRoleUserId(Integer roleUserId) {
		this.roleUserId = roleUserId;
	}

	@Column(name = "ROLE_USER_RID", nullable = false)
	public Integer getRoleUserRid() {
		return this.roleUserRid;
	}

	public void setRoleUserRid(Integer roleUserRid) {
		this.roleUserRid = roleUserRid;
	}

	@Column(name = "ROLE_USER_UID", nullable = false, length = 40)
	public String getRoleUserUid() {
		return this.roleUserUid;
	}

	public void setRoleUserUid(String roleUserUid) {
		this.roleUserUid = roleUserUid;
	}

	@Column(name = "USER_NAME", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}