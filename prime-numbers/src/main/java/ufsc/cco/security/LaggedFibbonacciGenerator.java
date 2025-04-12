package ufsc.cco.security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class LaggedFibbonacciGenerator implements PseudoNumberGenerator {

    private final BigInteger[] buffer;
    private final BigInteger m;
    private final Random random = new SecureRandom();
    private final int j;
    private final int k;
    private int jIndexForNavigation;


    public LaggedFibbonacciGenerator(int j, int k,  int bitLength) {
        if (j >= k) {
            throw new IllegalArgumentException("j must be less than k");
        }

        this.j = j;
        this.k = k;
        this.m = BigInteger.ONE.shiftLeft(bitLength);
        this.jIndexForNavigation = j - 1;
        
        // Initialize the buffer with random values
        this.buffer = new BigInteger[k];
        for (int i = 0; i < k; i++) {
            buffer[i] = new BigInteger(bitLength, random);
        }
    }

    public LaggedFibbonacciGenerator(int bitLength) {
        this(274, 607, bitLength);
    }

    @Override
    public BigInteger generate() {
        int kIndexForNavigation = (jIndexForNavigation + k - j) % k;

        BigInteger newRandom = applyOperation(buffer[jIndexForNavigation], buffer[kIndexForNavigation]).mod(m);
        buffer[kIndexForNavigation] = newRandom;

        jIndexForNavigation = (jIndexForNavigation + 1) % k;

        return newRandom;
    }

    protected BigInteger applyOperation(BigInteger jIndexElement, BigInteger kIndexElement) {
        // By default apply xor operation
        // You can extend this class to override this operation
        return jIndexElement.xor(kIndexElement);
    }

}
