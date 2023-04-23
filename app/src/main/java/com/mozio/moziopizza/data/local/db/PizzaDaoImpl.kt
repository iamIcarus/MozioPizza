package com.mozio.moziopizza.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mozio.moziopizza.data.local.models.HashEntity
import com.mozio.moziopizza.data.models.Pizza

@Database(entities = [Pizza::class,HashEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pizzaDao(): PizzaDao
}

class PizzaDaoImpl(database: AppDatabase) : PizzaDao {

    private val pizzaDao = database.pizzaDao()

    override fun getAllPizzas(): List<Pizza> {
        return pizzaDao.getAllPizzas()
    }

    override suspend fun insertAllPizzas(pizzas: List<Pizza>) {
        pizzaDao.insertAllPizzas(pizzas)
    }

    override suspend fun deleteAllPizzas() {
        pizzaDao.deleteAllPizzas()
    }

    override suspend fun getDataHash(): String {
        return pizzaDao.getDataHash()
    }

    override suspend fun updateDataHash(hash: HashEntity) {
        updateDataHash(hash= hash)
    }


}