package com.mozio.moziopizza.domain.model

import com.mozio.moziopizza.data.models.Pizza
import com.mozio.moziopizza.domain.PizzaType

data class PizzaFlavorItem (val id: Int,
                            val name: String,
                            val price: Double,
                            var selected: Boolean){


        companion object {
            fun createFromPizza(pizza: Pizza): PizzaFlavorItem {
                val id = pizza.id
                val name = pizza.name ?: "N/A"
                val price:Double = (pizza.price ?: 0) as Double
                val selected = false

                return PizzaFlavorItem(id, name, price, selected)
            }

            fun createFrom(pizzaFlavorItem: PizzaFlavorItem, selected: Boolean): PizzaFlavorItem {
                    return PizzaFlavorItem(
                        id = pizzaFlavorItem.id,
                        name = pizzaFlavorItem.name,
                        price = pizzaFlavorItem.price,
                        selected = selected
                    )
            }
        }
}

data class PizzaOption(var type: PizzaType, val pizzaFlavorList: List<PizzaFlavorItem>){
    companion object {
        fun createFromPizzaFlavorItem(type: PizzaType ,pizzaFlavorList: List<PizzaFlavorItem>): PizzaOption {
            val type = type
            val pizzaFlavorList = pizzaFlavorList
            return PizzaOption(type, pizzaFlavorList)
        }
    }
}