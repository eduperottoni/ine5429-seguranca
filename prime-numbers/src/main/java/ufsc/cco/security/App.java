package ufsc.cco.security;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Vamos começar os experimentos!");
        BlumBlumShubGenerator blumBlumShubGenerator = new BlumBlumShubGenerator(4096);
        
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            System.out.println(blumBlumShubGenerator.generate());
            System.out.println("------------------");
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
        System.out.println("Média de tempo para gerar um número: " + (endTime - startTime) / 100 + "ms");
        System.out.println("Hello World!");
    }
}
