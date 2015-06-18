package com.parseTree.grammer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.parseTree.objects.*;
import com.regular.expression.RegularExpParser;

public class GrammerRulesLoader {

	private Set<String> terminals = new HashSet<String>();
	private Set<String> nonTerminals = new HashSet<String>();
	private static final String CHAR_KEY = "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|\\s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|_";
	private RegularExpParser charparser = null;
	private List<Rule> rulesSet = new ArrayList<Rule>();
	private Map<String, Set<Rule>> rulesSetMap = new LinkedHashMap<String, Set<Rule>>();
	private boolean rulesLoaded = false;
	private Rule startRule = null;
	Map<String, Set<String>> firstSets = new HashMap<String, Set<String>>();
	Map<String, Set<String>> followSets = new HashMap<String, Set<String>>();
	Map<String, NodeInfo> rulesNodeInfoMap = new HashMap<String, NodeInfo>();

	public GrammerRulesLoader() {
		charparser = new RegularExpParser().compile(CHAR_KEY);
	}	
	
	
	public Map<String, NodeInfo> getRulesNodeInfoMap() {
		return rulesNodeInfoMap;
	}
	
	public Map<String, Set<String>> getFirstSets() {
		this.load();
		return firstSets;
	}
	
	public Map<String, Set<String>> getFollowSets() {
		this.load();
		return followSets;
	}
	
