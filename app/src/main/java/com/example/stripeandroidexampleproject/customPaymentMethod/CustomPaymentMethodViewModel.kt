package com.example.stripeandroidexampleproject.customPaymentMethod

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.stripeandroidexampleproject.customerSheet.MyCustomerSessionProvider
import com.stripe.android.customersheet.CustomerSheetResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

import com.stripe.android.paymentsheet.model.PaymentOption

class CustomPaymentMethodViewModel(
    application: Application
) : AndroidViewModel(application) {
    val customPaymentMethodHandler = CustomPaymentMethodHandler(application)

}