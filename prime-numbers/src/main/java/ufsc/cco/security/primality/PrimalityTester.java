package ufsc.cco.security.primality;

import java.math.BigInteger;

public interface PrimalityTester {

    public Boolean test(BigInteger n);
}
