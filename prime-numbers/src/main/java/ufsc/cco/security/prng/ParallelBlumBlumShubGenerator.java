package ufsc.cco.security.prng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class ParallelBlumBlumShubGenerator extends BlumBlumShubGenerator {

    private BigInteger aM;

    public ParallelBlumBlumShubGenerator(int bitLength) {
        super(bitLength);
    }

    @Override
    protected BigInteger getM(int bitLength, Random random) {
        BigInteger p = getPrimeFactorForM(bitLength / 2, random);
        BigInteger q = getPrimeFactorForM(bitLength / 2, random);
    
        while (p.equals(q)) {
            q = getPrimeFactorForM(bitLength / 2, random);
        }
    
        // Computa a(M) = mmc((p-1), (q-1))
        aM = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
        
        return p.multiply(q);
    }

    @Override
    public BigInteger generate() {
        int availableCores = Runtime.getRuntime().availableProcessors();
        int bitsPerThread = bitLength / availableCores;
        int remainingBits = bitLength % availableCores;
        BigInteger seed = generateSeed();

        ExecutorService executor = Executors.newFixedThreadPool(availableCores);
        List<Future<BitSet>> futures = new ArrayList<>();
        BigInteger currentX = seed.mod(m);
        
        // Feito usando paralelismo
        // Para usar paralelismo, usa-se xi = x0^(2^i mod(a(M))) (mod M)
        // Em que a(M) é o mmc de (p-1) e (q-1)
        for (int i = 0; i < availableCores; i++) {
            final int startBit = i * bitsPerThread;
            final int endBit = (i == availableCores - 1) ? (startBit + bitsPerThread + remainingBits) : (startBit + bitsPerThread);
            final BigInteger startX = currentX;

            futures.add(executor.submit(() -> {
                BitSet bitSet = new BitSet(bitLength);
                BigInteger x = startX;
                for (int j = startBit; j < endBit; j++) {
                    x = x.modPow(BigInteger.TWO, m);
                    if (x.testBit(0)) {
                        bitSet.set(j);
                    }
                }
                return bitSet;
            }));

            // Update currentX = x^(2^(bitsPerThread)) mod M for next thread start
            BigInteger powExp = BigInteger.TWO.pow(bitsPerThread);
            currentX = currentX.modPow(powExp, m);
        }

        executor.shutdown();

        // Combine results
        BitSet finalResult = new BitSet(bitLength);
        for (Future<BitSet> future : futures) {
            try {
                finalResult.or(future.get());
            } catch (Exception e) {
                throw new RuntimeException("Error in parallel bit generation", e);
            }
        }

        return new BigInteger(finalResult.toByteArray());
    }
    
}
