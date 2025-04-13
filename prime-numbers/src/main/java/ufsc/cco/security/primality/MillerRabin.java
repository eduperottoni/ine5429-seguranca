package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;


// Usado no método BigInteger#passesMillerRabin
public class MillerRabin extends AbstractPrimalityTester {

    private static final SecureRandom random = new SecureRandom();

    public MillerRabin() {
        // empty
    }

    @Override
    protected Boolean isProbablyPrime(BigInteger possiblePrime, int iterations) {
        // 1 - Escreva n - 1 = 2^k*m
        FactoringResult result = factorizeByTwoPower(possiblePrime.subtract(BigInteger.ONE));
        System.out.println("Fatoração de n - 1: k = " + result.k + ", m = " + result.m);

        HashSet<BigInteger> testedAs = new HashSet<>();
        for (int it = 0; it < iterations; it++) {
            // 2 - Selecione número aleatório a; 2 <= a <= n - 2
            BigInteger a;
            do {
                a = new BigInteger(possiblePrime.bitLength(), random);
            } while((a.compareTo(BigInteger.TWO) < 0 || a.compareTo(possiblePrime.subtract(BigInteger.TWO)) > 0) || testedAs.contains(a));
            
            testedAs.add(a);
            System.out.println("Testando com a = " + a);
            // a^m ≡ 1 (mod n) ?
            BigInteger aPowMModN = a.modPow(result.m, possiblePrime);
            if (aPowMModN.equals(BigInteger.ONE) || aPowMModN.equals(possiblePrime.subtract(BigInteger.ONE)))
                continue;
            
            Boolean continueTesting = false;
            for (int i = 0; i < result.k - 1; i++) {
                // a^(2^i*m) = -1 (mod n) ?
                // Testamos antes porque já temos o resultado de a^(2^i*m) mod n
                if (aPowMModN.equals(possiblePrime.subtract(BigInteger.ONE))) {
                    continueTesting = true;
                    break;
                }
                aPowMModN = aPowMModN.modPow(BigInteger.TWO, possiblePrime);
            }

            if (continueTesting) continue;

            System.out.println("FALHOU! a^m mod n = " + aPowMModN);
            return false;
        }

        return true;
    }


    private FactoringResult factorizeByTwoPower(BigInteger n) {
        BigInteger m = n;
        int k = 0;

        while(m.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            m = m.divide(BigInteger.TWO);
            k++;
        }
        
        return new FactoringResult(k, m);
    }

    private class FactoringResult {
        public int k;
        public BigInteger m;

        FactoringResult(int k, BigInteger m) {
            this.k = k;
            this.m = m;
        }
    }
    

}
