package com.regular.expression;

import java.util.Stack;

class ExpressionHelper {

	public static String addDotOperatorToInput(String input) {
		StringBuffer sb = new StringBuffer();
		sb.append(input.charAt(0));

		for (int i = 1; i < input.length(); i++) {
			Character currentChar = input.charAt(i);

			if ((CharUtils.isAlphabet(currentChar)
					|| CharUtils.isOpenBracket(currentChar) || CharUtils
						.isBackSlash(currentChar) || CharUtils.isSpace(currentChar))
					&& (CharUtils.isAlphabet(input.charAt(i - 1))
							|| CharUtils.isSingleOperator(input.charAt(i - 1)) || CharUtils
								.isCloseBracket(input.charAt(i - 1)))) {
				sb.append(".");

			}
			sb.append(currentChar);

			if (CharUtils.isBackSlash(currentChar)) {
				sb.append(input.charAt(++i));
			}
		}
		return sb.toString();
	}

	public static String convertToPostFix(String input) {
		StringBuffer sb = new StringBuffer();
		Stack<Character> opStack = new Stack<Character>();

		for (int i = 0; i < input.length(); i++) {
			Character currentChar = input.charAt(i);
			
			if (CharUtils.isSpace(currentChar)) {
				sb.append(currentChar);
			}
			else if (CharUtils.isBackSlash(currentChar)) {
				sb.append(currentChar);
				sb.append(input.charAt(++i));
			} else if (CharUtils.isSingleOperator(currentChar)
					|| CharUtils.isAlphabet(currentChar)) {
				sb.append(currentChar);
			} else if (CharUtils.isOpenBracket(currentChar)) {
				sb.append(currentChar);
				opStack.push(currentChar);
			} else if (CharUtils.isOpeartor(currentChar)) {

				if (currentChar == '.') {
					while (!opStack.isEmpty() && opStack.peek() != '|'
							&& opStack.peek() != '(') {
						sb.append(opStack.pop());
					}
				} else if (currentChar == '|') {
					while (!opStack.isEmpty() && opStack.peek() != '(') {
						sb.append(opStack.pop());
					}
				}
				opStack.push(currentChar);
			} else if (CharUtils.isCloseBracket(currentChar)) {
				while (!opStack.isEmpty() && opStack.peek() != '(') {
					sb.append(opStack.pop());
				}
				sb.append(currentChar);
				if (!opStack.isEmpty()) {
					opStack.pop();
				}
			}
		}
		while (!opStack.isEmpty()) {
			sb.append(opStack.pop());
		}
		return sb.toString();
	}

}
