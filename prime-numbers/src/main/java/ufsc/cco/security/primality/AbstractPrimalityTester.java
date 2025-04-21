package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.security.SecureRandom;

public abstract class AbstractPrimalityTester implements PrimalityTester {

    protected static final SecureRandom random = new SecureRandom();

    private Boolean testBaseCases(BigInteger possiblePrime) {
        // Se for 2 ou 3 é primo
        if (possiblePrime.equals(BigInteger.TWO) || possiblePrime.equals(BigInteger.valueOf(3)))
            return true;
        // Se for menor que 2 ou par, não é primo
        if (possiblePrime.compareTo(BigInteger.TWO) < 0 || possiblePrime.mod(BigInteger.TWO).equals(BigInteger.ZERO))
            return false;

        return null;
    }

    public Boolean test(BigInteger n) {
        // System.out.println("Testando para o possível primo " + n);
        return test(n, 1);
    }

    public Boolean test(BigInteger n, int iterations) {
        // System.out.println("Testando para o possível primo " + n);
        Boolean baseCasesResult = testBaseCases(n);

        return baseCasesResult != null ? baseCasesResult : isProbablyPrime(n, iterations);
    }

    protected Boolean isProbablyPrime(BigInteger n, int iterations) {
        throw new UnsupportedOperationException("Algorithm to know if a prime");
    }
    
}
