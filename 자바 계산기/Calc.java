import java.util.*;

// 문제 2: 괄호를 처리할 수 있도록 수정한 Calc
// Infix2Postfix.convert()가 괄호를 처리해서 후위 표기법으로 바꿔주기 때문에
// Calc.eval()은 교재 코드 그대로 사용해도 괄호가 포함된 수식을 계산할 수 있음
//
// 전체 흐름:
// "(2 + 3) * 4"  →  Infix2Postfix.convert()  →  "2 3 + 4 *"  →  Calc.eval()  →  20.0
public class Calc {

    public static double eval(String exp) {
        StringTokenizer st = new StringTokenizer(exp);
        Stack<Double> stack = new Stack<Double>();

        while (st.hasMoreTokens()) {
            String tok = st.nextToken();

            if (Infix2Postfix.opType(tok) > 0) {
                double v1 = stack.pop(); // 오른쪽 피연산자
                double v2 = stack.pop(); // 왼쪽 피연산자
                double value = 0;

                switch (Infix2Postfix.opType(tok)) {
                    case 1: value = v2 + v1; break; // +
                    case 2: value = v2 - v1; break; // -
                    case 3: value = v2 * v1; break; // *
                    case 4: value = v2 / v1; break; // /
                }
                stack.push(value);
            } else {
                stack.push(Double.parseDouble(tok));
            }
        }

        double result = stack.pop();
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 변경: 괄호가 포함된 수식도 입력받을 수 있도록 안내 메시지 수정
        System.out.print("수식 입력 (예: (2 + 3) * 4): ");
        String infix = sc.nextLine();

        // 변경:  Infix2Postfix.convert()가 괄호를 처리하므로
        // 괄호가 포함된 중위 표기식도 올바른 후위 표기식으로 변환됨
        String postfix = Infix2Postfix.convert(infix);

        double value = Calc.eval(postfix);
        System.out.printf("%s = %.2f%n", infix, value);

        sc.close();
    }
}
