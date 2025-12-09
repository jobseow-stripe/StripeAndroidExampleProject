package com.example.stripeandroidexampleproject.paymentSheet

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.stripeandroidexampleproject.api.API
import com.example.stripeandroidexampleproject.ui.theme.StripeAndroidExampleProjectTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
class PaymentSheetActivity: ComponentActivity() {

    private val api = API()

    companion object {
        private const val BACKEND_URL = "https://jobseow-localhost.tunnel.stripe.me"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckoutScreen()
        }
    }

    @Composable
    private fun PayButton(
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onClick
        ) {
            Text("Pay now")
        }
    }

    @Composable
    private fun ErrorAlert(
        errorMessage: String,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            title = {
                Text(text = "Error occurred during checkout")
            },
            text = {
                Text(text = errorMessage)
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onDismiss) {
                    Text(text = "Ok")
                }
            }
        )
    }



    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onPayClicked(
        paymentSheet: PaymentSheet,
        paymentIntentClientSecret: String,
    ) {
        val configuration = PaymentSheet.Configuration.Builder(merchantDisplayName = "Example, Inc.")
            .build()

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }


    @Composable
    private fun CheckoutScreen() {
        var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

        var error by remember { mutableStateOf<String?>(null) }

        val paymentSheet = rememberPaymentSheet { paymentResult ->
            when (paymentResult) {
                is PaymentSheetResult.Completed -> showToast("Payment complete!")
                is PaymentSheetResult.Canceled -> showToast("Payment canceled!")
                is PaymentSheetResult.Failed -> {
                    error = paymentResult.error.localizedMessage ?: paymentResult.error.message
                }
            }
        }

        error?.let { errorMessage ->
            ErrorAlert(
                errorMessage = errorMessage,
                onDismiss = {
                    error = null
                }
            )
        }

        LaunchedEffect(Unit) {
            api.createPaymentIntent().onSuccess { clientSecret ->
                paymentIntentClientSecret = clientSecret
            }.onFailure { paymentIntentError ->
                error = paymentIntentError.localizedMessage ?: paymentIntentError.message
            }
        }

        StripeAndroidExampleProjectTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    PayButton(
                        enabled = paymentIntentClientSecret != null,
                        onClick = {
                            paymentIntentClientSecret?.let {
                                onPayClicked(
                                    paymentSheet = paymentSheet,
                                    paymentIntentClientSecret = it,
                                )
                            }
                        }
                    )
                }
            }
        }


    }

}