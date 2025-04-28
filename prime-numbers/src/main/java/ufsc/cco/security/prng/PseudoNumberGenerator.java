package ufsc.cco.security.prng;

import java.math.BigInteger;

/**
 * Interface a ser implementada por geradores de números pseudo-aleatórios
 */
public interface PseudoNumberGenerator {
    /*
     * Gera o próximo número pseudo-aleatório
     */
    public BigInteger generate();
}
