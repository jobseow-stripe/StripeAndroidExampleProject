package com.example.stripeandroidexampleproject.customerSheet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.stripe.android.customersheet.CustomerSheetResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

import com.stripe.android.paymentsheet.model.PaymentOption

class CustomerSheetViewModel(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "CustomerSheetViewModel"
    }

    val customerSessionProvider = MyCustomerSessionProvider()

    private val _paymentOption = MutableStateFlow<PaymentOption?>(null)
    val paymentOption: StateFlow<PaymentOption?> = _paymentOption

    fun handleResult(result: CustomerSheetResult) {
        Log.d(TAG, "Handling customer sheet result: ${result::class.simpleName}")
        when (result) {
            is CustomerSheetResult.Selected -> {
                Log.d(TAG, "Payment option selected: ${result.selection?.paymentOption?.label}")
                _paymentOption.update {
                    result.selection?.paymentOption
                }
            }
            is CustomerSheetResult.Canceled -> {
                Log.d(TAG, "Customer sheet canceled")
                _paymentOption.update {
                    result.selection?.paymentOption
                }
            }
            is CustomerSheetResult.Failed -> {
                Log.e(TAG, "Customer sheet failed", result.exception)
            }
        }
    }
}