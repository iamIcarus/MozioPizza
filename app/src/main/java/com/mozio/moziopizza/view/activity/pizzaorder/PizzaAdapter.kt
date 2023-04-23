package com.mozio.moziopizza.view.activity.pizzaorder

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozio.moziopizza.R
import com.mozio.moziopizza.databinding.ItemPizzaBinding
import com.mozio.moziopizza.domain.PizzaType
import com.mozio.moziopizza.domain.model.PizzaFlavorItem
import com.mozio.moziopizza.domain.model.PizzaOption

class PizzaAdapter() : RecyclerView.Adapter<PizzaViewHolder>() {

    private var pizzaOptionsList: MutableList<PizzaOption> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pizza, parent, false)
        val binding = ItemPizzaBinding.bind(view)

        return PizzaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        holder.bind(pizzaOptionsList[position])
    }

    /*
     * Returns the number of items in the list. Outer item will be 1 or 2
     * depending on the selection. Also pizzaFlavorsList must have data
     * otherwise its an indicator that something went wrong
     */
    override fun getItemCount(): Int = if( pizzaOptionsList.isEmpty() ) { 0 } else{ pizzaOptionsList.count() }

    /*
    * Sets the adapter list data and notify change
    */
    fun setPizzaList(pizzaFlavorsList: List<PizzaOption>) {
        this.pizzaOptionsList.clear()
        this.pizzaOptionsList.addAll(pizzaFlavorsList)
        notifyDataSetChanged()
    }


    fun getItems(): List<PizzaOption> {
        return pizzaOptionsList
    }
    /*
     * Function will change pizza type. It basically using the existing data to create the 2 different types
     * Switching type will reset the data.
     * (This should not be here and we should find a better way to do this)
     */
    fun changeType(type: PizzaType)
    {
        if(this.pizzaOptionsList.isEmpty())
            return

        val firstpizzaFlavorListItem =  this.pizzaOptionsList.subList(0, 1)[0].pizzaFlavorList

        //call the setPizzaList with the new data (deep copy)
        //for case HALF we duplicate the data to have the half/half option
        when(type){
            PizzaType.FULL -> { setPizzaList(pizzaFlavorsList = mutableListOf(
                PizzaOption( type = type ,
                    firstpizzaFlavorListItem.map  { pizzaFlavorItem -> PizzaFlavorItem.createFrom(pizzaFlavorItem,false) }.toMutableList()))) }
            PizzaType.HALF -> { setPizzaList(pizzaFlavorsList = mutableListOf(
                PizzaOption( type = type ,
                    firstpizzaFlavorListItem.map  { pizzaFlavorItem -> PizzaFlavorItem.createFrom(pizzaFlavorItem,false) }.toMutableList()) ,
                PizzaOption( type = type ,
                    firstpizzaFlavorListItem.map  { pizzaFlavorItem -> PizzaFlavorItem.createFrom(pizzaFlavorItem,false) }.toMutableList())))}
        }
    }
}

class PizzaViewHolder(private val binding: ItemPizzaBinding) : RecyclerView.ViewHolder(binding.root) {

    // inner recycler adapter
    private val pizzaFlavorsAdapter = PizzaFlavorAdapter({ selectedItemIndex , isChecked -> setSelection(selectedItemIndex , isChecked)})

    init {
        binding.recyclerViewPizzas.adapter = pizzaFlavorsAdapter
    }

    fun bind(pizzaOption: PizzaOption) {
        binding.textViewPizzaOption.text = pizzaOption.type.type
        binding.recyclerViewPizzas.layoutManager = GridLayoutManager(binding.root.context, if (pizzaOption.type == PizzaType.FULL) 1 else 2)
        pizzaFlavorsAdapter.setData(pizzaOption, pizzaOption.pizzaFlavorList)
    }

    private fun setSelection(selectedItemIndex: Int , isChecked: Boolean){
        pizzaFlavorsAdapter.setSelection(selectedItemIndex,isChecked)
        pizzaFlavorsAdapter.notifyDataSetChanged()
    }

}