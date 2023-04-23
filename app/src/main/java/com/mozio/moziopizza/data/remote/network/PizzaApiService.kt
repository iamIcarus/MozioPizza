package com.mozio.moziopizza.data.remote.network

import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.utli.Resource
import okhttp3.ResponseBody
import retrofit2.http.GET


/**
 * Interface for the Retrofit service to handle the Pizza Api methods
 */
interface PizzaApiService {

    /**
     * Gets the pizza JSON data from the API
     * @return the JSON data string.
     */
    @GET(PIZZA_API)
    suspend fun getPizza(): ResponseBody


    private companion object {
        const val PIZZA_API = "pizzas.json"
    }
}