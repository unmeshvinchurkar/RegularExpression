package com.parseTree.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class State {
	private Set<Rule> rulesSet = new LinkedHashSet<Rule>();
	private int stateNo = 0;
	private Map<String, State> nextStateMap = new HashMap<String, State>();
	private boolean processed = false;
	private static int stateNoCount = 0;
	private boolean accptingState = false;

	public State() {
	}

	public void setStateNo(int stateNo) {
		this.stateNo = stateNo;
	}

	public void addRule(Rule rule) {
		rulesSet.add(rule);
	}

	public boolean isAccptingState() {

		if (!accptingState && rulesSet.size() == 1) {
			Rule r = ((Rule) rulesSet.toArray()[0]);
			int dotPosition = r.getDotPos();

			if (dotPosition == r.getRHS().size() - 1) {
				if (r.getRHS().get(dotPosition).equals("$")) {
					accptingState = true;
				}
			}
		}
		return accptingState;
	}

	public void addRules(Collection<Rule> rules) {
		if (rules != null) {
			rulesSet.addAll(rules);
		}
	}

	public Set<Rule> getReductions() {
		Set<Rule> rules = new LinkedHashSet<Rule>();

		for (Rule rule : rulesSet) {
			if (rule.isReduction()) {
				rules.add(rule);
			}
		}
		return rules;
	}

	public List<Rule> getNextRules(String input) {
		List<Rule> rules = new ArrayList<Rule>();
		for (Rule rule : rulesSet) {
			if (rule.getItemAfterDot() != null
					&& (rule.getItemAfterDot().equals(input) || rule
							.getItemAfterDot().equals("~") || (rule.getRHS().size() == 1 && rule.getRHS().get(0).equals("~")))) {
				Rule cloned = rule.clone();
				cloned.moveDot();
				rules.add(cloned);
			}

		}
		return rules;
	}

	public Set<String> getPossibleInputs() {
		Set<String> items = new HashSet<String>();
		for (Rule rule : rulesSet) {
			items.add(rule.getItemAfterDot());
		}
		return items;
	}

	public Set<Rule> getRulesSet() {
		return rulesSet;
	}

	public int getStateNo() {
		return stateNo;
	}

	public void addNextState(String item, State nextState) {
		nextStateMap.put(item, nextState);
	}

	public State getNextState(String input) {
		return nextStateMap.get(input);
	}

	public Collection<State> getNextStates() {
		return nextStateMap.values();
	}

	public Set<String> getInputs() {
		return nextStateMap.keySet();
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	@Override
	public String toString() {
		return "State [stateNo=" + stateNo + ", " + "rulesSet=" + rulesSet
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rulesSet == null) ? 0 : rulesSet.hashCode());
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
		State other = (State) obj;
		if (rulesSet == null) {
			if (other.rulesSet != null)
				return false;
		} else if (!rulesSet.equals(other.rulesSet))
			return false;
		return true;
	}

}
