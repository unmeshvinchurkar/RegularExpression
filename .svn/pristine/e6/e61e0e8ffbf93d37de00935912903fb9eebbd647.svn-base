package com.regular.expression;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.regular.expression.objects.MachineNfa;
import com.regular.expression.objects.MachineDfa;
import com.regular.expression.objects.StateDfa;
import com.regular.expression.objects.StateNfa;
import com.regular.expression.objects.StringConstants;

class RegularExpHelper {
	
	public static MachineDfa convertNfaToDfa(MachineNfa m) {
		Map<StateDfa, StateDfa> dfaSet = new HashMap<StateDfa, StateDfa>();

		StateDfa startDfa = new StateDfa(m.getStartState().getEpsilonClosure());
		startDfa.setStateType(StringConstants.START_STATE);
		dfaSet.put(startDfa, startDfa);
		Queue<StateDfa> queue = new LinkedList<StateDfa>();
		queue.add(startDfa);

		while (!queue.isEmpty()) {

			StateDfa dfaState = queue.remove();

			if (!dfaState.isProcessed()) {
				dfaState.setProcessed(true);

				Set<String> inputs = dfaState.getAllInputsExceptEpsilon();
				for (String c : inputs) {
					Set<StateNfa> nextStates = dfaState
							.getNextStatesWithEClosure(c);

					StateDfa tmpDfa = new StateDfa(nextStates);

					if (!dfaSet.containsKey(tmpDfa)) {
						dfaSet.put(tmpDfa, tmpDfa);
					}
					tmpDfa = dfaSet.get(tmpDfa);
					dfaState.addNextState(c, tmpDfa);

					if (!tmpDfa.isProcessed()) {
						queue.add(tmpDfa);
					}
				}
			}
		}
		for (StateDfa dfa : dfaSet.values()) {
			dfa.setProcessed(false);
		}
		dfaSet.clear();
		return new MachineDfa(startDfa);
	}

	public static MachineNfa convertPostFixToNfa(String postfixStr) {
		Stack<MachineNfa> nfaMachineStack = new Stack<MachineNfa>();

		for (int index = 0; index < postfixStr.length(); index++) {
			Character currentChar = postfixStr.charAt(index);

			if (CharUtils.isSpace(currentChar)) {
				nfaMachineStack.push(RegularExpHelper.createMachine(
						String.valueOf(' '), null, null));				
			}
			else if (CharUtils.isBackSlash(currentChar)) {
				nfaMachineStack
						.push(RegularExpHelper.createMachine(
								String.valueOf(postfixStr.charAt(++index)),
								null, null));

			} else if (CharUtils.isAlphabet(currentChar)) {
				nfaMachineStack.push(RegularExpHelper.createMachine(
						String.valueOf(currentChar), null, null));

			} else if (CharUtils.isSingleOperator(currentChar)) {
				nfaMachineStack.push(RegularExpHelper.mergeMachines(
						nfaMachineStack.pop(), String.valueOf(currentChar),
						null));
			} else if (CharUtils.isOpeartor(currentChar)) {

				MachineNfa m2 = nfaMachineStack.pop();
				MachineNfa m1 = nfaMachineStack.pop();
				nfaMachineStack.push(RegularExpHelper.mergeMachines(m1,
						String.valueOf(currentChar), m2));
			} else if (CharUtils.isOpenBracket(currentChar)) {
				StringBuffer sb = new StringBuffer();
				index++;
				while (index < postfixStr.length()					
						&& !CharUtils.isCloseBracket(postfixStr.charAt(index))) {

					if (CharUtils.isBackSlash(postfixStr.charAt(index))) {
						sb.append(postfixStr.charAt(index++));
						sb.append(postfixStr.charAt(index));
						index++;
					} else {
						sb.append(postfixStr.charAt(index++));
					}
				}
				nfaMachineStack.push(convertPostFixToNfa(sb.toString()));
			}
		}
		return nfaMachineStack.pop();
	}

	private static MachineNfa _createMachine(String input, String operator) {
		MachineNfa m = null;
		StateNfa startState = new StateNfa(StringConstants.START_STATE);
		StateNfa endtState = new StateNfa(StringConstants.END_STATE);

		if (operator == null) {
			startState.addNextState(input, endtState);
		} else if (operator.equals("?")) {
			startState.addNextState(input, endtState);
			startState.addNextState(StringConstants.EPSILON_INPUT, endtState);
		} else if (operator.equals("*")) {
			startState.addNextState(input, startState);
			startState.addNextState(StringConstants.EPSILON_INPUT, endtState);
		} else if (operator.equals("+")) {
			StateNfa is = new StateNfa(StringConstants.INTERMEDIATE_STATE);
			startState.addNextState(input, is);
			is.addNextState(input, endtState);
			endtState.addNextState(input, endtState);
		}

		m = new MachineNfa(startState, endtState);
		return m;
	}

	public static MachineNfa createMachine(String input, String operator,
			Character input1) {
		MachineNfa machine = null;

		if (input1 == null) {
			machine = _createMachine(input, operator);
		} else if (operator.equals(".") || operator.equals("|")) {
			MachineNfa m1 = _createMachine(String.valueOf(input), null);
			MachineNfa m2 = _createMachine(String.valueOf(input1), null);
			machine = mergeMachines(m1, operator, m2);
		}
		return machine;
	}

	public static MachineNfa mergeMachines(MachineNfa m1, String operator,
			MachineNfa m2) {
		MachineNfa newMachine = null;
		StateNfa startState = null;
		StateNfa endtState = null;

		if (operator == null || operator.equals(".")) {
			newMachine = new MachineNfa(m1.getStartState(), m2.getEndState());

			m1.getEndState().setType(StringConstants.INTERMEDIATE_STATE);
			m2.getStartState().setType(StringConstants.INTERMEDIATE_STATE);
			m1.getEndState().addNextState(StringConstants.EPSILON_INPUT,
					m2.getStartState());
		} else if (operator.equals("|")) {
			startState = new StateNfa(StringConstants.START_STATE);
			endtState = new StateNfa(StringConstants.END_STATE);
			newMachine = new MachineNfa(startState, endtState);

			startState.addNextState(StringConstants.EPSILON_INPUT,
					m1.getStartState());
			startState.addNextState(StringConstants.EPSILON_INPUT,
					m2.getStartState());
			m1.getEndState().addNextState(StringConstants.EPSILON_INPUT,
					endtState);
			m2.getEndState().addNextState(StringConstants.EPSILON_INPUT,
					endtState);
			m1.getEndState().setType(StringConstants.INTERMEDIATE_STATE);
			m2.getEndState().setType(StringConstants.INTERMEDIATE_STATE);
		} else if (operator.equals("?")) {
			startState = m1.getStartState();
			endtState = m1.getEndState();
			startState.addNextState(StringConstants.EPSILON_INPUT, endtState);

			newMachine = m1;
		} else if (operator.equals("*")) {
			startState = m1.getStartState();
			endtState = m1.getEndState();
			endtState.addNextState(StringConstants.EPSILON_INPUT, startState);
			startState.addNextState(StringConstants.EPSILON_INPUT, endtState);

			newMachine = m1;
		} else if (operator.equals("+")) {
			startState = m1.getStartState();
			endtState = m1.getEndState();
			endtState.addNextState(StringConstants.EPSILON_INPUT, startState);

			newMachine = m1;
		}
		return newMachine;
	}
}
