package com.regular.expression;

class CharUtils {

	private static final char operators[] = { '*', '?', '+', '.', '|' };
	private static final char OPEN_BRACKET = '(';
	private static final char CLOSE_BRACKET = ')';
	private static final char singleOperators[] = { '*', '?', '+' };
	private static final char BACK_SLASH = '\\';
	private static final char SPACE = 's';
	
	
	public static boolean isSpace(char inputC){
		return inputC==SPACE;
	}
	
	public static boolean isBackSlash(char inputC){
		return inputC==BACK_SLASH;
	}

	public static boolean isAlphabet(char inputC) {
		if (inputC == OPEN_BRACKET || inputC == CLOSE_BRACKET) {
			return false;
		}
		for (char c : operators) {
			if (c == inputC) {
				return false;
			}
		}
		return true;
	}

	public static boolean isOpeartor(char inputC) {
		for (char c : operators) {
			if (c == inputC) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSingleOperator(char inputC) {
		for (char c : singleOperators) {
			if (c == inputC) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOpenBracket(char inputC) {
		return inputC == OPEN_BRACKET;
	}

	public static boolean isCloseBracket(char inputC) {
		return inputC == CLOSE_BRACKET;
	}

	public static boolean isBracket(char inputC) {
		return inputC == CLOSE_BRACKET || inputC == OPEN_BRACKET;
	}

}
