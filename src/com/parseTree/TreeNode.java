package com.parseTree;

import java.util.ArrayList;
import java.util.List;

import com.parseTree.grammer.NodeInfo;
import com.tokenizer.Token;

public class TreeNode {

	private String className = null;
	private List<TreeNode> childNodes = new ArrayList<TreeNode>();
	private Token token = null;
	private NodeInfo nodeInfo = null;

	public TreeNode(String className, Token token) {
		super();
		this.className = className;
		this.token = token;
	}

	public String getClassName() {
		return className;
	}

	public List<TreeNode> getChildNodes() {
		return childNodes;
	}

	public Token getToken() {
		return token;
	}

	public void addChildNode(TreeNode node) {
		childNodes.add(node);
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	
	@Override
	public String toString() {
		return "TreeNode [className=" + className + ", token=" + token
				+ ", nodeInfo=" + nodeInfo + "]";
	}
	

//	@Override
//	public String toString() {
//		return "TreeNode [className=" + className + ", token=" + token + "]";
//	}

}
