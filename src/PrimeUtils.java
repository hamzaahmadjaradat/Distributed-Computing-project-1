public class PrimeUtils {
    public static boolean isPrime(int x) {
        if (x <= 1) return false;
        if (x == 2) return true;
        if (x % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(x); i += 2) {
            if (x % i == 0) return false;
        }
        return true;
    }

    public static int nextPrime(int x) {
        int num = x + 1;
        while (!isPrime(num)) {
            num++;
        }
        return num;
    }
}
