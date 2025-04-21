package ufsc.cco.security;

import ufsc.cco.security.prng.LaggedFibbonacciGenerator;
import ufsc.cco.security.prng.ParallelBlumBlumShubGenerator;
import ufsc.cco.security.prng.BlumBlumShubGenerator;
import ufsc.cco.security.prng.PseudoNumberGenerator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JSpinner.NumberEditor;

import ufsc.cco.security.primality.MillerRabin;
import ufsc.cco.security.primality.PrimalityTester;
import ufsc.cco.security.primality.SolovayStrassem;


public class App {

    // TODO: Implementar exponenciação modular:
    // Não precisa implementar, apenas referenciar a documentação, que tem um método já otimizado para fazer exponenciação modular
    public static void main(String[] args) {
        // System.out.println("Vamos começar os experimentos!");
        List<Integer> numbers = Arrays.asList(40, 56, 80, 128, 168, 224, 256, 512, 1024, 2048, 4096);
        int NUMBERS_TO_GENERATE = 1000;
        
        
        // PRNG TESTING
        PrngTester.generateRandomNumbers(BlumBlumShubGenerator.class, NUMBERS_TO_GENERATE, numbers, false);
        // PrngTester.generateRandomNumbers(LaggedFibbonacciGenerator.class, NUMBERS_TO_GENERATE, numbers, false);
        PrngTester.generateRandomNumbers(ParallelBlumBlumShubGenerator.class, NUMBERS_TO_GENERATE, numbers, false);

        // System.out.println("Vamos começar os experimentos!");
        // LaggedFibbonacciGenerator laggedFiboGenerator = new LaggedFibbonacciGenerator(4096);
        
        // Long startTime = System.currentTimeMillis();
        // for (int i = 0; i < 100; i++) {
        //     System.out.println(laggedFiboGenerator.generate());
        //     System.out.println("------------------");
        // }
        // Long endTime = System.currentTimeMillis();
        // System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
        // System.out.println("Média de tempo para gerar um número: " + (endTime - startTime) / 100 + "ms");

        // MillerRabin millerRabin = new MillerRabin();
        // SolovayStrassem solovay = new SolovayStrassem();



        // PRIMALITY TESTING
        // Map<Integer, Long> map = new HashMap<>();
        // for (int i : numbers) {
        //     map.put(i, 0L);
        // }
        // for (int times = 0; times < 5; times++) {
        //     System.out.println("TESTE NÚMERO " + times);
        //     for (int i : numbers) {
        //         Boolean isPrime = false;
        //         BigInteger prime = null;
        //         PrimalityTester tester = new MillerRabin();
        //         PseudoNumberGenerator generator = new BlumBlumShubGenerator(i);

        //         Long startTime = System.currentTimeMillis();
        //         while(!isPrime) {
        //             prime = generator.generate();
        //             // isPrime = solovay.test(prime, 4);
        //             isPrime = tester.test(prime, 4);
        //         }
        //         Long endTime = System.currentTimeMillis();
        //         System.out.println("Encontramos um primo com "+ i + " bits : " + prime + " | " + (endTime - startTime) + " ms");
        //         long newAvg = map.get(i) + (endTime - startTime);
        //         map.put(i, newAvg);
        //     }
        // }
        // for (int i : numbers) {
        //     Long avg = map.get(i);
        //     System.out.println("Média de tempo para encontrar um número primo de " + i + " bits : " + avg / 5L + " ms");
        // }

        // System.out.println(millerRabin.test(laggedFiboGenerator.generate(), 4));
    }
}

