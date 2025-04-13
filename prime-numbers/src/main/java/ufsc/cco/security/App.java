package ufsc.cco.security;

import ufsc.cco.security.prng.LaggedFibbonacciGenerator;
import ufsc.cco.security.prng.BlumBlumShubGenerator;

import java.math.BigInteger;

import ufsc.cco.security.primality.MillerRabin;


public class App {
    public static void main(String[] args) {
        // System.out.println("Vamos começar os experimentos!");
        // BlumBlumShubGenerator blumBlumShubGenerator = new BlumBlumShubGenerator(4096);
        
        // Long startTime = System.currentTimeMillis();
        // for (int i = 0; i < 100; i++) {
        //     System.out.println(blumBlumShubGenerator.generate());
        //     System.out.println("------------------");
        // }
        // Long endTime = System.currentTimeMillis();
        // System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
        // System.out.println("Média de tempo para gerar um número: " + (endTime - startTime) / 100 + "ms");
        // System.out.println("Hello World!");

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

        MillerRabin millerRabin = new MillerRabin();
        System.out.println(millerRabin.test(BigInteger.valueOf(561), 4));

        System.out.println("Hello World!");
    }
}
