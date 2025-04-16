package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.util.HashSet;


// https://link.springer.com/chapter/10.1007/978-3-540-25933-6_6
public class SolovayStrassem extends AbstractPrimalityTester {

    private final BigInteger THREE = BigInteger.valueOf(3);
    private final BigInteger FOUR = BigInteger.valueOf(4);
    private final BigInteger FIVE = BigInteger.valueOf(5);
    private final BigInteger EIGHT = BigInteger.valueOf(8);

    @Override
    protected Boolean isProbablyPrime(BigInteger possiblePrime, int iterations) {
        
        for(int i = 0; i < iterations; i++) {
            BigInteger a;
            do {
                a = new BigInteger(possiblePrime.bitLength(), random);
            } while((a.compareTo(BigInteger.TWO) < 0 || a.compareTo(possiblePrime.subtract(BigInteger.ONE)) > 0));

            BigInteger jacobi = (possiblePrime.add(calculateJacobi(a, possiblePrime))).mod(possiblePrime);

            BigInteger mod = a.modPow(possiblePrime.subtract(BigInteger.ONE).divide(BigInteger.TWO), possiblePrime);

            if (jacobi.equals(BigInteger.ZERO) || !mod.equals(jacobi)) {
                return false;
            }
        }
        return true;
    }

    // TODO: documentar esse método
    public BigInteger calculateJacobi(BigInteger a, BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) <= 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("n deve ser um inteiro positivo ímpar.");
        }
        // System.out.println("Calculando Jacobiano para " + a + " / " + n);

        a = a.mod(n);
        BigInteger jacobi = BigInteger.ONE;

        while (!a.equals(BigInteger.ZERO)) {
            while (a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                a = a.divide(BigInteger.TWO);
                BigInteger nMod8 = n.mod(EIGHT);
                if (nMod8.equals(THREE) || nMod8.equals(FIVE)) {
                    jacobi = jacobi.negate();
                }
            }

            BigInteger temp = a;
            a = n;
            n = temp;

            // If both a and n ≡ 3 mod 4, flip sign
            if (a.mod(FOUR).equals(THREE) &&
                n.mod(FOUR).equals(THREE)) {
                jacobi = jacobi.negate();
            }

            a = a.mod(n);
        }

        return n.equals(BigInteger.ONE) ? jacobi : BigInteger.ZERO;
    }

}
