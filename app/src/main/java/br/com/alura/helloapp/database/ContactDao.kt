package br.com.alura.helloapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.alura.helloapp.data.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun searchAll(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :id")
    fun searchById(id: Long): Flow<Contact?>

    @Query("DELETE FROM Contact WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM Contact WHERE name LIKE '%' || :name || '%'")
    fun searchByName(name: String): Flow<List<Contact>>
}