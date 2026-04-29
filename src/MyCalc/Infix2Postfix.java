package MyCalc;

import java.util.*;

public class Infix2Postfix {

    public static String convert(String exp) {
        if (exp == null || exp.length() == 0) return null;

        // 변경: 구분자에 '(' ')' 와 공백 추가
        // 원본: new StringTokenizer(exp, "+-*/", true)
        // 괄호도 별도 토큰으로 분리되도록 구분자에 포함시킴
        StringTokenizer st = new StringTokenizer(exp, "+-*/() ", true);
   

        Stack<String> stack = new Stack<String>();
        StringBuffer buf = new StringBuffer();
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();

            // 변경: 공백 토큰은 건너뜀 (구분자에 공백을 추가했기 때문에 필요)
            if (tok.trim().isEmpty()) continue;
  

            // 변경: '(' 처리: 여는 괄호는 무조건 스택에 push
            // 괄호 안의 연산자들과 바깥 연산자의 우선순위를 분리하기 위해
            if (tok.equals("(")) {
                stack.push(tok);

            // 변경: ')' 처리: '('가 나올 때까지 스택에서 꺼내 출력, '('는 제거
            // 괄호 안의 연산자들을 먼저 출력 → 괄호 안을 먼저 계산하는 효과
            } else if (tok.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    buf.append(stack.pop());
                    buf.append(" ");
                }
                if (!stack.isEmpty()) {
                    stack.pop(); // '(' 제거 (출력하지 않음)
                }
    

            } else if (opType(tok) > 0) {
                while (!stack.empty()) {
                    String op2 = stack.peek();

                    // 변경: '('를 만나면 더 이상 꺼내지 않고 중단
                    // 괄호 안쪽은 독립 영역이므로 바깥 연산자와 우선순위 비교 안 함
                    if (op2.equals("(")) break;
       

                    int p1 = getPriority(tok.charAt(0));
                    int p2 = getPriority(op2.charAt(0));
                    if (p1 <= p2) {
                        buf.append(op2);
                        buf.append(" ");
                        stack.pop();
                    } else break;
                }
                stack.push(tok.trim());
            } else {
                buf.append(tok.trim());
                buf.append(" ");
            }
        }
        while (!stack.empty()) {
            buf.append(stack.pop());
            buf.append(" ");
        }
        return buf.toString();
    }

    public static int opType(String op) {
        op = op.trim();
        if (op.length() > 1 || op.length() == 0) {
            return -1;
        }
        char c = op.charAt(0);
        switch (c) {
            case '+': return 1;
            case '-': return 2;
            case '*': return 3;
            case '/': return 4;
        }
        return -1;
    }

    private static int getPriority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        // 원본 테스트
        String exp = "12 + 2.5 * 5";
        System.out.printf("%s => %s %n", exp, Infix2Postfix.convert(exp));

        // 변경: 괄호 테스트 추가
        String exp2 = "(2 + 3) * 4";
        System.out.printf("%s => %s %n", exp2, Infix2Postfix.convert(exp2));

        String exp3 = "2 * (3 + 4)";
        System.out.printf("%s => %s %n", exp3, Infix2Postfix.convert(exp3));

        String exp4 = "(1 + 2) * (3 + 4)";
        System.out.printf("%s => %s %n", exp4, Infix2Postfix.convert(exp4));

    }
}
