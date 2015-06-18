package com.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node {
	private String className;
	private List<Node> children = new ArrayList<Node>();
	private String value;

	public Node(String className) {
		this.className = className;
	}

	public Node(String className, String value) {
		super();
		this.className = className;
		this.value = value;
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public void addChildren(Collection<? extends Node> nodes) {
		children.addAll(nodes);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "Node [className=" + className + ", value=" + value + "]";
	}
	
	
}
