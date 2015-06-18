package com.parseTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.parseTree.table.ActionTable;

public class ActionTableLoader {

	private ActionTable table = null;
	private boolean loaded = false;
	private Set<String> terminals = new HashSet<String>();

//	public Set<String> getTerminals() {
//		if (!loaded) {
//			this.load();
//		}
//		return terminals;
//	}

	public void load() {
		File file = new File(".//action_table.txt");
		BufferedReader br = null;

		try {
			FileInputStream input = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(input));
			String line = null;
			// Go to %ACTION_TABLE%
			while (!(line = br.readLine()).equalsIgnoreCase("%ACTION_TABLE%"))
				;

			while ((line = br.readLine()) == null)
				;

			String columnStr = line;
			table = new ActionTable(columnStr.split(","));

			while (!(line = br.readLine()).equalsIgnoreCase("%END%")) {
				if (!line.startsWith("//")) {
					table.addRow(line.split(","));
				}
			}

			while (!(line = br.readLine()).equalsIgnoreCase("%TERMINALS%"))
				;

			while (!(line = br.readLine()).equalsIgnoreCase("%END%")) {
				if (!line.startsWith("//")) {
					terminals.add(line.trim());
				}
			}

			// ///////////////////////////////////////////////////

			loaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public ActionTable getActionTable() {
		if (!loaded) {
			this.load();
		}
		return table;
	}

	@Override
	public String toString() {
		return "ActionTableLoader [table=" + table + "]";
	}

	public static void main(String args[]) {
		ActionTableLoader loader = new ActionTableLoader();
		System.out.println(loader.getActionTable());
	}

}
