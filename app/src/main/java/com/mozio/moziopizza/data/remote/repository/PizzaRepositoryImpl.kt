package com.mozio.moziopizza.data.remote.repository

import com.mozio.moziopizza.data.local.db.PizzaDao
import com.mozio.moziopizza.data.local.models.HashEntity
import com.mozio.moziopizza.data.models.Pizza
import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.remote.network.PizzaApiService
import com.mozio.moziopizza.data.utli.Resource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.security.MessageDigest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

/**
 * Fetches the pizza menu from the API and saves it to the database if it's new.
 * Returns a [Flow] of [Resource] with the fetched data or an error.
 */
class PizzaRepositoryImpl @Inject constructor(
    private val pizzaApiService: PizzaApiService,
    @Named("pizzaDao") private val pizzaDao: PizzaDao
) : PizzaRepository {
    override suspend fun getPizza(): Flow<Resource<PizzaResponse?>> = flow {
        try {
                //TODO: We need a new object to handle below logic
                // pizzaApiService should only get the data and
                // PizzaDao should only do the I/O


                // Get the local hash from the database
                val localHash = pizzaDao.getDataHash()

                // we don't have data, block the UI and show loading , this may take a while
                if (localHash == null) {
                    emit(Resource.Loading)
                }

                 // Fetch pizza menu from the API
                val responseBody = pizzaApiService.getPizza()
                val responseData = responseBody.string()

                // Compute the hash of the fetched data
                // Optimal scenario would have been for the hash to be provided so we wont need
                // to download the all data and compute it through the app
                val remoteHash = hashString(input = responseData )



                // Check for changes
                // Naive way to check for changes. Although for this app it wont make much difference
                // but generally we want to have a way to detect changes for
                // 1. Save time when parsing and saving big data
                // 2. Allow the user to use local data and notify on change (checkout)
                if (localHash != remoteHash) {

                    // parse JSON data to List<Pizza>
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val pizzaListAdapter = moshi.adapter<List<Pizza>>(
                        Types.newParameterizedType(List::class.java, Pizza::class.java)
                    )
                    val pizzaList = pizzaListAdapter.fromJson(responseData)

                    // Create a response object with the parsed data and the remote hash
                    val response = PizzaResponse(pizzaList ?: listOf(), remoteHash, null)

                    // Save pizza data to database
                    pizzaDao.deleteAllPizzas()
                    pizzaDao.insertAllPizzas(response.items)
                    pizzaDao.updateDataHash(HashEntity(id = 1, dataHash = remoteHash))
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Success(null,  "No new data available"))
                }

            } catch (e: Exception) {
                Resource.Error("Error fetching pizza menu: ${e.message}")
                print(e.message)
            }

        }.flowOn(Dispatchers.IO)

    /**
     * Computes the SHA-256 hash of the input string and returns it as a hex string.
     */
    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}