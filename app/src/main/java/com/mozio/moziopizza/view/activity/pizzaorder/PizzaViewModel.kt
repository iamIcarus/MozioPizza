package com.mozio.moziopizza.view.activity.pizzaorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mozio.moziopizza.R
import com.mozio.moziopizza.data.local.db.PizzaDao
import com.mozio.moziopizza.data.models.Pizza
import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.remote.repository.PizzaRepository
import com.mozio.moziopizza.data.utli.Resource
import com.mozio.moziopizza.domain.PizzaType
import com.mozio.moziopizza.domain.model.PizzaFlavorItem
import com.mozio.moziopizza.domain.model.PizzaOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PizzaViewModel @Inject constructor(
    private val pizzaRepository: PizzaRepository,
    @Named("pizzaDao") private val pizzaDao: PizzaDao
) : ViewModel() {

    private val _pizzaResponse = MutableLiveData<Resource<PizzaResponse>>()
    val pizzaResponse: LiveData<Resource<PizzaResponse>>
        get() = _pizzaResponse

    suspend fun getPizza(): Flow<Resource<PizzaResponse?>> = pizzaRepository.getPizza()

    suspend fun getPizzaFromDao(): Flow<Resource<PizzaResponse?>> = getAllPizzasFlow()

    fun getPizzaOptionItems(pizzaType: List<PizzaType> , pizzaItems: List<Pizza>): List<PizzaOption> {

        val pizzaFlavorItems = pizzaItems.map {  PizzaFlavorItem.createFromPizza(it)}

        return pizzaType.map { PizzaOption.createFromPizzaFlavorItem(it , pizzaFlavorItems) }
    }

    fun getPizzaTypeList(from: PizzaType): List<PizzaType>{
        when(from)
        {
            PizzaType.FULL -> { return listOf(PizzaType.FULL)  }
            PizzaType.HALF -> { return listOf(PizzaType.HALF, PizzaType.HALF)  }
        }
    }


    suspend fun getAllPizzasFlow(): Flow<Resource<PizzaResponse>> = flow {
        try {
            emit(Resource.Loading)
            val data = pizzaDao.getAllPizzas()
            emit(Resource.Success(PizzaResponse(items = data, pizzaDao.getDataHash(), errorMessage = null) , isNew = true))
        }
        catch (e: Exception) {
            Resource.Error("Error fetching pizza menu: ${e.message}")
            print(e.message)
        }
    }.flowOn(Dispatchers.IO)


}