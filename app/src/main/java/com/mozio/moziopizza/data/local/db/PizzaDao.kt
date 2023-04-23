package com.mozio.moziopizza.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mozio.moziopizza.data.local.models.HashEntity
import com.mozio.moziopizza.data.models.Pizza

@Dao
interface PizzaDao {

    /**
     * Inserts a list of pizzas into the database.
     * @param pizzas The list of pizzas to be inserted.
     */
    @Query("SELECT * FROM pizza")
    fun getAllPizzas(): List<Pizza>

    /**
     * Retrieves all pizzas from the database.
     * @return A list of pizzas wrapped.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPizzas(pizzas: List<Pizza>)

    /**
     * Deletes all pizzas from the database.
     */
    @Query("DELETE FROM pizza")
    suspend fun deleteAllPizzas()

    /**
     * Gets the save hash for the data we have stored
     */
    @Query("SELECT dataHash FROM hash WHERE id = 1")
    suspend fun getDataHash(): String

    /**
     * Update saved hash
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDataHash(hash: HashEntity)

}