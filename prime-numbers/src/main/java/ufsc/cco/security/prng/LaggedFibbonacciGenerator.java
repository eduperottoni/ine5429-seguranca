package ufsc.cco.security.prng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Implementação do gerador de números pseudo-aleatórios Lagged Fibonacci.
 * 
 * O algoritmo Lagged Fibonacci é um gerador de números pseudo-aleatórios que
 * utiliza uma sequência de Fibonacci com um atraso (lag) para gerar novos números.
 * 
 * Este gerador é baseado em dois índices j e k, onde o próximo número gerado é
 * calculado como uma operação dos números nos índices j e k, aplicando uma operação
 * bitwise XOR. O tamanho do buffer é determinado pelo valor de k.
 */
public class LaggedFibbonacciGenerator implements PseudoNumberGenerator {

    private final BigInteger[] buffer;
    private final BigInteger m;
    private final Random random = new SecureRandom();
    private final int j;
    private final int k;
    private int currentIndex;
    private int bufferSize;

    /**
     * Construtor do gerador Lagged Fibonacci.
     * 
     * @param j         O índice j (deve ser menor que k).
     * @param k         O índice k.
     * @param bitLength O comprimento em bits dos números gerados.
     */
    public LaggedFibbonacciGenerator(int j, int k,  int bitLength) {
        if (j >= k) {
            throw new IllegalArgumentException("j must be less than k");
        }

        this.j = j;
        this.k = k;
        this.m = BigInteger.ONE.shiftLeft(bitLength);
        this.currentIndex = 0;
        
        // Inicializa o buffer com números aleatórios
        this.buffer = new BigInteger[k];
        for (int i = 0; i < k; i++) {
            buffer[i] = new BigInteger(bitLength, random);
        }
        this.bufferSize = buffer.length;
    }

    /**
     * Construtor do gerador Lagged Fibonacci com valores padrão de j e k.
     * 
     * @param bitLength O comprimento em bits dos números gerados.
     */
    public LaggedFibbonacciGenerator(int bitLength) {
        this(273, 607, bitLength);
    }

    /**
     * Gera o próximo número pseudo-aleatório usando o algoritmo Lagged Fibonacci.
     * A partir dos números nos índices j e k do buffer, aplica a operação definida
     * 
     * @return O próximo número pseudo-aleatório gerado.
     */
    @Override
    public BigInteger generate() {
        int indexJ = (currentIndex - j + bufferSize) % bufferSize;
        int indexK = (currentIndex - k + bufferSize) % bufferSize;

        BigInteger newRandom = applyOperation(buffer[indexJ], buffer[indexK]).mod(m);
        buffer[currentIndex] = newRandom;

        currentIndex = (currentIndex + 1) % bufferSize;

        return newRandom;
    }

    /**
     * Aplica a operação de Lagged Fibonacci entre os elementos nos índices j e k.
     * 
     * @param jIndexElement O elemento no índice j.
     * @param kIndexElement O elemento no índice k.
     * @return O resultado da operação aplicada aos elementos.
     */
    protected BigInteger applyOperation(BigInteger jIndexElement, BigInteger kIndexElement) {
        // Por padrão, aplica a operação de XOR
        // Essa classe pode ser estendida para implementar as outras operações (soma, multiplicação, etc.)
        return jIndexElement.xor(kIndexElement);
    }

}
