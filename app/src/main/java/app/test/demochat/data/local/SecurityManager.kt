package app.test.demochat.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Менеджер для работы с Android Keystore.
 * Демонстрирует навык защиты секретов (токенов) с использованием аппаратного шифрования.
 */
class SecurityManager(private val context: Context) {

    private val provider = "AndroidKeyStore"
    private val alias = "DemoChatSecretKey"
    private val transformation = "AES/GCM/NoPadding"

    init {
        generateKeyIfNeeded()
    }

    private fun generateKeyIfNeeded() {
        val keyStore = KeyStore.getInstance(provider).apply { load(null) }
        if (!keyStore.containsAlias(alias)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, provider)
            val spec = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
            Log.d("SecurityManager", "Новый ключ успешно создан в Keystore")
        }
    }

    /**
     * Шифрует строку. Безопасно логирует только факт операции, не данные.
     */
    fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            
            // Сохраняем IV вместе с данными для последующей расшифровки
            val combined = iv + encryptedBytes
            Base64.encodeToString(combined, Base64.DEFAULT).also {
                Log.i("SecurityManager", "Данные зашифрованы успешно")
            }
        } catch (e: Exception) {
            Log.e("SecurityManager", "Ошибка при шифровании", e)
            ""
        }
    }

    /**
     * Расшифровывает строку.
     */
    fun decrypt(encryptedData: String): String {
        if (encryptedData.isEmpty()) return ""
        return try {
            val combined = Base64.decode(encryptedData, Base64.DEFAULT)
            val iv = combined.sliceArray(0 until 12) // GCM IV обычно 12 байт
            val encryptedBytes = combined.sliceArray(12 until combined.size)

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            
            String(cipher.doFinal(encryptedBytes)).also {
                Log.i("SecurityManager", "Данные расшифрованы успешно")
            }
        } catch (e: Exception) {
            Log.e("SecurityManager", "Ошибка при расшифровке", e)
            ""
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(provider).apply { load(null) }
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}