package MyCalc;
import java.util.StringTokenizer;

public class StringToken {
    public static void main(String[] args) {
        String exp = "(121+20/5)*4-152";

    StringTokenizer st= new StringTokenizer(exp,"+-*/()",true);
    while (st.hasMoreTokens()) {
        String token = st.nextToken();
        System.out.printf("token -> %s \n",token);
    }
    }
}
