package com.syntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.parseTree.ParseTreeGenerator;
import com.parseTree.TreeNode;
import com.parseTree.grammer.NodeInfo;

public class SyntaxTreeGenerator {

	private ParseTreeGenerator genarator = null;
	private Set<String> terminals = null;
	private static String[] ignoredTerminals = { "(", ")", "$", ";", ",", "{",
			"}" };
	private static final String BASE_PACKAGE_NAME = "com.syntax";

	public SyntaxTreeGenerator(ParseTreeGenerator genarator) {
		this.genarator = genarator;
	}

	private boolean _isIgnoredterminal(TreeNode treeNode) {
		for (String entry : ignoredTerminals) {
			if (entry.equals(treeNode.getToken().getValue())) {
				return true;
			}
		}
		return false;
	}

	public Node generateSyntaxTree() {
		TreeNode rootNode = genarator.generateParseTree();
		terminals = genarator.getTerminals();
		return _generateTree(rootNode);
	}

	private Node _generateTree(TreeNode treeNode) {

		NodeInfo info = treeNode.getNodeInfo();
		String nodeIndex = info.getNodeIndex();
		String nodeClassname = info.getNodeClassName();
		List<TreeNode> children = new ArrayList<TreeNode>();
		TreeNode parentNode = null;

		if (Integer.valueOf(nodeIndex) == 0) {
			children.addAll(treeNode.getChildNodes());
			parentNode = treeNode;

		} else {
			int i = 1;
			for (TreeNode child : treeNode.getChildNodes()) {

				if (i == Integer.valueOf(nodeIndex)) {
					parentNode = child;
				} else {
					children.add(child);
				}
				i++;
			}
		}
		
		

		Node node = null;

		if (!_isTerminal(parentNode)) {
			TreeNode terminal = null;

			int nTCount = 0;
			for (TreeNode child : children) {
				if (_isTerminal(child) && !_isIgnoredterminal(child)) {
					terminal = child;
				} else if (!_isTerminal(child)) {
					nTCount++;
				}
			}

			List<TreeNode> childNodes = treeNode.getChildNodes();

			if (terminal != null) {
				node = new Node(terminal.getToken().getClassName(), terminal
						.getToken().getValue());
			} else {

				node = new Node(treeNode.getClassName(), treeNode.getToken()
						.getValue());

				if (nTCount == 1) {
					for (TreeNode child : treeNode.getChildNodes()) {
						if (!_isTerminal(child)) {
							childNodes = child.getChildNodes();
							break;
						}
					}
				}
			}

			for (TreeNode child : childNodes) {
				if (!_isTerminal(child)) {
					_addChild(node, _generateTree(child));
				}
			}
		} else if (!_isIgnoredterminal(parentNode)) {
			node = new Node(treeNode.getToken().getClassName(), treeNode
					.getToken().getValue());
		}
		return node;
	}

	private void _addChild(Node node, Node childNode) {
		if (node != null && childNode != null) {
			node.addChild(childNode);
		}
	}

	private boolean _isTerminal(TreeNode node) {
		return terminals.contains(node.getToken().getClassName())
				|| terminals.contains(node.getToken().getValue());
	}

	private static void printTree(Node node) {
		Node endNode = new Node("END_NODE", null);

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);
		queue.add(endNode);
		Node syntaxNode = null;

		while (!queue.isEmpty()) {
			while (!queue.isEmpty() && (syntaxNode = queue.remove()) != endNode) {
				System.out.print(syntaxNode.getValue().toString() + " ");
				queue.addAll(syntaxNode.getChildren());
			}

			if (!queue.isEmpty()) {
				queue.add(endNode);
			}
			if (syntaxNode == endNode) {
				System.out.println("\n");
				continue;
			}
		}
	}

	public static void main(String args[]) {

		ParseTreeGenerator treeGenerator = new ParseTreeGenerator();
		SyntaxTreeGenerator syntaxTree = new SyntaxTreeGenerator(treeGenerator);
		Node node = syntaxTree.generateSyntaxTree();
		printTree(node);

	}

}
