package com.mozio.moziopizza.view.activity.pizzaorder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.mozio.moziopizza.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity  : AppCompatActivity() {

    private val pizzaViewModel by viewModels<PizzaViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* binding = ActivityPizzaMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.[viewModel = viewModel
        binding.lifecycleOwner = this*/
        pizzaViewModel.getPizza()
    }

    override fun onStart() {
        super.onStart()
    }
}