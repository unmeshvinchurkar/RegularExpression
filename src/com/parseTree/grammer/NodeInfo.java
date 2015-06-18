package com.parseTree.grammer;

public class NodeInfo {

	private String nodeIndex;
	private String nodeClassName;

	public NodeInfo(String nodeIndex, String nodeClassName) {
		super();
		this.nodeIndex = nodeIndex;
		this.nodeClassName = nodeClassName;
	}

	public NodeInfo(String nodeIndex) {
		super();
		this.nodeIndex = nodeIndex;
	}

	public String getNodeIndex() {
		return nodeIndex;
	}

	public String getNodeClassName() {
		return nodeClassName;
	}

	@Override
	public String toString() {
		return "NodeInfo [nodeIndex=" + nodeIndex + ", nodeClassName="
				+ nodeClassName + "]";
	}

}
