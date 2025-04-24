package org.cryptoapi.bench.predictablecryptographickey;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class PredictableCryptographicKeyABMC1 {
    public void go(String key) {
        byte[] keyBytes = key.getBytes();
        keyBytes = Arrays.copyOf(keyBytes,16);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    }
}


public class PredictableCryptographicKeyABMCCase1 {
    public static void main(String [] args){
        PredictableCryptographicKeyABMC1 pc = new PredictableCryptographicKeyABMC1();
        String key = "defaultkey";
        pc.go(key);
    }
}
