package ufsc.cco.security.prng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Implementação do gerador de números pseudo-aleatórios Blum-Blum-Shub com paralelismo.
 * 
 * Nesse caso, o algoritmo é executado em múltiplas threads para aumentar a eficiência.
 * Cada thread calcula uma porção do número. O primeiro valor de cada thread é calculado usando a fórmula xi = x0^(2^i mod(a(M))) (mod M),
 * em que a(M) é o mmc de (p-1) e (q-1).
 */
public class ParallelBlumBlumShubGenerator extends BlumBlumShubGenerator {

    // Deixamos a(M) pré-calculado
    private BigInteger aM;

    public ParallelBlumBlumShubGenerator(int bitLength) {
        super(bitLength);
    }

    // Igual ao método da superclasse, mas com o valor de a(M) pré-calculado
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
        // Informações sobre as threads
        int availableCores = Runtime.getRuntime().availableProcessors();
        int bitsPerThread = bitLength / availableCores;
        int remainingBits = bitLength % availableCores;

        ExecutorService executor = Executors.newFixedThreadPool(availableCores);
        // Cada thread vai gerar uma parte do set abaixo
        BitSet sharedBitSet = new BitSet(bitLength);
        List<Future<Void>> futures = new ArrayList<>();
        
        // Feito usando paralelismo
        // Para usar paralelismo, usa-se xi = x0^(2^i mod(a(M))) (mod M)
        // Em que a(M) é o mmc de (p-1) e (q-1)
        for (int i = 0; i < availableCores; i++) {
            final int startBit = i * bitsPerThread;
            final int endBit = (i == availableCores - 1)
                ? (startBit + bitsPerThread + remainingBits)
                : (startBit + bitsPerThread);
    
            futures.add(executor.submit(() -> {
                // Cada thread computa seu X inicial usando xi = x0^(2^i mod(a(M))) (mod M)
                BigInteger exp = BigInteger.TWO.modPow(BigInteger.valueOf(startBit), aM);
                BigInteger x = seed.modPow(exp, m);
                // Depois, calcula os demais bits seguintes da sua parte (startBit até endBit)
                for (int j = startBit; j < endBit; j++) {
                    x = x.modPow(BigInteger.TWO, m); // x = x^2 mod m
                    if (x.testBit(0)) {
                        synchronized (sharedBitSet) {
                            sharedBitSet.set(j);
                        }
                    }
                }
                return null;
            }));
        }
    
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Erro ao executar Blum Blum Shub Paralelo");
            e.printStackTrace();
        }

        // Atualizamos a seed para que o próximo número gerado seja diferente
        BigInteger exp = BigInteger.TWO.modPow(BigInteger.valueOf(bitLength), aM);
        seed = seed.modPow(exp, m);

        return new BigInteger(sharedBitSet.toByteArray());
    }
    
}
