package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.util.HashSet;


/**
 * Implementa o teste de primalidade de Miller-Rabin
 */
public class MillerRabin extends AbstractPrimalityTester {

    public MillerRabin() {
        // empty
    }

    @Override
    protected Boolean isProbablyPrime(BigInteger possiblePrime, int iterations) {
        // 1 - Escreva n - 1 = 2^k*m
        FactoringResult result = factorizeByTwoPower(possiblePrime.subtract(BigInteger.ONE));
        // System.out.println("Fatoração de n - 1: k = " + result.k + ", m = " + result.m);

        HashSet<BigInteger> testedAs = new HashSet<>();
        for (int it = 0; it < iterations; it++) {
            // 2 - Selecione número aleatório a; 2 <= a <= n - 2
            BigInteger a;
            do {
                a = new BigInteger(possiblePrime.bitLength(), random);
            } while((a.compareTo(BigInteger.TWO) < 0 || a.compareTo(possiblePrime.subtract(BigInteger.TWO)) > 0) || testedAs.contains(a));
            
            testedAs.add(a);
            // System.out.println("Testando com a = " + a);

            // 3 - a^m ≡ 1 (mod n) ?
            BigInteger aPowMModN = a.modPow(result.m, possiblePrime);
            if (aPowMModN.equals(BigInteger.ONE) || aPowMModN.equals(possiblePrime.subtract(BigInteger.ONE)))
                continue;
            
            Boolean continueTesting = false;
            for (int i = 0; i < result.k - 1; i++) {
                // 4 - a^(2^i*m) = -1 (mod n) ?
                // Testamos antes porque já temos o resultado de a^(2^i*m) mod n
                if (aPowMModN.equals(possiblePrime.subtract(BigInteger.ONE))) {
                    continueTesting = true;
                    break;
                }
                aPowMModN = aPowMModN.modPow(BigInteger.TWO, possiblePrime);
            }

            if (continueTesting) continue;  

            return false;
        }

        return true;
    }


    /**
     * Descobre k e m tais que n = 2^k*m, com n par e m ímpar
     * 
     * @param n número par
     * @return {@link FactoringResult}, descrevendo k e m em n = 2^k*m
     */
    private FactoringResult factorizeByTwoPower(BigInteger n) {
        BigInteger m = n;
        int k = 0;

        while(m.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            m = m.divide(BigInteger.TWO);
            k++;
        }
        
        return new FactoringResult(k, m);
    }

    /**
     * Classe auxiliar apenas para guardar os valores de k e m da fatoração
     */
    private class FactoringResult {
        public int k;
        public BigInteger m;

        FactoringResult(int k, BigInteger m) {
            this.k = k;
            this.m = m;
        }
    }
    

}
