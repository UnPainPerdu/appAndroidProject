package be.heh.projetapphyb.util

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class HashMaker
{
    companion object
    {
        fun hashPswd(pswd: String): String
        {
            val md = MessageDigest.getInstance("SHA-256")

            md.update(pswd.toByteArray(StandardCharsets.UTF_8))
            val digest = md.digest()

            val number = BigInteger(1, digest)
            val hexHash = number.toString(16)
            return hexHash
        }
    }
}