package ufsc.cco.security;

import ufsc.cco.security.prng.LaggedFibbonacciGenerator;
import ufsc.cco.security.prng.ParallelBlumBlumShubGenerator;
import ufsc.cco.security.prng.BlumBlumShubGenerator;

import java.util.Arrays;
import java.util.List;

import ufsc.cco.security.primality.MillerRabin;
import ufsc.cco.security.primality.SolovayStrassem;


public class App {

    // TODO: Implementar exponenciação modular:
    // Não precisa implementar, apenas referenciar a documentação, que tem um método já otimizado para fazer exponenciação modular
    public static void main(String[] args) {
        // System.out.println("Vamos começar os experimentos!");
        List<Integer> bitLenghts = Arrays.asList(40, 56, 80, 128, 168, 224, 256, 512, 1024, 2048, 4096);
        int NUMBERS_TO_GENERATE = 1000;
        
        
        // PRNG TESTING
        // PrngTester.generateRandomNumbers(BlumBlumShubGenerator.class, NUMBERS_TO_GENERATE, bitLenghts, false);
        // PrngTester.generateRandomNumbers(ParallelBlumBlumShubGenerator.class, NUMBERS_TO_GENERATE, bitLenghts, false);
        // PrngTester.generateRandomNumbers(LaggedFibbonacciGenerator.class, NUMBERS_TO_GENERATE, bitLenghts, true);


        // PRIMALITY TESTING
        PtTester.findPrimeNumbers(BlumBlumShubGenerator.class, MillerRabin.class, 5, bitLenghts, 10, false);
        // PtTester.findPrimeNumbers(LaggedFibbonacciGenerator.class, SolovayStrassem.class, 5, bitLenghts, false);
        // PtTester.findPrimeNumbers(BlumBlumShubGenerator.class, MillerRabin.class, 5, bitLenghts, false);
        // PtTester.findPrimeNumbers(BlumBlumShubGenerator.class, SolovayStrassem.class, 5, bitLenghts, false);
        // PtTester.findPrimeNumbers(ParallelBlumBlumShubGenerator.class, SolovayStrassem.class, 5, bitLenghts, false);
        // PtTester.findPrimeNumbers(ParallelBlumBlumShubGenerator.class, MillerRabin.class, 5, bitLenghts, false);

    }
}

