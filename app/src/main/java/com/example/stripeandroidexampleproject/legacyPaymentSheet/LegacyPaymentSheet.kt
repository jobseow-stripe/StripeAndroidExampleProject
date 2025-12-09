package com.example.stripeandroidexampleproject.legacyPaymentSheet

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stripeandroidexampleproject.BuildConfig
import com.example.stripeandroidexampleproject.R
import com.example.stripeandroidexampleproject.api.API
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LegacyPaymentSheet : AppCompatActivity() {

    var api = API()
    private lateinit var clientSecret: String
    private lateinit var ephemeralKeySecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legacy_payment_sheet)
//        PaymentConfiguration.init(applicationContext,  B)

        val newPaymentIntentButton: Button = findViewById(R.id.new_payment)
        val openPaymentSheetButton: Button = findViewById(R.id.open_paymentsheet)

        val paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        newPaymentIntentButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result =  api.createPaymentIntent()
                val ephemeralKeyResult = api.createEphemeralKey()
                if (result != null && ephemeralKeyResult != null) {
                    val ephemeralKey = JSONObject(ephemeralKeyResult)

                    clientSecret = result.getOrNull().toString()
                    ephemeralKeySecret = ephemeralKey.getString("secret")

                    CoroutineScope(
                        Dispatchers.Main).launch {
                        Toast.makeText(applicationContext, "Client secret and ephemeralKey returned",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    CoroutineScope(
                        Dispatchers.Main).launch {
                        Toast.makeText(applicationContext,
                            "Network error, is your server running?",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        openPaymentSheetButton.setOnClickListener {
            // TODO Implement later
            if (this::clientSecret.isInitialized) {
                paymentSheet.presentWithPaymentIntent(clientSecret,
                    PaymentSheet.Configuration(
                        merchantDisplayName = "Stripe Demo",
                        customer = PaymentSheet.CustomerConfiguration(
                            id = BuildConfig.CUSTOMER_ID,
                            ephemeralKeySecret = ephemeralKeySecret
                        ),
                        googlePay = PaymentSheet.GooglePayConfiguration(
                            environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
                        countryCode = "US",
                            currencyCode = "USD" // Required for Setup Intents, optional for Payment Intents
                        )
                    ))
            }
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        Log.d("MyStripeProject", paymentSheetResult.toString())
        val message = when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> "Payment completed"
            is PaymentSheetResult.Canceled -> "Payment canceled"
            else -> "N/A"
        }
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }



}