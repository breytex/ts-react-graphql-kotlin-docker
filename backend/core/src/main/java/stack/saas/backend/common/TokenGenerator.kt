package stack.saas.backend.common

import io.vertx.ext.auth.PRNG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenGenerator @Inject constructor(val prng: PRNG) {
    companion object {
        private val HEX = "0123456789abcdef".toCharArray()
    }

    fun generateToken(): String {
        return generateToken(32)
    }

    fun generateToken(length: Int): String {
        return genToken(length)
    }

    private fun genToken(length: Int): String {
        val bytes = ByteArray(length)
        prng.nextBytes(bytes)

        val hex = CharArray(length * 2)
        for (j in 0 until length) {
            val v = bytes[j].toInt() and 0xFF
            hex[j * 2] = HEX[v.ushr(4)]
            hex[j * 2 + 1] = HEX[v and 0x0F]
        }

        return String(hex)
    }

}