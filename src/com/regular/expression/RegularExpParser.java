package com.regular.expression;

import java.util.LinkedHashSet;
import java.util.Set;
import com.regular.expression.objects.MachineDfa;

/**
 * Supports: 1. Regx characters: ?,*,s,+,| 2. Space: By 's' character. 3.
 * Special chars in string like: ?, * which are used in Regx expression if
 * preceded by back slash '\'.
 * 
 * @author UnmeshVinchurkar
 * 
 */
public class RegularExpParser {

	private MachineDfa _mDfa = null;
	private boolean compiled = false;
	private String className =null;
	private int moveCount = 0;

	public RegularExpParser compile(String input) {
		compiled = false;
		String inputWithDot = ExpressionHelper.addDotOperatorToInput(input);
		String postfixStr = ExpressionHelper.convertToPostFix(inputWithDot);
		_mDfa = RegularExpHelper.convertNfaToDfa(RegularExpHelper
				.convertPostFixToNfa(postfixStr));
		compiled = true;

		//System.out.println("Input with dot:   " + inputWithDot);
		//System.out.println("Post Fix Exp:   " + postfixStr);
		return this;
	}

	public int getMoveCount() {
		return moveCount;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean move(Character c) {
		boolean moved = _mDfa.move(c);
		if (moved) {
			moveCount++;
		}
		return moved;
	}

	public boolean isInFinalState() {
		return _mDfa.isInFinalState();
	}

	public void reset() {
		_mDfa.reset();
		moveCount = 0;
	}

	public boolean match(String input) {
		if (!compiled || _mDfa == null) {
			throw new RuntimeException("Regular expression not compiled");
		}
		_mDfa.reset();

		for (int i = 0; i < input.length(); i++) {
			if (!_mDfa.move(input.charAt(i))) {
				return false;
			}
		}
		if (_mDfa.isInFinalState()) {
			return true;
		}
		return false;
	}

	public Set<String> parseFrombeg(String input) {
		if (!compiled) {
			throw new RuntimeException("Regular expression not compiled");
		}
		Set<String> strSet = new LinkedHashSet<String>();
		_mDfa.reset();
		System.out.println("______________________________________________");

		int i = 0;
		for (i = 0; i < input.length(); i++) {
			if (!_mDfa.move(input.charAt(i))) {
				break;
			}
		}
		strSet.add(input.substring(0, i));
		return strSet;
	}

	public Set<String> parse(String input) {
		if (!compiled) {
			throw new RuntimeException("Regular expression not compiled");
		}

		Set<String> strSet = new LinkedHashSet<String>();
		_mDfa.reset();
		System.out
				.println("__________________________________________________________________________");

		for (int i = 0; i < input.length(); i++) {
			if (_mDfa.move(input.charAt(i))) {
				int start = i;
				int j = i + 1;

				if (_mDfa.isInFinalState()) {
					strSet.add(input.substring(start, j));
				}
				while (j < input.length() && _mDfa.move(input.charAt(j))) {
					j++;
					if (_mDfa.isInFinalState()) {
						strSet.add(input.substring(start, j));
					}
				}

				if (_mDfa.isInFinalState()) {
					strSet.add(input.substring(start, j));
					_mDfa.reset();
				}
			} else {
				_mDfa.reset();
			}
		}
		return strSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_mDfa == null) ? 0 : _mDfa.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegularExpParser other = (RegularExpParser) obj;
		if (_mDfa == null) {
			if (other._mDfa != null)
				return false;
		} else if (!_mDfa.equals(other._mDfa))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

	public static void main(String args[]) {
		RegularExpParser parser = new RegularExpParser();
		// parser.compile("a\\*\\|*b+|d+c+");
		// System.out.println(parser.parse("a*||bbbbddddccccc"));
		// System.out.println(parser.match("aaaaaab"));

		// SIGN?DIGIT*\.?DIGIT+
		// (-|+)?(0|1|2|3|4|5|6|7|8|9)*\.(0|1|2|3|4|5|6|7|8|9)+

		// parser.compile("a\\s*bd");
		// System.out.println(parser.parse("abd"));

		// (-|+)?(0|1|2|3|4|5|6|7|8|9)*~(0|1|2|3|4|5|6|7|8|9)+

		// parser.compile("(-|\\+)?(0|1|2|3|4|5|6|7|8|9)+");
//		parser.compile("(-|\\+)?(0|1|2|3|4|5|6|7|8|9)*(\\.)(0|1|2|3|4|5|6|7|8|9)+");
//		System.out.println(parser.match("-123.0"));
//		System.out.println(parser.parse("-123.0"));
//		System.out.println(parser.parseFrombeg("-123.0")); (\()
		
		parser.compile("&&|\\|\\|");//  |\\|\\| |::|!=|<=|>=|<|>
		System.out.println(parser.parse("i && j;")); 
	}
}
