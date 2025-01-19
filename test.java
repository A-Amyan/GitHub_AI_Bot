import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.SecureRandom;
import java.util.Random;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;

public class SecurityMisusesExample {
    public static void main(String[] args) throws Exception {
        // 1. Hardcoded cryptographic key in SecretKeySpec
        String hardcodedKey = "1234567812345678"; // Insecure: Hardcoded key
        SecretKeySpec keySpec = new SecretKeySpec(hardcodedKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 11. Using ECB mode (insecure)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 2. Hardcoded password in PBEKeySpec
        char[] hardcodedPassword = "hardcodedPassword".toCharArray(); // Insecure: Hardcoded password
        byte[] salt = "12345678".getBytes(); // 10. Constant salt (insecure)
        int iterationCount = 500; // 13. Iteration count less than 1000 (insecure)
        PBEKeySpec pbeKeySpec = new PBEKeySpec(hardcodedPassword, salt, iterationCount);

        // 3. Hardcoded KeyStore password
        String keystorePassword = "hardcodedKeystorePassword"; // Insecure: Hardcoded password
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, keystorePassword.toCharArray());

        // 4. HostnameVerifier always returning true
        HostnameVerifier hostnameVerifier = (hostname, session) -> true; // Insecure: Accepts all hostnames
        URL url = new URL("http://example.com"); // 7. Using HTTP instead of HTTPS
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(hostnameVerifier);

        // 5. X509TrustManager with empty certificate validation
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
        };

        // 8. Using java.util.Random instead of SecureRandom
        Random insecureRandom = new Random();
        int randomValue = insecureRandom.nextInt();

        // 9. Using constant seed in SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(12345L); // Insecure: Constant seed

        // 16. Using broken hash function (SHA1)
        java.security.MessageDigest sha1Digest = java.security.MessageDigest.getInstance("SHA1"); // Insecure: SHA1 is broken
        sha1Digest.update("example".getBytes());
        byte[] hash = sha1Digest.digest();

        System.out.println("Insecure operations executed.");
    }
}
