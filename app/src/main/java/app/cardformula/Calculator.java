package app.cardformula;

import java.util.LinkedList;

public class Calculator {
    static boolean isDelim(char c) { // тру если пробел
        return c == ' ';
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    static int priority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    static void processOperator(LinkedList<Integer> st, char op) {
        int r = st.isEmpty() ? 0 : st.removeLast();
        int l;
        if (st.isEmpty()){
            l = 0;
        } else {
            l = st.removeLast();
        }
        switch (op) {
            case '+':
                st.add(l + r);
                break;
            case '-':
                st.add(l - r);
                break;
            case '*':
                st.add(l * r);
                break;
            case '/':
                st.add(l / r);
                break;
            case '%':
                st.add(l % r);
                break;
        }
    }

    public static int eval(char[] cards) {
        LinkedList<Integer> st = new LinkedList<Integer>();
        LinkedList<Character> op = new LinkedList<Character>();
        for (int i = 0; i < cards.length; i++) {
            char c = cards[i];
            if (isDelim(c))
                continue;
            if (c == '(')
                op.add('(');
            else if (c == ')') {
                while (op.getLast() != '(')
                    processOperator(st,op.removeLast());
                op.removeLast();
            } else if (isOperator(c)) {
                while (!op.isEmpty() && priority(op.getLast()) >= priority(c))
                    processOperator(st, op.removeLast());
                op.add(c);
            } else {
                StringBuilder operand = new StringBuilder();
                while (i < cards.length && Character.isDigit(cards[i]))
                    operand.append(cards[i++]);
                --i;
                st.add(Integer.parseInt(operand.toString()));
            }
        }

        while (!op.isEmpty())
            processOperator(st, op.removeLast());

        return st.get(0);
    }

    static boolean Compare(char[] cards) {
        int n = new String(cards).indexOf('=');
        if (n <= 0 || n == cards.length - 1) return false;
        char[] first = new char[n];
        System.arraycopy(cards, 0, first, 0, n);
        boolean isDigit = false;
        for (char c : first){
            if (Character.isDigit(c)) {
                isDigit = true;
                break;
            }
        }
        char[] second = new char[cards.length - n - 1];
        System.arraycopy(cards, n + 1, second, 0, second.length);
        for (char c : second){
            if (Character.isDigit(c)) {
                isDigit = true;
                break;
            }
        }
        if (!isDigit) return false;
        return eval(first) == eval(second);
    }
}
