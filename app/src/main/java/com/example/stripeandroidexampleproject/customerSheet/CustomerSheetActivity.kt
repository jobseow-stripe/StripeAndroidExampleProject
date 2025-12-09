package com.example.stripeandroidexampleproject.customerSheet

import android.R.attr.text
import android.annotation.SuppressLint

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stripe.android.customersheet.CustomerSheet
import com.stripe.android.customersheet.rememberCustomerSheet
import com.stripe.android.uicore.image.rememberDrawablePainter

class CustomerSheetActivity : ComponentActivity() {
    companion object {
        private const val TAG = "CustomerSheetActivity"
    }

    private val viewModel by viewModels<CustomerSheetViewModel>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CheckoutActivity created")
        enableEdgeToEdge()

        val configuration =
            CustomerSheet.Configuration.builder(merchantDisplayName = "StripeAndroidExampleProject")
                .build()
        setContent {
            val customerSheet = rememberCustomerSheet(
                customerSessionProvider = viewModel.customerSessionProvider,
                callback = viewModel::handleResult // Implemented in next step
            )

            LaunchedEffect(customerSheet) {
                Log.d(TAG, "Configuring customer sheet")
                customerSheet.configure(configuration = configuration)
                Log.d(TAG, "Retrieving payment option selection")
                viewModel.handleResult(customerSheet.retrievePaymentOptionSelection())
            }

            val paymentOption by viewModel.paymentOption.collectAsState()
            Log.d(TAG, "Current payment option: ${paymentOption?.label}")

            Log.d(TAG, "Current payment option: ${paymentOption?.label}")
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            val icon = paymentOption?.icon()
                            TextButton(
                                onClick = {
                                    customerSheet.present()
                                }
                            ) {
                                if (icon != null) {
                                    Image(
                                        painter = rememberDrawablePainter(
                                            drawable = icon
                                        ),
                                        contentDescription = "Payment Method Icon",
                                        modifier = Modifier.height(32.dp)
                                    )
                                }
                                Text(
                                    text = paymentOption?.label ?: "Select"
                                )
                            }
                        }

                    }
        }

//            StripeAndroidExampleProjectTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        TextButton(
//                            onClick = {
//                                customerSheet.present()
//                            }
//                        ) {
//                            Text(
//                                text = "Customer Sheet"
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}
