package be.heh.projetapphyb.util

import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

class HashMaker
{
    companion object
    {
        fun hashPswd(pswd: String): String
        {
            return MessageDigest.getInstance(pswd).digest("SHA-256".toByteArray(UTF_8)).toString()
        }
    }
}