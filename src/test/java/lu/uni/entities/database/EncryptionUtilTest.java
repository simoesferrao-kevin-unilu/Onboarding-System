package lu.uni.entities.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    @Test
    public void testEncryptionAndDecryption() throws Exception {
        String original = "123456789";
        String encrypted = EncryptionUtil.encrypt(original);
        String decrypted = EncryptionUtil.decrypt(encrypted);

        assertNotNull(encrypted, "Encrypted value should not be null");
        assertEquals(original, decrypted, "Decrypted value should match the original");
    }

    @Test
    public void testEncryptionUniqueness() throws Exception {
        String original1 = "123456789";
        String original2 = "987654321";

        String encrypted1 = EncryptionUtil.encrypt(original1);
        String encrypted2 = EncryptionUtil.encrypt(original2);

        assertNotEquals(encrypted1, encrypted2, "Encryption should produce different results for different inputs");
    }
}