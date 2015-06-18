package com.parseTree.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.parseTree.objects.Rule;
import com.parseTree.objects.State;

public class ActionTableHelper {
	
	public static int STATE_COUNT= 0;

	public static State createState(Collection<Rule> ruleSet,
			Map<String, Set<Rule>> rulesMap, Set<String> terminals) {

		State state = new State();
		state.addRules(ruleSet);
		Queue<Rule> queue = new LinkedList<Rule>();
		queue.addAll(ruleSet);

		while (!queue.isEmpty()) {
			Rule rule = queue.remove();

			if (!rule.isProcessed()) {
				rule.setProcessed(true);
				String itemAfterDot = rule.getItemAfterDot();

				// Item after dot is not a terminal
				if (itemAfterDot != null && !terminals.contains(itemAfterDot)) {
					Set<Rule> rulesStarWithNT = rulesMap.get(itemAfterDot);

					if (rulesStarWithNT != null) {
						state.addRules(rulesStarWithNT);

						for (Rule r : rulesStarWithNT) {
							if (!r.isProcessed()
									&& 
									
									(r.getItemAfterDot() != null && !itemAfterDot
											.equals(r.getItemAfterDot()))) {
								queue.add(r.clone());
							} else {
								// r.setProcessed(true);
							}
						}
					}
				}
			}
		}

		for (Rule r : ruleSet) {
			r.setProcessed(false);
		}

		return state;
	}

	public static void createDFA(State startState,
			Map<String, Set<Rule>> rulesMap, Set<String> terminals) {

		Map<State, State> stateSet = new HashMap<State, State>();
		Queue<State> queue = new LinkedList<State>();
		stateSet.put(startState, startState);
		queue.add(startState);

		while (!queue.isEmpty()) {
			State state = queue.remove();

			if (!state.isProcessed()) {
				state.setProcessed(true);

				Set<String> inputs = state.getPossibleInputs();

				for (String nextInput : inputs) {

					List<Rule> nextRules = state.getNextRules(nextInput);

					if (nextRules.size() > 0) {
						State nextState = createState(nextRules, rulesMap,
								terminals);

						if (stateSet.get(nextState) == null) {
							nextState.setStateNo(STATE_COUNT++);
							stateSet.put(nextState, nextState);
						}
						nextState = stateSet.get(nextState);

						state.addNextState(nextInput, nextState);

						if (!nextState.isProcessed()) {
							queue.add(nextState);

						}
					}
				}
			}
		}

		for (State s : stateSet.values()) {
			s.setProcessed(false);
		}
	}

	private static Map<String, Set<Rule>> _createRuleMap(List<Rule> nextRules) {
		Map<String, Set<Rule>> rulesSetMap = new LinkedHashMap<String, Set<Rule>>();

		for (Rule rule : nextRules) {
			Set<Rule> rules = rulesSetMap.get(rule.getLHS());
			if (rules == null) {
				rules = new LinkedHashSet<Rule>();
				rulesSetMap.put(rule.getLHS(), rules);
			}
			rules.add(rule);
		}
		return rulesSetMap;
	}
}
