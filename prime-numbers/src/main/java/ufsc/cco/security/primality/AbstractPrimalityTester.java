package ufsc.cco.security.primality;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Classe com métodos básicos de checagem e declarações de métodos necessários para um testador de primalidade
 */
public abstract class AbstractPrimalityTester implements PrimalityTester {

    protected static final SecureRandom random = new SecureRandom();

    /**
     * Testa os casos básicos de primalidade
     * @param possiblePrime número que está passando pelo teste
     * @return se é ou não primo e {@code null} caso o resultado ainda não é definitivo após os casos base
     */
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
        // Por padrão, 1 iteração
        return test(n, 1);
    }

    /**
     * Wrapper do teste específico para diferentes algoritmos.
     * @param n número a ser testado (possível primo)
     * @param iterations número de iterações a serem realizadas pelo teste (quanto maior, maior a confiabilidade do teste)
     * @return {@link Boolean} indicando se o teste retornou que o número é possível primo.
     */
    public Boolean test(BigInteger n, int iterations) {
        Boolean baseCasesResult = testBaseCases(n);

        return baseCasesResult != null ? baseCasesResult : isProbablyPrime(n, iterations);
    }

    /**
     * Aplica o algoritmo probabilístico específico para verificação de primalidade
     * @param n número a ser testado (possível primo)
     * @param iterations número de iterações a serem realizadas pelo teste (quanto maior, maior a confiabilidade do teste)
     * @return {@link Boolean} indicando se o teste retornou que o número é possível primo.
     */
    protected Boolean isProbablyPrime(BigInteger n, int iterations) {
        throw new UnsupportedOperationException("Algorithm to know if a prime");
    }
    
}
