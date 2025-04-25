package ufsc.cco.security.prng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class LaggedFibbonacciGenerator implements PseudoNumberGenerator {

    private final BigInteger[] buffer;
    private final BigInteger m;
    private final Random random = new SecureRandom();
    private final int j;
    private final int k;
    private int currentIndex;
    private int bufferSize;


    public LaggedFibbonacciGenerator(int j, int k,  int bitLength) {
        if (j >= k) {
            throw new IllegalArgumentException("j must be less than k");
        }

        this.j = j;
        this.k = k;
        this.m = BigInteger.ONE.shiftLeft(bitLength);
        this.currentIndex = 0;
        
        // Initialize the buffer with random values
        this.buffer = new BigInteger[k];
        for (int i = 0; i < k; i++) {
            buffer[i] = new BigInteger(bitLength, random);
        }
        this.bufferSize = buffer.length;
    }

    public LaggedFibbonacciGenerator(int bitLength) {
        this(273, 607, bitLength);
    }

    @Override
    public BigInteger generate() {
        int indexJ = (currentIndex - j + bufferSize) % bufferSize;
        int indexK = (currentIndex - k + bufferSize) % bufferSize;

        BigInteger newRandom = applyOperation(buffer[indexJ], buffer[indexK]).mod(m);
        buffer[currentIndex] = newRandom;

        currentIndex = (currentIndex + 1) % bufferSize;

        return newRandom;
    }

    protected BigInteger applyOperation(BigInteger jIndexElement, BigInteger kIndexElement) {
        // By default apply xor operation
        // You can extend this class to override this operation
        return jIndexElement.xor(kIndexElement);
    }

}
