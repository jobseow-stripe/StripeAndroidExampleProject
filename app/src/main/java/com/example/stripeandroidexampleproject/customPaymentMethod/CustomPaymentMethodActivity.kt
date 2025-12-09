package com.example.stripeandroidexampleproject.customPaymentMethod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stripeandroidexampleproject.api.API
import com.example.stripeandroidexampleproject.customerSheet.CustomerSheetViewModel
import com.example.stripeandroidexampleproject.ui.theme.StripeAndroidExampleProjectTheme
import com.stripe.android.paymentelement.EmbeddedPaymentElement
import com.stripe.android.paymentelement.ExperimentalCustomPaymentMethodsApi
import com.stripe.android.paymentelement.rememberEmbeddedPaymentElement
import com.stripe.android.paymentsheet.CreateIntentResult
import com.stripe.android.paymentsheet.PaymentSheet
import kotlin.getValue

class CustomPaymentMethodActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckoutScreen()
        }
    }
    private val viewModel by viewModels<CustomPaymentMethodViewModel>()

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    var api = API()
    @OptIn(ExperimentalCustomPaymentMethodsApi::class)
    @Composable
    fun CheckoutScreen() {


        val embeddedPaymentElementBuilder = remember {
            EmbeddedPaymentElement.Builder(
                createIntentCallback = { paymentMethod, shouldSavePaymentMethod ->

                    val networkResult = api.createPaymentIntent()
                    val paymentIntentClientSecret = networkResult.getOrNull()

                    print(networkResult)

                    if (paymentIntentClientSecret != null) {
                        val paymentIntentClientSecret = networkResult.getOrNull() ?: ""
                        CreateIntentResult.Success(paymentIntentClientSecret)
                    } else {
                        val error =  Exception("Could not find payment intent client secret in response!")
                        CreateIntentResult.Failure(error, "error")
                    }
                    // Create intent
                },
                resultCallback = { result ->
                    when (result) {
                        is EmbeddedPaymentElement.Result.Completed -> showToast("Payment complete!")
                        is EmbeddedPaymentElement.Result.Canceled -> showToast("Payment canceled!")
                        is EmbeddedPaymentElement.Result.Failed -> showToast("failed")
                    }
                },
            ).apply {
                confirmCustomPaymentMethodCallback(viewModel.customPaymentMethodHandler)
            }
        }

        val embeddedPaymentElement = rememberEmbeddedPaymentElement(embeddedPaymentElementBuilder)

        LaunchedEffect(embeddedPaymentElement) {
            val customPaymentMethod = PaymentSheet.CustomPaymentMethod(
                id = "cpmt_1Sc61dAjmB9vIEQDbk6mWt1A",
                subtitle = "Optional subtitle"
            )

            embeddedPaymentElement.configure(
                configuration = EmbeddedPaymentElement.Configuration.Builder("Powdur")
                    .customPaymentMethods(listOf(customPaymentMethod))
                    .build(),


                intentConfiguration = PaymentSheet.IntentConfiguration(
                    mode = PaymentSheet.IntentConfiguration.Mode.Payment(
                        amount = 1099,
                        currency = "USD",
                    ),
                    // Optional intent configuration options...
                ),
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