package oauth2.provider.util.base;

import org.springframework.stereotype.Component;

@Component
public class Operator {

    public static int add(int a, int b) {
        if(a == 0) return b;
        return add((a & b) << 1, a ^ b);
    }


    public static int sub(int a, int b) {
        if(a == 0) return b;
        return sub((a & ~b) << 1, a ^ ~b);
    }


    public static int mul(int a, int b) {
        int res = 0;
        while(b != 0) {
            if((b & 1) != 0) {
                res = add(res, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }


    public static int div(int a, int b) {
        int res = 0;
        while(b != 0) {
            if((b & 1) != 0) {
                res = mul(res, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }

    public static int[] swap(int a, int b) {
        a ^= b;
        b ^= a;
        a ^= b;
        return new int[]{ a, b };
    }

    public static boolean compare(int a, int b) {
        return (a ^ b) == 0;
    }

    public static boolean isOgg(int a) {
        return ((a & 1) == 0);
    }
}
