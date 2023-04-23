package com.mozio.moziopizza.view.activity.pizzaorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.remote.repository.PizzaRepository
import com.mozio.moziopizza.data.utli.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PizzaViewModel @Inject constructor(
    private val pizzaRepository: PizzaRepository
) : ViewModel() {

    private val _pizzaResponse = MutableLiveData<Resource<PizzaResponse?>>()
    val pizzaResponse: LiveData<Resource<PizzaResponse?>>
        get() = _pizzaResponse

    fun getPizza() = viewModelScope.launch(Dispatchers.IO){

        try {
            pizzaRepository.getPizza().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _pizzaResponse.value = result
                    }
                    is Resource.Success -> {
                        _pizzaResponse.value = result
                    }
                    is Resource.Error -> {
                        _pizzaResponse.value = result
                    }
                }
            }
        }
        catch (e: Exception) {
            print("Something went wrong!\n$e")
        }
    }
}