package com.parseTree.table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import com.parseTree.objects.*;
import com.parseTree.grammer.GrammerRulesLoader;

public class ActionTableGenerator {

	private GrammerRulesLoader loader = null;
	private Set<String> terminals = null;
	private Set<String> nTerminals = null;
	private List<Rule> rulesSet = null;
	Map<String, Set<Rule>> rulesMap = null;

	public ActionTableGenerator() {
		loader = new GrammerRulesLoader();
	}

	public void generateActionTable() {
		rulesMap = loader.getRulesSetMap();
		terminals = loader.getTerminals();
		nTerminals = loader.getNonTerminals();
		rulesSet = loader.getRuleSet();

		State startState = ActionTableHelper.createState(rulesSet, rulesMap,
				terminals);
		startState.setStateNo(ActionTableHelper.STATE_COUNT++);
		ActionTableHelper.createDFA(startState, rulesMap, terminals);

		_writeDfaToFile(startState, terminals, nTerminals);

		print(startState);
	}

	public void print(State startState) {

		List<Rule> rules = loader.getRuleSet();
		System.out.println("________________________ PRODUCTIONS_________\n");
		for (Rule r : rules) {
			System.out.println(r);
		}
		System.out
				.println("________________STATES_______________________________\n");

		Queue<State> queue = new LinkedList<State>();
		queue.add(startState);
		while (!queue.isEmpty()) {
			State state = queue.remove();

			if (!state.isProcessed()) {
				state.setProcessed(true);
				System.out.println(state);

				if (state.getNextStates() != null) {
					for (State s : state.getNextStates()) {
						if (!s.isProcessed()) {
							queue.add(s);
						}
					}
				}
			}
		}
	}

	private void _writeDfaToFile(State startState, Set<String> terminals,
			Set<String> nTerminals) {

		Set<State> stateSet = new HashSet<State>();
		stateSet.add(startState);

		File file = new File(".//action_table.txt");
		BufferedWriter bw = null;
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			System.out.println(file.getAbsoluteFile());

			bw = new BufferedWriter(fw);
			bw.write("%ACTION_TABLE%\n");

			_writeHeader(bw, terminals, nTerminals);

			Queue<State> queue = new LinkedList<State>();
			queue.add(startState);

			while (!queue.isEmpty()) {
				State state = queue.remove();
				if (!state.isProcessed()) {
					state.setProcessed(true);
					String stateName = "s" + state.getStateNo();

					StringBuffer line = new StringBuffer(100);
					line.append(stateName);

					_addItems(queue, (startState==state), terminals, line, state, stateSet, true);
					_addItems(queue, (startState==state), nTerminals, line, state, stateSet, false);

					bw.write(line.toString());
					bw.write("\n");
				}
			}

			bw.write("%END%");

			_writeTerminals(bw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
			}
		}

		for (State s : stateSet) {
			s.setProcessed(false);
		}
	}

	private void _writeTerminals(BufferedWriter bw) throws IOException {
		bw.write("\n\n%TERMINALS%\n");
		for (String value : terminals) {
			bw.write(value + "\n");
		}
		bw.write("%END%");
	}

	private void _addItems(Queue<State> queue,boolean isStartState, Set<String> items,
			StringBuffer line, State state, Set<State> stateSet,
			boolean terminal) {

		for (String input : items) {

			line.append(",");			
			StringBuffer sb = new StringBuffer(20);
			
//			if(input.equals("$") && isStartState){				
//				sb.append("accept");				
//			}
//			else
			
			{			

			State nextState = state.getNextState(input);

			if (nextState != null) {
				sb.append("s" + nextState.getStateNo());

				stateSet.add(nextState);

				if (!nextState.isProcessed()) {
					queue.add(nextState);
				}
			 }
			}

			if (terminal) {
				Set<Rule> reductions = state.getReductions();
				if (reductions.size() > 0) {
					for (Rule r : reductions) {
						sb.append(";");
						sb.append("r");
						sb.append(r.getRuleNo());
					}
				}
			}

			if (sb.length() > 0) {
				String frac = sb.toString();
				if (frac.charAt(0) == ';') {
					line.append(frac.substring(1));
				} else {
					line.append(frac);
				}
			} else {
				line.append("_");
			}
		}
	}

	private void _writeHeader(Writer writer, Set<String> terminals,
			Set<String> nTerminals) throws IOException {
		StringBuffer sb = new StringBuffer(500);

		sb.append("State,");

		for (String terminal : terminals) {
			sb.append(terminal);
			sb.append(",");
		}
		for (String nTerminal : nTerminals) {
			sb.append(nTerminal);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("\n");
		writer.write(sb.toString());

	}

	public static void main(String args[]) {
		ActionTableGenerator generator = new ActionTableGenerator();
		generator.generateActionTable();
	}
}
