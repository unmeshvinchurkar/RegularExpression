package com.regular.expression.objects;

public class MachineDfa {

	private StateDfa currentState = null;
	private StateDfa startState = null;

	public MachineDfa(StateDfa startState) {
		if (startState == null) {
			throw new IllegalArgumentException("Start state cannot be Null");
		}
		this.currentState = startState;
		this.startState = startState;
	}

	public void reset() {
		this.currentState = startState;
	}

	public boolean isInFinalState() {
		return currentState != null ? currentState.getStateType().equals(
				StringConstants.END_STATE) : false;
	}

	public boolean move(Character input) {
		StateDfa tmp = currentState.getNextState(String.valueOf(input));
		if (tmp != null) {
			currentState = tmp;
			return true;
		}
		return false;
	}
}
