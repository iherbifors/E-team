package MyCalc;
import java.util.*;

public class Infix2Postfix {

    public static String convert(String exp){
        if(exp==null || exp.length()==0) return null;

        StringTokenizer st = new StringTokenizer(exp,"+-*/()",true);
        Stack<String> stack = new Stack<String>();
        StringBuffer buf = new StringBuffer();

        while(st.hasMoreTokens()) {
            String tok = st.nextToken().trim();  //trim() => 앞 뒤 공백제거.
            if(tok.length()==0) continue;

            // 음수 처리
            if(tok.equals("-")) {
                // 조건: 시작이거나, 이전이 연산자거나 '('
                if(buf.length()==0 || 
                   buf.toString().endsWith("(") ||
                   (buf.length()>0 && opType(buf.substring(buf.length()-1))>0)) {

                    String next = st.nextToken().trim(); // 숫자 가져오기
                    buf.append("-").append(next).append(" "); // -n 형태로 저장
                    continue;
                }
            }

            // 여는 괄호
            if(tok.equals("(")) {
                stack.push(tok);
            }

            // 닫는 괄호
            else if(tok.equals(")")) {
                while(!stack.peek().equals("(")) {
                    buf.append(stack.pop()).append(" ");
                }
                stack.pop(); // '(' 제거
            }

            // 연산자
            else if (opType(tok)>0) {
                while(!stack.empty()) {

                    if(stack.peek().equals("(")) break;

                    String op2 = stack.peek();
                    int p1 = getPriority(tok.charAt(0));
                    int p2 = getPriority(op2.charAt(0));

                    if (p1 <= p2){
                        buf.append(stack.pop()).append(" ");
                    } else break;
                }
                stack.push(tok);
            }

            // 숫자
            else {
                buf.append(tok).append(" ");
            }
        }

        // 남은 연산자 처리
        while(!stack.empty()) {
            buf.append(stack.pop()).append(" ");
        }

        return buf.toString();
    }

    public static int opType(String op) {
        op=op.trim();
        if(op.length() > 1 || op.length()==0) {
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
            case '-': return 1;
            case '*':
            case '/': return 2;
            default: return -1;
        }
    }

    public static void main(String[] args) {
        String exp="-2+3*(4-6)";
        System.out.printf("%s -> %s\n",exp,Infix2Postfix.convert(exp));
    }
}