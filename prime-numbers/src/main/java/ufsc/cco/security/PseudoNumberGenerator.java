package ufsc.cco.security;

import java.math.BigInteger;

public interface PseudoNumberGenerator {
    /*
     * Generates a pseudo-random number of the specified bit length.
     */
    public BigInteger generate();
}
