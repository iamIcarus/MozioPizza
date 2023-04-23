package com.mozio.moziopizza.view.activity.pizzaorder

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mozio.moziopizza.R
import com.mozio.moziopizza.data.models.Pizza
import com.mozio.moziopizza.data.models.PizzaResponse
import com.mozio.moziopizza.data.utli.Resource
import com.mozio.moziopizza.databinding.ActivityMainBinding
import com.mozio.moziopizza.domain.PizzaType
import com.mozio.moziopizza.view.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity  : BaseActivity() {
    private val pizzaViewModel by viewModels<PizzaViewModel>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var pizzaAdapter: PizzaAdapter

    private lateinit var pizzaType: PizzaType


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()

        enableControls(false)

        initRecyclerView()

        requestData()
    }

    /**
     * Initializes the RecyclerView for displaying pizzas.
     * Sets the initial pizza type to full and sets up the RecyclerView adapter and layout manager.
     */
    fun initRecyclerView()
    {
        // Set the initial pizza type to full
        pizzaType = PizzaType.FULL

        // Create a new pizza adapter and set it as the adapter for the RecyclerView
        pizzaAdapter = PizzaAdapter()

        // Set up the layout manager for the RecyclerView
        val spanCount = resources.getInteger(R.integer.pizza_recycler_span_count)
        val layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerViewPizzas.layoutManager = layoutManager

        binding.recyclerViewPizzas.adapter = pizzaAdapter
    }

    /**
      *Toggles the visibility of the loading indicator and enables/disables user controls accordingly.
      *
      * @param show If true, the loading indicator will be displayed and user controls will be disabled. If false, the loading indicator will be hidden and user controls will be enabled.
      */
    private fun toggleLoading(show:Boolean) {
        // Disables user controls if loading is shown, enables them otherwise
        enableControls(!show)

        // Sets the visibility of the loading indicator based on the show parameter
        binding.loadingIndicator.setVisibility(if (show) { View.VISIBLE } else { View.GONE })
    }

    /**
      *Enables/disables the radio group and radio buttons based on the given parameter. Also sets the alpha
      *of the radio group to 0.6f if controls are disabled, otherwise to 1f.
      *@param enable True if controls should be enabled, false otherwise.
     */
    fun enableControls(enable:Boolean){
        binding.radioGroup.isEnabled = enable
        binding.radioButtonFull.isEnabled = enable
        binding.radioButtonHalf.isEnabled = enable

        binding.radioGroup.alpha = if (!enable) {  0.6f } else { 1f }
    }

    /**
      *Updates the data in the pizza adapter with the given pizza items by creating new pizza option items and setting them to the adapter.
      *@param pizzaItems The list of pizza items to update the adapter data with.
     */
    private fun updateAdapterData(pizzaItems: List<Pizza>)
    {
        val pizzaOptions = pizzaViewModel.getPizzaOptionItems(
            pizzaType = pizzaViewModel.getPizzaTypeList(PizzaType.FULL),
            pizzaItems = pizzaItems)

        pizzaAdapter.setPizzaList(pizzaOptions)
    }

    /**
     * Observes pizza data and updates the UI based on the data received.
     * If data is being loaded, the loading indicator is shown.
     * If data is loaded successfully, the adapter data is updated with the new pizza data and the loading indicator is hidden.
     * If there's an error, the loading indicator is hidden and an error message is shown.
     */
    fun getDataFromRepository(){
        lifecycleScope.launch {
            pizzaViewModel.getPizza().collect { resource ->
                when (resource) {
                    // Show loading indicator
                    is Resource.Loading -> {  toggleLoading(show = true)  }

                    // Update the adapter with the pizza data
                    is Resource.Success -> {
                        updateAdapterData(pizzaItems = resource.data?.items ?: emptyList() )
                        toggleLoading(show = false)
                    }

                    // Show error message
                    is Resource.Error -> {
                        toggleLoading(show = false)
                    }
                }
            }
        }
    }

    /**
     * Gets data from a Dao and updates the adapter with the pizza data obtained from the Dao.
     * If data is being loaded, the loading indicator is shown.
     * If there's an error, the loading indicator is hidden and an error message is shown.
     */
    fun getDataFromDao()
    {
        lifecycleScope.launch {
            pizzaViewModel.getPizzaFromDao().collect { resource ->
                when (resource) {
                    // Show loading indicator
                    is Resource.Loading -> {  toggleLoading(show = true)  }

                    // Update the adapter with the pizza data
                    is Resource.Success -> {
                        updateAdapterData(pizzaItems = resource.data?.items ?: emptyList())
                        toggleLoading(show = false)
                    }

                    // Show error message
                    is Resource.Error -> {
                        toggleLoading(show = false)
                    }
                }
            }
        }
    }

    /**
     * Requests pizza data from the repository if there is an internet connection, otherwise requests
     * it from the local database. Shows a loading indicator while fetching data.
     * If the repository does not detect that the data is new , it will return from database
     */
    fun requestData()
    {
        if(isInternetAvailable()){
            getDataFromRepository()
        }
        else{
            getDataFromDao()
        }
    }

    /**
     * Resets the data for a new order
     */
    fun resetData(){
        removeButtonListeners()

        pizzaType = PizzaType.FULL
        binding.radioButtonFull.isChecked = true

        setButtonListeners()

        getDataFromDao()
    }

    /**
     * If the checkout is success , display message and reset data if user choose to
     */
    fun handleCheckOutSuccess()
    {
        showAlertYesNoWithCompletion("Order completed successfully! Start new?" , {  resetData() } )
    }

    /**
     * Check for data change on our pizza JSON data before checkout to catch any change
     */
    fun dataChangeDetected(resource: Resource.Success<PizzaResponse?>)
    {
        showAlertYesNoWithCompletion("Cannot proceed to checkout, data are outdated. Synchronise new data now?" , {
            updateAdapterData(pizzaItems = resource.data?.items ?: emptyList())
        })

    }


    /**
     * Basic checks before checkout , item selections for lists and total amount confirmation by the user
     */
    fun prepareForCheckOut(){

        val  items = pizzaAdapter.getItems()

        val isSelectionsOk = items.all { option ->
            option.pizzaFlavorList.any { flavor -> flavor.selected }
        }


        if(!isSelectionsOk){
            showAlertOk("Need to choose pizza options to complete the checkout")
            return
        }

        var totalSelectedFlavorCost = items.sumOf { pizzaOption ->
            pizzaOption.pizzaFlavorList
                .filter { it.selected }
                .sumOf { it.price }
        }

        //We are in half pizza choose
        if (pizzaType == PizzaType.HALF){
            totalSelectedFlavorCost = totalSelectedFlavorCost / 2
        }

        showAlertYesNoWithCompletion("Total cost is $$totalSelectedFlavorCost , continue?" , { performCheckout() })
    }

    /**
     * Performs the checkout process by checking if the current data is already saved in the database.
     * If it is, a confirmation dialog will be shown to ask the user if they want to proceed with the checkout,
     * otherwise the checkout process will be completed directly.
     */
    fun performCheckout(){

        lifecycleScope.launch {
            pizzaViewModel.getPizzaFromDao().collect { resource ->
                when (resource) {
                    // Show loading indicator
                    is Resource.Loading -> {  toggleLoading(show = true)  }

                    // Update the adapter with the pizza data
                    is Resource.Success -> {
                        if (resource == null){
                            showAlertYesNoWithCompletion("Ops, something went wrong while trying to checkout. Try again?" , { prepareForCheckOut() })
                        }
                        else {
                            if (resource.isNew ?: false) {
                                handleCheckOutSuccess()
                            } else {
                                dataChangeDetected(resource)
                            }
                        }

                        toggleLoading(show = false)
                    }

                    // Show error message
                    is Resource.Error -> {
                        toggleLoading(show = false)
                    }
                }
            }
        }
    }

    /**
     * Creates the button listeners
     */
    fun setButtonListeners(){
        binding.buttonCheckout.setOnClickListener {prepareForCheckOut()  }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonFull -> {
                    pizzaType = PizzaType.FULL
                    pizzaAdapter.changeType(type = pizzaType)
                }

                R.id.radioButtonHalf -> {
                    pizzaType = PizzaType.HALF
                    pizzaAdapter.changeType(type = pizzaType)
                }
            }
        }
    }

    /**
     * Removes the button listeners listeners
     */
    fun removeButtonListeners(){
        binding.buttonCheckout.setOnClickListener(null)
        binding.radioGroup.setOnCheckedChangeListener(null)
    }


}