package ufsc.cco.security.primality;

import java.math.BigInteger;

/**
 * Implementa o teste de primalidade de Solovay-Strassen
 * 
 * Esse algoritmo é um teste probabilístico de primalidade que utiliza o símbolo de Jacobi e a exponenciação modular.
 * Ele é baseado na ideia de que, se n é um número primo ímpar e a é um inteiro tal que 1 < a < n, então:
 * a^(n-1)/2 ≡ (a/n) (mod n), onde (a/n) é o símbolo de Jacobi.
 * Se o número não satisfaz essa condição, ele é definitivamente composto.
 * Se o número satisfaz essa condição, ele é provavelmente primo.
 */
public class SolovayStrassem extends AbstractPrimalityTester {

    private final BigInteger THREE = BigInteger.valueOf(3);
    private final BigInteger FOUR = BigInteger.valueOf(4);
    private final BigInteger FIVE = BigInteger.valueOf(5);
    private final BigInteger EIGHT = BigInteger.valueOf(8);

    @Override
    protected Boolean isProbablyPrime(BigInteger possiblePrime, int iterations) {
        
        for(int i = 0; i < iterations; i++) {
            // 1 - Selecione número aleatório a; 2 <= a <= n - 2
            BigInteger a;
            do {
                a = new BigInteger(possiblePrime.bitLength(), random);
            } while(a.compareTo(BigInteger.TWO) < 0 || a.compareTo(possiblePrime.subtract(BigInteger.TWO)) > 0);
            // 2 - Calcule o símbolo de Jacobi (a/n)
            // Corrigimos o símbolo caso ele seja -1 para a comparação
            BigInteger jacobi = (possiblePrime.add(calculateJacobi(a, possiblePrime))).mod(possiblePrime);
            // 3 - Calcule a^(n-1)/2 (mod n) - Critério de Euler
            BigInteger euler = a.modPow(possiblePrime.subtract(BigInteger.ONE).divide(BigInteger.TWO), possiblePrime);
            // 4 - Se o símbolo de Jacobi for 0 ou diferente do resultado de Euler, não é primo
            if (jacobi.equals(BigInteger.ZERO) || !euler.equals(jacobi)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calcula o símbolo de Jacobi (a/n)
     * @param a Número entre 1 e n-1
     * @param n Número possível primo 
     * @return
     */
    public BigInteger calculateJacobi(BigInteger a, BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) <= 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("n deve ser um inteiro positivo ímpar.");
        }
        // System.out.println("Calculando Jacobi para " + a + " / " + n);

        // Se a for maior que n, reduzimos a
        a = a.mod(n);
        BigInteger jacobi = BigInteger.ONE;

        // Se a for 0, jacobi é 0
        while (!a.equals(BigInteger.ZERO)) {
            // Se a for divisível por dois, reduzimos a
            while (a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                a = a.divide(BigInteger.TWO);
                BigInteger nMod8 = n.mod(EIGHT);
                // Se n ≡ 1 mod 8 ou n ≡ 7 mod 8, jacobi é positivo
                // Se n ≡ 3 mod 8 ou n ≡ 5 mod 8, jacobi é negativo
                if (nMod8.equals(THREE) || nMod8.equals(FIVE)) {
                    jacobi = jacobi.negate();
                }
            }
            
            // Aplicamos a lei da reciprociidade quadrática
            BigInteger temp = a;
            a = n;
            n = temp;

            // Se a for 1 ou -1, jacobi é positivo
            // Se a for 3 ou -3, jacobi é negativo
            if (a.mod(FOUR).equals(THREE) &&
                n.mod(FOUR).equals(THREE)) {
                jacobi = jacobi.negate();
            }

            a = a.mod(n);
        }

        // Se n for 1, jacobi foi calculado
        // Se n for 0, jacobi é 0
        return n.equals(BigInteger.ONE) ? jacobi : BigInteger.ZERO;
    }

}
