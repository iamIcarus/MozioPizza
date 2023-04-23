package com.mozio.moziopizza.view.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun isInternetAvailable() : Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    fun showAlertYesNoWithCompletion(
        message: String,
        onYes: () -> Unit
    ) {

        runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(message)
            builder.setPositiveButton("Yes") { _, _ -> onYes.invoke() }
            builder.setNegativeButton("No", null)
            builder.show()
        }

    }

    fun showAlertOk( message: String  ) {

        runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(message)
            builder.setPositiveButton("Ok", null)
            builder.show()
        }

    }
}