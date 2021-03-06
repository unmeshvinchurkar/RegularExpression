package com.parseTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.parseTree.grammer.GrammerRulesLoader;
import com.parseTree.grammer.NodeInfo;
import com.parseTree.objects.Rule;
import com.parseTree.table.ActionTable;
import com.tokenizer.Token;
import com.tokenizer.Tokenizer;

public class ParseTreeGenerator {

	private Tokenizer tokenizer = null;
	private ActionTableLoader actionTableLoader = null;
	private List<Token> tokens = null;
	private ActionTable actionTable = null;
	private Set<String> terminals = null;
	private GrammerRulesLoader rulesLoader = null;
	private final String $ = "S";
	private final String START_STATE_NAME = "s0";
	private Map<String, NodeInfo> rulesNodeInfoMap=null;

	private int tokenIndex = 0;
	private boolean loaded = false;

	public ParseTreeGenerator() {
		tokenizer = new Tokenizer();
		actionTableLoader = new ActionTableLoader();
		rulesLoader = new GrammerRulesLoader();
	}

	public Set<String> getTerminals() {
		return terminals;
	}

	private void loadRules() {
		rulesLoader.load();
		terminals = rulesLoader.getTerminals();
	}

	private void loadTokens() {
		tokens = tokenizer.getTokens();
	}

	private void loadActionTable() {
		actionTable = actionTableLoader.getActionTable();
	}

	private void loadNodeInfo() {
		rulesNodeInfoMap = rulesLoader.getRulesNodeInfoMap();
	}

	public void loadData() {
		if (loaded) {
			return;
		}
		loadTokens();
		loadActionTable();
		loadRules();
		loadNodeInfo();
		loaded = true;
	}

	private Token _lookAhead() {
		if (tokenIndex >= tokens.size()) {
			return new Token("STRING", "$", 0, 0);
		}
		return tokens.get(tokenIndex);
	}

	private Token _readNextToken() {
		if (tokenIndex >= tokens.size()) {
			return new Token("STRING", "$", 0, 0);
		}
		return tokens.get(tokenIndex++);
	}

