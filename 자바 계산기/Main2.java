import java.util.*;

// ===================================================
// 문제 2: 괄호 처리 계산기 실행 파일
// ===================================================
public class Main2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 'e' 를 입력할 때까지 계속 계산
        while (true) {
            System.out.print("\n수식 입력: ");
            String infix = sc.nextLine().trim();

            // 'e' 입력 시 종료
            if (infix.equals("e")) {
                System.out.println("계산기를 종료합니다.");
                break;
            }

            // 빈 입력 건너뜀
            if (infix.isEmpty()) continue;

            // 중위 표기법 → 후위 표기법 변환
            String postfix = Infix2Postfix2.convert(infix);

            // 후위 표기법 수식 계산
            double value = Calc.eval(postfix);

            // 결과 출력
            System.out.printf("결과: %s = %.2f%n", infix, value);
        }

        sc.close();
    }
}
