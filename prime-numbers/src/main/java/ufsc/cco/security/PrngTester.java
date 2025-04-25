package ufsc.cco.security;

import java.math.BigInteger;
import java.util.List;

import ufsc.cco.security.prng.PseudoNumberGenerator;

public class PrngTester {

    public static <T extends PseudoNumberGenerator> void generateRandomNumbers(Class<T> clazz, int numbersToGenerate, List<Integer> bitLenghts, boolean debug) {
        System.out.println("Vamos começar os experimentos para o PRNG " + clazz.getSimpleName() + "!");
        String graphicCoords = "";
        int bitLenghtsCounter = 1;
        for (int i: bitLenghts) {
            PseudoNumberGenerator generator;
            try {
                generator = clazz.getDeclaredConstructor(int.class).newInstance(i);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
            System.out.println("Vamos começar os experimentos para " + i + " bits!");
            Long startTime = System.currentTimeMillis();
            for (int j = 0; j < numbersToGenerate; j++) {
                BigInteger number = generator.generate();
                if (debug) {
                    System.out.println("Número de " + i + " bits: " + number);
                    System.out.println("------------------");
                }
            }
            Long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + numbersToGenerate + " números de " + i + " bits : " + (endTime - startTime) + " ms");
            System.out.println("  Média : " + (endTime - startTime) / (double) numbersToGenerate + " ms");
            graphicCoords += "(" + bitLenghtsCounter++ + "," + (endTime - startTime) / (double) numbersToGenerate + ")" + "\n";
        }

        System.out.println(graphicCoords);
    }

}
