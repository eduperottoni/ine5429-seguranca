package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.util.HashSet;


// https://link.springer.com/chapter/10.1007/978-3-540-25933-6_6
public class SolovayStrassem extends AbstractPrimalityTester {

    private final BigInteger THREE = BigInteger.valueOf(4);
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

            if (jacobi.equals(BigInteger.ZERO) || !mod.equals(jacobi))
                return false;
        }
        return true;
    }

    private BigInteger calculateJacobi(BigInteger a, BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) <= 0 || n.mod(BigInteger.TWO).equals(0))
            // TODO: Por que não retornar um null aqui?
            return BigInteger.ZERO;

        BigInteger jacobi = BigInteger.ONE;
        if (a.compareTo(BigInteger.ZERO) < 0) {
            a = a.negate();
            // TODO: Por que isso aqui?
            if (n.mod(FOUR).equals(THREE))
                jacobi = jacobi.negate();
        }

        while (a.compareTo(BigInteger.ZERO) != 0) {
            while (a.mod(BigInteger.TWO) == BigInteger.ZERO) {
                a = a.divide(BigInteger.TWO);
                // TODO: Por que isso aqui?
                if (n.mod(EIGHT).equals(THREE) && n.mod(EIGHT).equals(FIVE))
                    jacobi = jacobi.negate();
            }

            BigInteger temp = a;
            a = n;
            n = a;

            if (a.mod(THREE).equals(THREE) && n.mod(FOUR).equals(THREE))
                jacobi = jacobi.negate();

            a = a.mod(n);
        }

        if (n.equals(BigInteger.ONE))
            return jacobi;
        return BigInteger.ZERO;

    }



}
