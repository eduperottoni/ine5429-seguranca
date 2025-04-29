package ufsc.cco.security.tests;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ufsc.cco.security.primality.PrimalityTester;
import ufsc.cco.security.prng.PseudoNumberGenerator;

public class PtTester {

    public static <T extends PseudoNumberGenerator, P extends PrimalityTester> void findPrimeNumbers(Class<T> pseudoNumberGenClazz, Class<P> primalityTesterClazz , int numbersToGenerate, List<Integer> bitLenghts, int iterations, boolean debug) {
    
        Map<Integer, Long> map = new HashMap<>();
        for (int i : bitLenghts) {
            map.put(i, 0L);
        }
        for (int times = 0; times < numbersToGenerate; times++) {
            System.out.println("TESTE NÚMERO " + times);
            for (int i : bitLenghts) {
                Boolean isPrime = false;
                BigInteger prime = null;
                PseudoNumberGenerator generator;
                PrimalityTester tester;
                try {
                    generator = pseudoNumberGenClazz.getDeclaredConstructor(int.class).newInstance(i);
                    tester = primalityTesterClazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Long startTime = System.currentTimeMillis();
                while(!isPrime) {
                    prime = generator.generate();
                    isPrime = tester.test(prime, iterations);
                }
                Long endTime = System.currentTimeMillis();
                System.out.println("Encontramos um primo com "+ i + " bits : " + prime + " | " + (endTime - startTime) + " ms");
                long newAvg = map.get(i) + (endTime - startTime);
                map.put(i, newAvg);
            }
        }
        for (int i : bitLenghts) {
            Long avg = map.get(i);
            System.out.println("Média de tempo para encontrar um número primo de " + i + " bits : " + avg / (double) numbersToGenerate + " ms");
        }
    }

    public static <P extends PrimalityTester> void testForConstant(Class<P> primalityTesterClazz , int timesToRun, Map<Integer, BigInteger> primeNumbers, int iterations, boolean debug) {
        Map<Integer, Long> map = new HashMap<>();
        for (int i : primeNumbers.keySet()) {
            map.put(i, 0L);
        }

        
        PrimalityTester tester;
        try {
            tester = primalityTesterClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (int times = 0; times < timesToRun; times++) {
            System.out.println("TESTE NÚMERO " + times);

            primeNumbers.forEach((i, prime) -> {;
                Long startTime = System.currentTimeMillis();
                
                Boolean isPrime = tester.test(primeNumbers.get(i), iterations);
                assert isPrime == true;
                Long endTime = System.currentTimeMillis();
                System.out.println("Teste para o primo com "+ i + " bits : " + prime + " | " + (endTime - startTime) + " ms");
                long newAvg = map.get(i) + (endTime - startTime);
                map.put(i, newAvg);
            });
        }

        for (int i : primeNumbers.keySet()) {
            Long avg = map.get(i);
            System.out.println("Média de tempo para testar um primo com " + i + " bits : " + avg / (double) timesToRun + " ms");
        }
    }
}
