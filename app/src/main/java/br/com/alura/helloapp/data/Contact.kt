package br.com.alura.helloapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val lastName: String = "",
    val phone: String = "",
    val photo: String = "",
    val birthday: Date? = null,
)