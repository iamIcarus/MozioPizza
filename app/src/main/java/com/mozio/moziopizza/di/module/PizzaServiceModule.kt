package com.mozio.moziopizza.di.module

import com.mozio.moziopizza.data.local.db.PizzaDao
import com.mozio.moziopizza.data.remote.network.PizzaApiService
import com.mozio.moziopizza.data.remote.repository.PizzaRepository
import com.mozio.moziopizza.data.remote.repository.PizzaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

/**
 *  A Dagger module that provides the dependencies for the Pizza Service feature.
 */
@Module
@InstallIn(ViewModelComponent::class)
object PizzaServiceModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Provides an instance of Retrofit to be used for API calls
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    // Provides an instance of PizzaApiService to be used for calling the API methods
    @Provides
    fun providePizzaApiService(retrofit: Retrofit): PizzaApiService {
        return retrofit.create(PizzaApiService::class.java)
    }

    //Provides an instance of the PizzaRepository interface, which defines methods for retrieving Pizza data from the API.
    @Provides
    fun providePizzaRepository(pizzaApiService: PizzaApiService,@Named("pizzaDao") pizzaDaoService: PizzaDao): PizzaRepository {
        return PizzaRepositoryImpl(pizzaApiService,pizzaDaoService)
    }

    // The base URL for the API calls
    private const val BASE_URL = "https://static.mozio.com/mobile/tests/"
}

