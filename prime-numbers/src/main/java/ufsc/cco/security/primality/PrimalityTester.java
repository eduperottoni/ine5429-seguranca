package ufsc.cco.security.primality;

import java.math.BigInteger;

/**
 * Interface a ser implementada por testadores de primalidade
 */
public interface PrimalityTester {

    /**
     * Testa se o número é primo
     * @param n possível primo
     * @return true caso o número seja possivelmente primo, false caso contrário
     */
    public Boolean test(BigInteger n);

    // Mesma coisa que o método acima, mas com o número de iterações definido pela chamada
    public Boolean test(BigInteger n, int iterations);
}