	public TreeNode generateParseTree() {
		loadData();
		Stack<StackObj> stack = new Stack<StackObj>();
		Stack<TreeNode> treeStack = new Stack<TreeNode>();

		StackObj obj = new StackObj(START_STATE_NAME, new Token("STRING", "$",
				0, 0), "STRING");
		stack.push(obj);

		Token currToken = _readNextToken();

		while (!stack.isEmpty()) {
			String currentState = stack.peek().getState();
			String nextState = null;
			String reductionNo = null;

			String nextState_reduction = actionTable.getValue(currentState,
					_getColumnName(currToken));

			if (nextState_reduction.indexOf(";") > 0) {
				nextState = nextState_reduction.substring(0,
						nextState_reduction.indexOf(";"));
				reductionNo = nextState_reduction.substring(nextState_reduction
						.indexOf(";") + 2);
			} else if (nextState_reduction.startsWith("s")) {
				nextState = nextState_reduction;
			} else if (nextState_reduction.startsWith("r")) {
				reductionNo = nextState_reduction.substring(1);
			}
			// else if (nextState_reduction.equals("accept")) {
			// break;
			// }
			else {
				throw new RuntimeException("state:" + currentState + " input:"
						+ _getColumnName(currToken) + "  " + currToken
						+ " Line no:" + currToken.getLineNo() + " colNo:"
						+ currToken.getColumnNo());
			}

			if (nextState != null && reductionNo != null) {
				Rule reduction = rulesLoader.getRuleSet().get(
						Integer.valueOf(reductionNo) - 1);

				Set<String> followSetOfLHS = rulesLoader.getFollowSets().get(
						reduction.getLHS());

				if (followSetOfLHS.contains(_getColumnName(_lookAhead()))) {
					// Reduce
					nextState = null;
				} else {
					// Shift
					reductionNo = null;
				}
			}

			// Shift otherwise reduce
			if (nextState != null) {
				StackObj obj1 = new StackObj(nextState, currToken,
						_getColumnName(currToken));
				stack.push(obj1);
				currToken = _readNextToken();
			} else if (reductionNo != null) {

				Rule reduction = rulesLoader.getRuleSet().get(
						Integer.valueOf(reductionNo) - 1);

				TreeNode parentNode = new TreeNode(reduction.getLHS(),
						new Token(reduction.getLHS(), reduction.getLHS(),
								currToken.getLineNo(), currToken.getColumnNo()));
				
				// Set the node info in parse tree node to be used to generate AST
				parentNode.setNodeInfo(rulesNodeInfoMap.get(reduction.getRuleNo()));

				if (!(reduction.getRHS().size() == 1 && reduction.getRHS()
						.get(0).equals("~"))) {
					List<TreeNode> childNodes = new ArrayList<TreeNode>();

					for (int i = reduction.getRHS().size() - 1; i >= 0; i--) {
						StackObj obj1 = stack.pop();
						TreeNode childNode = null;

						if (!treeStack.isEmpty()
								&& reduction
										.getRHS()
										.get(i)
										.equals(treeStack.peek().getClassName())) {
							childNode = treeStack.pop();
						}
						// If not a terminal
						else if (!terminals.contains(_getClassName(obj1
								.getToken()))) {
							childNode = new TreeNode(reduction.getRHS().get(i),
									new Token(reduction.getRHS().get(i),
											reduction.getRHS().get(i),
											currToken.getLineNo(),
											currToken.getColumnNo()));
						}
						// If terminal
						else {
							childNode = new TreeNode(obj1.getClassName(),
									obj1.getToken());

						}

						childNodes.add(childNode);
					}

					for (int i = childNodes.size() - 1; i >= 0; i--) {
						parentNode.addChildNode(childNodes.get(i));
					}
				}
				treeStack.push(parentNode);

				if (treeStack.size() == 1
						&& treeStack.peek().getClassName().equalsIgnoreCase($)) {
					break;
				}

				String nextStateStr = actionTable.getValue(stack.peek()
						.getState(), reduction.getLHS());

				if (!nextStateStr.equals("_")) {
					stack.push(new StackObj(nextStateStr, new Token(reduction
							.getLHS(), reduction.getLHS(), currToken
							.getLineNo(), currToken.getColumnNo()), reduction
							.getLHS()));
				} else {
					throw new RuntimeException("state:" + currentState
							+ " input:" + _getColumnName(currToken) + "  "
							+ currToken + " Line no:" + currToken.getLineNo()
							+ " colNo:" + currToken.getColumnNo());
				}

			}
		}

		return treeStack.pop();
	}

	private String _getClassName(Token token) {
		return _getColumnName(token);
	}

	private String _getColumnName(Token token) {
		if (token.getValue() != null
				&& terminals.contains(String.valueOf(token.getValue()))) {
			return token.getValue();
		}
		return token.getClassName();
	}

	private static void printTree(TreeNode node) {
		TreeNode endNode = new TreeNode("END_NODE", null);

		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(node);
		queue.add(endNode);
		TreeNode treeNode = null;

		while (!queue.isEmpty()) {
			while (!queue.isEmpty() && (treeNode = queue.remove()) != endNode) {
				System.out.print(treeNode.getToken().toString() + " ");
				queue.addAll(treeNode.getChildNodes());
			}

			if (!queue.isEmpty()) {
				queue.add(endNode);
			}

			if (treeNode == endNode) {
				System.out.println("\n");
				continue;
			}
		}
	}

	public static void main(String args[]) {
		ParseTreeGenerator treeGenerator = new ParseTreeGenerator();
		TreeNode node = treeGenerator.generateParseTree();
		printTree(node);

	}
}
