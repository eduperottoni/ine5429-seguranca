package ufsc.cco.security.prng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.Random;

/**
 * Implementação do gerador de números pseudo-aleatórios Blum-Blum-Shub (BBS).
 * 
 * O algoritmo BBS é baseado em propriedades de números primos e é considerado
 * criptograficamente seguro, desde que os parâmetros sejam escolhidos corretamente.
 * 
 * Este gerador utiliza dois números primos p e q, ambos da forma 4k + 3, para calcular
 * o módulo M = p * q. O valor inicial (seed) deve ser coprimo com M para garantir
 * a segurança do gerador.
 */
public class BlumBlumShubGenerator implements PseudoNumberGenerator {
    
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger FOUR = BigInteger.valueOf(4);

    // M = p * q, com p e q primos de forma 4k + 3 (p === q === 3 mod 4)
    protected BigInteger m;
    protected Random random;
    // O tamanho do número gerado
    protected int bitLength;
    // Raiz X0 (o valor de X0 deve ser coprimo com M)
    protected BigInteger seed;

     /**
     * Gera um número primo da forma 4k + 3 com o comprimento especificado.
     * 
     * @param bitLength O comprimento em bits do número primo a ser gerado.
     * @param random    Instância de gerador de números aleatórios.
     * @return Um número primo da forma 4k + 3.
     */
    protected BigInteger getPrimeFactorForM(int bitLength, Random random) {
        BigInteger prime;
        do {
            prime = new BigInteger(bitLength, 100, random);
        } while (!prime.mod(FOUR).equals(THREE));
        return prime;
    }

    /**
     * Calcula o valor de M = p * q, onde p e q são primos da forma 4k + 3.
     * 
     * @param bitLength O comprimento em bits de M.
     * @param random    Instância de gerador de números aleatórios.
     * @return O valor de M.
     */
    protected BigInteger getM(int bitLength, Random random) {
        BigInteger p = getPrimeFactorForM(bitLength / 2, random);
        BigInteger q = getPrimeFactorForM(bitLength / 2, random);

        // Garante que p e q sejam diferentes
        while (p.equals(q)) {
            q = getPrimeFactorForM(bitLength / 2, random);
        }
        
        return p.multiply(q);
    }

    /**
     * Construtor da classe
     * 
     * @param bitLength O comprimento em bits do número gerado.
     */
    public BlumBlumShubGenerator(int bitLength) {
        this.bitLength = bitLength;
        this.random = new SecureRandom();
        this.m = getM(bitLength, random);
        this.seed = generateSeed().mod(m);
    }

    /**
     * Gera um valor inicial (seed) que seja coprimo com M.
     * 
     * @return Um valor inicial (seed) válido.
     */
    public BigInteger generateSeed() {
        BigInteger seed = new BigInteger(bitLength, random);
        // Garantindo que seed não compartilhe fatores primos com M
        // Isso garante que nenhuma informação sobre M seja revelada
        while (!seed.gcd(m).equals(BigInteger.ONE)) {
            seed = new BigInteger(bitLength, random);
        }
        return seed;
    }


    @Override
    public BigInteger generate() {
        BitSet result = new BitSet(bitLength);

        // Feito sem usar paralelismo
        // Cada bit  do número final é calculado por vez
        BigInteger currentX = seed;
        for (int i = 0; i < bitLength; i++) {

            // Calcula X(i) = X(i-1)^2 mod M
            currentX = currentX.modPow(BigInteger.TWO, m);
            
            // Usa o LSB de X(i) para determinar o bit i do número gerado
            if (currentX.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result.set(i);
            } else {
                result.clear(i);
            }
        }
        seed = currentX;
        
        return new BigInteger(result.toByteArray());
    }

}
