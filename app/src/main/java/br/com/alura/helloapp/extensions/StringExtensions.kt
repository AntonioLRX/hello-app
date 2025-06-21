package br.com.alura.helloapp.extensions

import br.com.alura.helloapp.util.FORMATO_DATA_DIA_MES_ANO
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.converteParaDate(): Date? {
    return try {
        SimpleDateFormat(
            FORMATO_DATA_DIA_MES_ANO,
            Locale.getDefault()
        ).parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun String.toHash(
    algorithmType: String = "SHA-256"
): String {
    val md = MessageDigest.getInstance(algorithmType)
    return md.digest(this.toByteArray()).joinToString("") { "%02x".format(it) }
}