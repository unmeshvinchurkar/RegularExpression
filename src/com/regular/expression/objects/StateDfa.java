package com.regular.expression.objects;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class StateDfa {
	
	private Set<StateNfa> nfaStates = new TreeSet<StateNfa>(new _NfaStateComp());
	private Map<String, StateDfa> nextStateMap = new HashMap<String, StateDfa>();
	private String stateType = null;
	private boolean processed = false;

	private class _NfaStateComp implements Comparator<StateNfa> {
		public int compare(StateNfa e1, StateNfa e2) {
			return Long.valueOf(e1.getName()).compareTo(
					Long.valueOf(e2.getName()));
		}
	}

	public boolean hasFinalNFAState() {
		for (StateNfa s : nfaStates) {
			if (s.geType().equals(StringConstants.END_STATE)) {
				return true;
			}
		}
		return false;
	}

	public StateDfa(Set<StateNfa> nfaStates) {
		this.nfaStates = nfaStates;
		if (hasFinalNFAState()) {
			stateType = StringConstants.END_STATE;
		} else {
			stateType = StringConstants.INTERMEDIATE_STATE;
		}
	}

	public StateDfa getNextState(String c) {
		return nextStateMap.get(c);
	}

	public void addNextState(String input, StateDfa nextState) {

		if (input != null && nextState != null) {
			nextStateMap.put(input, nextState);
		} else {
			throw new IllegalArgumentException(
					"Either input or next state is null");
		}
	}

	public Set<StateNfa> getNextStatesWithEClosure(String input) {
		Set<StateNfa> eStates = new HashSet<StateNfa>();
		Set<StateNfa> nextStates = new HashSet<StateNfa>();

		for (StateNfa s : nfaStates) {
			Set<StateNfa> nfaSet = s.getNextStates(input);
			if (nfaSet != null && !nfaSet.isEmpty()) {
				nextStates.addAll(s.getNextStates(input));
			}
		}

		for (StateNfa s : nextStates) {
			Set<StateNfa> nfaSet = s.getEpsilonClosure();
			if (nfaSet != null && !nfaSet.isEmpty()) {
				eStates.addAll(s.getEpsilonClosure());
			}
		}
		return eStates;
	}

	public Set<String> getAllInputsExceptEpsilon() {
		Set<String> inputs = getAllInputs();
		inputs.remove(StringConstants.EPSILON_INPUT);
		return inputs;
	}

	public Set<String> getAllInputs() {
		Set<String> inputs = new HashSet<String>();
		for (StateNfa s : nfaStates) {
			inputs.addAll(s.getAllInputs());
		}
		return inputs;
	}

	public String getStateType() {
		return stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nfaStates == null) ? 0 : nfaStates.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateDfa other = (StateDfa) obj;
		if (nfaStates == null) {
			if (other.nfaStates != null)
				return false;
		} else if (!nfaStates.equals(other.nfaStates))
			return false;
		return true;
	}

}
