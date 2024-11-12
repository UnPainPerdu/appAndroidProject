package be.heh.projetapphyb.util

import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

class HashMaker
{
    companion object
    {
        fun hashPswd(pswd: String): String
        {
            return MessageDigest.getInstance("SHA-256").digest(pswd.toByteArray(UTF_8)).toString()
        }
    }
}