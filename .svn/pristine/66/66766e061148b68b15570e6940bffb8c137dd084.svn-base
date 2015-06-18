package com.parseTree.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionTable {

	private List<String[]> table = null;
	private Map<String, Integer> symbol_index_map = new HashMap<String, Integer>();
	private Map<String, Integer> state_index_map = new HashMap<String, Integer>();

	public ActionTable(String columns[]) {
		table = new ArrayList<String[]>();
		table.add(columns);

		int i = 0;
		for (String columnName : columns) {
			symbol_index_map.put(columnName, i++);
		}
	}

	public void addRow(String row[]) {
		table.add(row);
		state_index_map.put(row[0], table.size() - 1);
	}

	public String getValue(int rowIndex, int colIndex) {
		return (table.get(rowIndex))[colIndex - 1];
	}

	public String getValue(int rowIndex, String columnName) {
		return (table.get(rowIndex))[symbol_index_map.get(columnName)];
	}

	public String getValue(String state, String columnName) {
		return (table.get(state_index_map.get(state)))[symbol_index_map
				.get(columnName)];
	}

}
