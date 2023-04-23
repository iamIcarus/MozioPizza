package com.mozio.moziopizza.data.remote.repository

import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.utli.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the PizzaRepository that uses the Retrofit service to fetch the data and map it to a PizzaResponse
 */
interface PizzaRepository {
    suspend fun getPizza(): Flow<Resource<PizzaResponse?>>
}