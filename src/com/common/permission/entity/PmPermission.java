package com.common.permission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * PmPermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pm_permission")
public class PmPermission implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3266088193444499804L;
	// Fields
	/**
	 * 
	 */
	/**
	 * 
	 */
	private Integer permissionId;
	private Integer permissionParentId;
	private String permissionName;
	private String permissionUri;
	private String permissionImageUri;
	private String permissionLinkTarget;
	private String permissionPrompt;

	// Constructors
	public boolean equals(Object obj) {
		if(obj instanceof PmPermission)
			return this.permissionId.equals(((PmPermission)obj).permissionId);
		else
			return super.equals(obj);
	}
	
	
	/** default constructor */
	public PmPermission() {
	}

	/** minimal constructor */
	public PmPermission(Integer permissionParentId, String permissionName) {
		this.permissionParentId = permissionParentId;
		this.permissionName = permissionName;
	}

	/** full constructor */
	public PmPermission(Integer permissionParentId, String permissionName,
			String permissionUri, String permissionImageUri,
			String permissionLinkTarget, String permissionPrompt) {
		this.permissionParentId = permissionParentId;
		this.permissionName = permissionName;
		this.permissionUri = permissionUri;
		this.permissionImageUri = permissionImageUri;
		this.permissionLinkTarget = permissionLinkTarget;
		this.permissionPrompt = permissionPrompt;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PERMISSION_ID", unique = true, nullable = false)
	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Column(name = "PERMISSION_PARENT_ID", nullable = false)
	public Integer getPermissionParentId() {
		return this.permissionParentId;
	}

	public void setPermissionParentId(Integer permissionParentId) {
		this.permissionParentId = permissionParentId;
	}

	@Column(name = "PERMISSION_NAME", nullable = false, length = 100)
	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Column(name = "PERMISSION_URI", length = 254)
	public String getPermissionUri() {
		return this.permissionUri;
	}

	public void setPermissionUri(String permissionUri) {
		this.permissionUri = permissionUri;
	}

	@Column(name = "PERMISSION_IMAGE_URI", length = 254)
	public String getPermissionImageUri() {
		return this.permissionImageUri;
	}

	public void setPermissionImageUri(String permissionImageUri) {
		this.permissionImageUri = permissionImageUri;
	}

	@Column(name = "PERMISSION_LINK_TARGET", length = 100)
	public String getPermissionLinkTarget() {
		return this.permissionLinkTarget;
	}

	public void setPermissionLinkTarget(String permissionLinkTarget) {
		this.permissionLinkTarget = permissionLinkTarget;
	}

	@Column(name = "PERMISSION_PROMPT", length = 100)
	public String getPermissionPrompt() {
		return this.permissionPrompt;
	}

	public void setPermissionPrompt(String permissionPrompt) {
		this.permissionPrompt = permissionPrompt;
	}

}