	public void load() {

		if (rulesLoaded) {
			return;
		}

		File grammerFile = new File(".\\grammer.g");
		BufferedReader br = null;

		try {

			FileInputStream fin = new FileInputStream(grammerFile);
			br = new BufferedReader(new InputStreamReader(fin));
			String line = null;

			while (!(line = br.readLine()).equalsIgnoreCase("%TERMINALS%"))
				;
			while (!(line = br.readLine()).equalsIgnoreCase("%END%")) {
				if (!line.startsWith("//")) {
					terminals.add(line.trim());
				}
			}

			while (!(line = br.readLine()).equalsIgnoreCase("%GRAMMER%"))
				;
			while (!(line = br.readLine()).equalsIgnoreCase("%END%")) {
				if (!line.startsWith("//") && line.indexOf("=")>-1) {

					String nTerminal = line.substring(0, line.indexOf('='));
					nonTerminals.add(nTerminal);
					Rule rule = _parseRuleStr(line.trim());

					if (rule != null) {
						if (startRule == null) {
							startRule = rule;
						}

						rulesSet.add(rule);

						// Add Rule in Rule set Map
						Set<Rule> rules = rulesSetMap.get(rule.getLHS());
						if (rules == null) {
							rules = new LinkedHashSet<Rule>();
							rulesSetMap.put(rule.getLHS(), rules);
						}
						rules.add(rule);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		
		calculateFirstSets();
		calculateFollowSets();
		
		rulesLoaded = true;
	}

	public Rule getStartRule() {
		this.load();
		return startRule;
	}

	public void reLoad() {
		rulesLoaded = false;
		this.load();
	}

	public Set<String> getTerminals() {
		this.load();
		return terminals;
	}

	public Set<String> getNonTerminals() {
		this.load();
		return nonTerminals;
	}

	public List<Rule> getRuleSet() {
		this.load();
		return rulesSet;
	}

	public boolean areRulesLoaded() {
		return rulesLoaded;
	}

	public Map<String, Set<Rule>> getRulesSetMap() {
		this.load();
		return rulesSetMap;
	}

	private void calculateFirstSets() {
		Map<String, String> nTerm_calculated = new HashMap<String, String>();
		Set<String> nTerminals = rulesSetMap.keySet();
		for (String nTerm : nTerminals) {
			if (nTerm_calculated.get(nTerm) == null) {
				_calculateFirstSets(nTerm, nTerm_calculated);
			}
		}
	}

	private void _calculateFirstSets(String nTerm,
			Map<String, String> nTerm_calculated) {
		Set<Rule> rules = rulesSetMap.get(nTerm);

		if(rules!=null){
		for (Rule r : rules) {
			if (firstSets.get(nTerm) == null) {
				firstSets.put(nTerm, new HashSet<String>());
			}

				String fstString = r.getRHS().get(0);
				
				if (!fstString.equals(nTerm)) {

					if (terminals.contains(fstString)) {
						firstSets.get(nTerm).add(fstString);
					} else if (fstString.equals("~")) {
						firstSets.get(nTerm).add(fstString);
					} else {

						if (!fstString.equals(nTerm)) {
							if (nTerm_calculated.get(nTerm) == null) {
								_calculateFirstSets(fstString, nTerm_calculated);
							}

							if (firstSets.get(fstString) != null) {
								firstSets.get(nTerm).addAll(
										firstSets.get(fstString));
							}
						}
					}
				}
		   }
		}
		nTerm_calculated.put(nTerm, "true");
	}

	private void calculateFollowSets() {
		Map<String, String> nTerm_calculated = new HashMap<String, String>();
		Set<String> nTerminals = rulesSetMap.keySet();

		for (String nTerm : nTerminals) {
			if (nTerm_calculated.get(nTerm) == null && !nTerm.equals("~")) {
				_calculateFollowSets(nTerm, nTerm_calculated);
			}
		}
	}

	private void _calculateFollowSets(String nTerm,
			Map<String, String> nTerm_calculated) {

		List<Rule> rules = rulesSet;
		
		if(nTerm.equals("~")){ return;}

		if (followSets.get(nTerm) == null) {
			followSets.put(nTerm, new HashSet<String>());
		}

		for (Rule r : rules) {
			for (int i = 0; i < r.getRHS().size(); i++) {

				if (nTerm.equals(r.getRHS().get(i))) {

					if (i + 1 < r.getRHS().size()) {

						if (terminals.contains(r.getRHS().get(i + 1))) {
							followSets.get(nTerm).add(r.getRHS().get(i + 1));
						} else {
							followSets.get(nTerm).addAll(
									firstSets.get(r.getRHS().get(i + 1)));
						}
					} else {

						if (!r.getLHS().equals(nTerm)) {

							if (nTerm_calculated.get(r.getLHS()) == null) {
								_calculateFollowSets(r.getLHS(),
										nTerm_calculated);
							}
							followSets.get(nTerm).addAll(
									followSets.get(r.getLHS()));
						}
					}
				}
			}

		}
		nTerm_calculated.put(nTerm, "true");
	}

	public Rule _parseRuleStr(String ruleStr) {

		int index = ruleStr.indexOf('=');
		if (index < 0) {
			return null;
		}

		String syntaxInfo = ruleStr.substring(ruleStr.indexOf("[") + 1,
				ruleStr.indexOf("]") + 1).trim();
		String nodeInfoArry[]=syntaxInfo.split(":");

		ruleStr = ruleStr.substring(0, ruleStr.indexOf("[")).trim();
		
		String nTerminal = ruleStr.substring(0, index);
		String rest = ruleStr.substring(index + 1);
		rest = rest + " ";

		Rule rule = new Rule(nTerminal);
		StringBuffer tmpValueBuffer = new StringBuffer(20);

		for (int i = 0; i < rest.length(); i++) {
			Character c = rest.charAt(i);

			if (charparser.match(String.valueOf(c))) {
				tmpValueBuffer.append(c);
			} else {

				if (tmpValueBuffer.length() > 0) {
					rule.addRhsItem(tmpValueBuffer.toString());
					tmpValueBuffer.setLength(0);
				}

				if (c != ' ') {
					rule.addRhsItem(String.valueOf(c));
				}
			}
		}

		// The following information is needed for generating AST
		NodeInfo nodeInfo = null;

		if (nodeInfoArry.length > 1) {
			nodeInfo = new NodeInfo(nodeInfoArry[0], nodeInfoArry[1]);
		} else {
			nodeInfo = new NodeInfo(nodeInfoArry[0]);
		}
		rulesNodeInfoMap.put(rule.getRuleNo(), nodeInfo);
		
		return rule;
	}

	public static void main(String args[]) {
		GrammerRulesLoader loader = new GrammerRulesLoader();
		loader.load();
		System.out.println(loader.getRuleSet());
	}
}
