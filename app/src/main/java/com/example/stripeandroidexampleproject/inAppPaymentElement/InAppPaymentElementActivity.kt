package com.example.stripeandroidexampleproject.inAppPaymentElement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stripeandroidexampleproject.MainActivity
import com.example.stripeandroidexampleproject.api.API
import com.stripe.android.paymentelement.EmbeddedPaymentElement
import com.stripe.android.paymentelement.rememberEmbeddedPaymentElement
import com.stripe.android.paymentsheet.CreateIntentResult
import com.stripe.android.paymentsheet.PaymentSheet

class InAppPaymentElementActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckoutScreen()
        }
    }

    var api = API()

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    @Composable
    fun CheckoutScreen() {

        val embeddedBuilder = remember {
            EmbeddedPaymentElement.Builder(
                createIntentCallback = { confirmationToken, ->
                    val networkResult = api.createPaymentIntent()
                    val paymentIntentClientSecret = networkResult.getOrNull()

                    print(networkResult)

                    if (paymentIntentClientSecret != null) {
                        CreateIntentResult.Success(paymentIntentClientSecret)
                    } else {
                        val error =  Exception("Could not find payment intent client secret in response!")
                        CreateIntentResult.Failure(error, "error")
                    }
                },
                resultCallback = { result ->
                    when (result) {
                        is EmbeddedPaymentElement.Result.Completed -> {
                            // Payment completed - show a confirmation screen.
                            showToast("Payment Succeeded")
                            val intent = Intent(this@InAppPaymentElementActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is EmbeddedPaymentElement.Result.Failed -> {
                            showToast("Payment Failed")

                            // Encountered an unrecoverable error. You can display the error to the user, log it, and so on
                        }
                        is EmbeddedPaymentElement.Result.Canceled -> {
                            showToast("Payment Cancelled")
                            // Customer canceled - you should probably do nothing.
                        }
                    }
                },
            )
        }

        val embeddedPaymentElement = rememberEmbeddedPaymentElement(embeddedBuilder)

        LaunchedEffect(embeddedPaymentElement) {
            embeddedPaymentElement.configure(
                intentConfiguration = PaymentSheet.IntentConfiguration(
                    mode = PaymentSheet.IntentConfiguration.Mode.Payment(
                        amount = 1099,
                        currency = "USD",
                    ),
                    // Optional intent configuration options...
                ),
                configuration = EmbeddedPaymentElement.Configuration.Builder("Powdur").build()
            )
        }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            embeddedPaymentElement.Content()
            Button(
                onClick = {
                    embeddedPaymentElement.confirm()
                }
            ) {
                Text("Confirm payment")
            }

        }
    }
}