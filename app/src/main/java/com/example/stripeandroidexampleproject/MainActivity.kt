package com.example.stripeandroidexampleproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.stripeandroidexampleproject.cardWidget.CardWidgetActivity
import com.example.stripeandroidexampleproject.customPaymentMethod.CustomPaymentMethodActivity
import com.example.stripeandroidexampleproject.customerSheet.CustomerSheetActivity
import com.example.stripeandroidexampleproject.inAppPaymentElement.InAppPaymentElementActivity
import com.example.stripeandroidexampleproject.legacyPaymentSheet.LegacyPaymentSheet
import com.example.stripeandroidexampleproject.paymentSheet.PaymentSheetActivity
import com.example.stripeandroidexampleproject.ui.theme.StripeAndroidExampleProjectTheme
import com.example.stripeandroidexampleproject.webview.WebViewActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StripeAndroidExampleProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        TextButton(onClick = {
                            val intent = Intent(this@MainActivity, CustomerSheetActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Customer Sheet")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, PaymentSheetActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Accept a Payment Sheet")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, InAppPaymentElementActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Accept an in-app Payment")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, CustomPaymentMethodActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Accept Custom Payment Method")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, CardWidgetActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Accept Card Payment")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "WebView")
                        }
                        TextButton(  onClick = {
                            val intent = Intent(this@MainActivity, LegacyPaymentSheet::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "Legacy Payment Sheet")
                        }
                    }
                }
            }
        }
    }
}
