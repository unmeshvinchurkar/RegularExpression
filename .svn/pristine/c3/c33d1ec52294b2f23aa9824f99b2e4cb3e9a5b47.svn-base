package com.parseTree.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {

	private String head = null;
	private List<String> rhsItems = new ArrayList<String>();
	private int dotPos = -1;
	private boolean isProcessed = false;
	protected String ruleNo = null;
	private static int ruleNoCount = 0;

	public Rule(String head) {
		if (head == null) {
			throw new IllegalArgumentException("LHS of rule is Null");
		}
		this.head = head;
		ruleNo = String.valueOf(++ruleNoCount);
	}

	public String getRuleNo() {
		return ruleNo;
	}

	void setRuleNo(String ruleName) {
		this.ruleNo = ruleName;
	}

	public void addRhsItem(String item) {
		rhsItems.add(item);
	}

	public void addRhsItems(Collection<String> items) {
		for (String item : items) {
			rhsItems.add(item);
		}
	}

	public List<String> getRHS() {
		return rhsItems;
	}

	public String getLHS() {
		return head;
	}

	public Set<String> getAllItems() {
		Set<String> items = new HashSet<String>();
		items.addAll(rhsItems);
		items.add(head);
		return items;
	}

	public String getItemAfterDot() {
		if (dotPos < rhsItems.size() - 1) {
			return rhsItems.get(dotPos + 1);
		}
		return null;
	}

	public boolean isDotAtStart() {
		return dotPos == -1;
	}

	public boolean isDotAtEnd() {
		if (rhsItems.size() == 1 && rhsItems.get(0).equals("~")) {
			dotPos = rhsItems.size() - 1;
		}
		return (dotPos == rhsItems.size() - 1);
	}

	public boolean isReduction() {
		if (rhsItems.size() == 1 && rhsItems.get(0).equals("~")) {
			dotPos = rhsItems.size() - 1;
		}

		return (dotPos == rhsItems.size() - 1);
	}

	public boolean canShift() {
		if (rhsItems.size() == 1 && rhsItems.get(0).equals("~")) {
			dotPos = rhsItems.size() - 1;
		}
		return dotPos < rhsItems.size() - 1;
	}

	public boolean moveDot() {
		if (dotPos < rhsItems.size() - 1) {
			++dotPos;
			return true;
		}
		return false;
	}

	public int getDotPos() {
		if (rhsItems.size() == 1 && rhsItems.get(0).equals("~")) {
			dotPos = rhsItems.size() - 1;
		}
		return dotPos;
	}

	public void setDotPos(int dotPos) {
		this.dotPos = dotPos;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Rule clone() {
		Rule rule = new Rule(this.head);
		rule.addRhsItems(this.getRHS());
		rule.setDotPos(this.getDotPos());
		rule.setRuleNo(this.getRuleNo());
		return rule;
	}

	@Override
	public String toString() {
		return "Rule [dotPos=" + dotPos + ", ruleNo=" + ruleNo + ", head="
				+ head + ", rhsItems=" + rhsItems + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dotPos;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result
				+ ((rhsItems == null) ? 0 : rhsItems.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (dotPos != other.dotPos)
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (rhsItems == null) {
			if (other.rhsItems != null)
				return false;
		} else if (!rhsItems.equals(other.rhsItems))
			return false;
		return true;
	}
}
