package ufsc.cco.security;

import ufsc.cco.security.prng.LaggedFibbonacciGenerator;
import ufsc.cco.security.prng.BlumBlumShubGenerator;

import java.math.BigInteger;

import ufsc.cco.security.primality.MillerRabin;
import ufsc.cco.security.primality.SolovayStrassem;


public class App {

    // TODO: Implementar exponenciação modular:
    // Não precisa implementar, apenas referenciar a documentação, que tem um método já otimizado para fazer exponenciação modular
    public static void main(String[] args) {
        // System.out.println("Vamos começar os experimentos!");
        BlumBlumShubGenerator blumBlumShubGenerator = new BlumBlumShubGenerator(4096);
        
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
        LaggedFibbonacciGenerator laggedFiboGenerator = new LaggedFibbonacciGenerator(4096);
        
        // Long startTime = System.currentTimeMillis();
        // for (int i = 0; i < 100; i++) {
        //     System.out.println(laggedFiboGenerator.generate());
        //     System.out.println("------------------");
        // }
        // Long endTime = System.currentTimeMillis();
        // System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
        // System.out.println("Média de tempo para gerar um número: " + (endTime - startTime) / 100 + "ms");

        MillerRabin millerRabin = new MillerRabin();
        SolovayStrassem solovay = new SolovayStrassem();

        Long startTime = System.currentTimeMillis();
        Boolean isPrime = false;
        BigInteger prime = null;
        while(!isPrime) {
            prime = laggedFiboGenerator.generate();
            // isPrime = solovay.test(prime, 4);
            isPrime = solovay.test(prime, 4);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("Encontramos um primo: " + prime);
        System.out.println("Tempo decorrido: " + (endTime - startTime));

        // System.out.println(millerRabin.test(laggedFiboGenerator.generate(), 4));
    }
}
