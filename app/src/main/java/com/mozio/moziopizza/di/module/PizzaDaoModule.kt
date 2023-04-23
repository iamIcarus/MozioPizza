package com.mozio.moziopizza.di.module

import android.content.Context
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.mozio.moziopizza.data.local.db.AppDatabase
import com.mozio.moziopizza.data.local.db.PizzaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object PizzaDaoModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pizza_db"
        ).build()
    }

    @Provides
    @Named("pizzaDao")
    fun providePizzaDao(database: AppDatabase): PizzaDao {
        return database.pizzaDao()
    }
}