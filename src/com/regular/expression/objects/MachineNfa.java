package com.regular.expression.objects;

public class MachineNfa {

	private StateNfa startState = null;
	private StateNfa endState = null;

	public MachineNfa(StateNfa startState, StateNfa endState) {
		if (startState == null) {
			throw new IllegalArgumentException("Start state cannot be Null");
		}
		if (endState == null) {
			throw new IllegalArgumentException("End state cannot be Null");
		}

		this.startState = startState;
		this.endState = endState;
	}

	public StateNfa getStartState() {
		return startState;
	}

	public StateNfa getEndState() {
		return endState;
	}

	@Override
	public String toString() {
		return "MachineNfa [startState=" + startState + ", endState="
				+ endState + "]";
	}

}
