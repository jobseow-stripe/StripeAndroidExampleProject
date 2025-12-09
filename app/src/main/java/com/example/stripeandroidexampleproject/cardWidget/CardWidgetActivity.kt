package com.example.stripeandroidexampleproject.cardWidget


import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stripeandroidexampleproject.BuildConfig
import com.example.stripeandroidexampleproject.MainActivity
import com.example.stripeandroidexampleproject.R
import com.example.stripeandroidexampleproject.api.API
import com.example.stripeandroidexampleproject.inAppPaymentElement.InAppPaymentElementActivity
import com.stripe.android.paymentsheet.CreateIntentResult


import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.lang.ref.WeakReference

class CardWidgetActivity : AppCompatActivity() {
    // 10.0.2.2 is the Android emulator's alias to localhost
//    private val backendUrl = "http://10.0.2.2:4242/"
    private val backendUrl =  "https://jobseow-localhost.tunnel.stripe.me"
    private val httpClient = OkHttpClient()
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher

    private var api = API()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            BuildConfig.STRIPE_PUBLISHABLE_KEY,
            BuildConfig.ACCOUNT_ID,
            ::onPaymentResult
        )
        CoroutineScope(Dispatchers.IO).launch {
            startCheckout()
        }
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    private fun displayAlert(
        activity: Activity,
        title: String,
        message: String,
        restartDemo: Boolean = false
    ) {
        runOnUiThread {
            val builder = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)

            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    suspend fun startCheckout() {
        val networkResult = api.createPaymentIntent()
        val paymentIntentClientSecret = networkResult.getOrNull()

        print(networkResult)

        if (paymentIntentClientSecret != null) {
            val payButton: Button = findViewById(R.id.payButton)
            payButton.setOnClickListener {
                val cardInputWidget =
                    findViewById<CardInputWidget>(R.id.cardInputWidget)
                cardInputWidget.paymentMethodCreateParams?.let { params ->
                    val confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                    paymentLauncher.confirm(confirmParams)
                }
            }
        } else {
            showToast("Error loading widget")
        }

        // Hook up the pay button to the card widget and stripe instance

    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        var title = ""
        var message = ""
        var restartDemo = false
        when (paymentResult) {
            is PaymentResult.Completed -> {
                title = "Setup Completed"
                restartDemo = true
                showToast("Payment Succeeded")
                val intent = Intent(this@CardWidgetActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            is PaymentResult.Canceled -> {
                title = "Setup Canceled"
                showToast("Payment Failed")
            }
            is PaymentResult.Failed -> {
                title = "Setup Failed"
                message = paymentResult.throwable.message!!
                showToast("Setup Failed")
            }
        }

    }
}