package ufsc.cco.security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.Random;

public class BlumBlumShubGenerator implements PseudoNumberGenerator {
    
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger FOUR = BigInteger.valueOf(4);

    private BigInteger m;
    private Random random;
    private int bitLength;

    private BigInteger getPrimeFactorForM(int bitLength, Random random) {
        BigInteger prime;
        do {
            prime = new BigInteger(bitLength, 100, random);
        } while (prime.mod(FOUR).equals(THREE));
        return prime;
    }

    private BigInteger getM(int bitLength, Random random) {
        BigInteger p = getPrimeFactorForM(bitLength / 2, random);
        BigInteger q = getPrimeFactorForM(bitLength / 2, random);

        while (p.equals(q)) {
            q = getPrimeFactorForM(bitLength / 2, random);
        }

        return p.multiply(q);
    }

    public BlumBlumShubGenerator(int bitLength) {
        this.bitLength = bitLength;
        this.random = new SecureRandom();
        this.m = getM(bitLength, random);
    }

    @Override
    public BigInteger generate() {
        BitSet result = new BitSet(bitLength);

        // TODO: Gerar uma seed para cada execução é o correto? Sim, porque se usar a mesma, os primeiros números serão sempre iguais
        BigInteger seed = new BigInteger(bitLength, random);
        // Garantindo que seed não compartilhe fatores primos com M
        while (seed.mod(m).equals(BigInteger.ZERO)) {
            seed = new BigInteger(bitLength, random);
        }

        // Feito sem usar paralelismo
        // Para usar paralelismo, usa-se xi = x0^(2^i mod(a(M))) (mod M)
        // Em que a(M) é o mmc de (p-1) e (q-1)
        BigInteger currentX = seed.mod(m);
        for (int i = 0; i < bitLength; i++) {
            currentX = currentX.modPow(BigInteger.TWO, m);

            if (currentX.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result.set(i);
            } else {
                result.clear(i);
            }
        }

        return new BigInteger(result.toByteArray());
    }

}
