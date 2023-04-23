package com.mozio.moziopizza.view.activity.pizzaorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mozio.moziopizza.databinding.PizzaFlavorItemBinding
import com.mozio.moziopizza.domain.PizzaType
import com.mozio.moziopizza.domain.model.PizzaFlavorItem
import com.mozio.moziopizza.domain.model.PizzaOption

class PizzaFlavorAdapter(private val onPizzaFlavorSelected: (Int,Boolean) -> Unit) : RecyclerView.Adapter<PizzaFlavorViewHolder>() {

    private lateinit var pizzaOption: PizzaOption
    private var pizzaFlavors: MutableList<PizzaFlavorItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaFlavorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PizzaFlavorItemBinding.inflate(inflater, parent, false)
        return PizzaFlavorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PizzaFlavorViewHolder, position: Int) {
        holder.bind(pizzaOption , pizzaFlavors[position], onPizzaFlavorSelected)
    }

    override fun getItemCount(): Int {
        return pizzaFlavors.size
    }

    /*
    * Sets the adapter list data and notify change
    */
    fun setData(pizzaOption: PizzaOption , pizzaFlavors: List<PizzaFlavorItem>) {
        this.pizzaOption = pizzaOption

        this.pizzaFlavors.clear()
        this.pizzaFlavors.addAll(pizzaFlavors)
        notifyDataSetChanged()
    }


    /*
    * Will remove selections for all items but the target index
    */
    fun setSelection(forIndex: Int, isChecked: Boolean) {
        pizzaFlavors.forEachIndexed  { index , pizzaFlavor ->
            if (forIndex != index)  {
                pizzaFlavor.selected = false
            }else
            {
                pizzaFlavor.selected = isChecked
            }
        }
    }
}

class PizzaFlavorViewHolder(private val binding: PizzaFlavorItemBinding ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pizzaOption: PizzaOption , pizzaFlavor: PizzaFlavorItem,  onPizzaFlavorSelected: (Int, Boolean) -> Unit) {

        //set the name of the pizza flavor
        when(pizzaOption.type){
            PizzaType.FULL -> {
                binding.checkBox.text = "${pizzaFlavor.name} ($${String.format("%.2f", pizzaFlavor.price)})"
            }

            PizzaType.HALF -> {
                binding.checkBox.text = "${pizzaFlavor.name} ($${String.format("%.2f", pizzaFlavor.price / 2)})"
            }
        }

        //Remove listener before assigning value so we wont trigger a checked call
        binding.checkBox.setOnCheckedChangeListener(null)

        //set the check status
        binding.checkBox.isChecked = pizzaFlavor.selected


        //Set listener again
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onPizzaFlavorSelected(adapterPosition,isChecked)
        }
    }
}