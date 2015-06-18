package com.regular.expression.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class StateNfa {
	
	private boolean processed = false;
	private String name = null;
	private String stateType = null;
	private Map<String, Set<StateNfa>> nextStateMap = new HashMap<String, Set<StateNfa>>();
	
	private static Integer stateNum = 1;
	
	private static Integer _getStateNo(){
		return stateNum++;		
	}

	public StateNfa() {
		this(StringConstants.INTERMEDIATE_STATE);
	}

	public StateNfa(String type) {
		stateType = type;
		name = _getStateNo()+"";
	}

	public Set<String> getAllInputs() {
		return nextStateMap.keySet();
	}

	public Set<StateNfa> getEpsilonClosure() {

		Set<StateNfa> statesSet = new HashSet<StateNfa>();

		Queue<StateNfa> queue = new LinkedList<StateNfa>();
		queue.add(this);
		statesSet.add(this);

		while (!queue.isEmpty()) {
			StateNfa state = queue.remove();

			if (!state.isProcessed()) {
				state.setProcessed(true);
				Set<StateNfa> eStates = state
						.getNextStates(StringConstants.EPSILON_INPUT);
				if (eStates != null && !eStates.isEmpty()) {
					statesSet.addAll(eStates);
				}

				if (eStates != null) {
					for (StateNfa s1 : eStates) {
						if (!s1.isProcessed()) {
							queue.add(s1);
						}
					}
				}
			}
		}
		for (StateNfa s : statesSet) {
			s.setProcessed(false);
		}

		return statesSet;
	}

	public void addNextState(String input, StateNfa nextState) {

		if (input != null && nextState != null) {
			Set<StateNfa> set = nextStateMap.get(input);

			if (set == null) {
				set = new HashSet<StateNfa>();
				nextStateMap.put(input, set);
			}
			set.add(nextState);
		} else {
			throw new IllegalArgumentException(
					"Either input or next state is null");
		}
	}	

	public Set<StateNfa> getNextStates(String input) {
		return nextStateMap.get(input);
	}

	public boolean hasNextState(Character input) {
		return nextStateMap.get(String.valueOf(input)) != null
				&& nextStateMap.get(String.valueOf(input)).size() > 0 ? true : false;
	}

	public String geType() {
		return stateType;
	}

	public void setType(String stateType) {
		this.stateType = stateType;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public void clearNextStatesMap() {
		nextStateMap.clear();
	}

	public String toString() {
		return "StateNfa [stateName=" + name +"  stateType=" + stateType + ", keys="
				+ nextStateMap.keySet().toString() + "]";
	}

	public String getName() {
		return name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateNfa other = (StateNfa) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
