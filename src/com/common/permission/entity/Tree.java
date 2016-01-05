package com.common.permission.entity;

import java.util.List;
/**
 * ext tree的实体
 * @author zhouya
 *
 */
public class Tree {

	public String text; //节点显示名称
	public String cls="folder";
	public String id; //节点编号
	public String leaf; //是否有子节点
	public List<Tree> children;//子节点集合
	public String href; 
	public String hrefTarget;//移上去显示的信息
	public String getHrefTarget() {
		return hrefTarget;
	}
	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
