package com.example.stripeandroidexampleproject.customPaymentMethod

import android.content.Context
import com.stripe.android.model.PaymentMethod
import com.stripe.android.paymentelement.ConfirmCustomPaymentMethodCallback
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentelement.CustomPaymentMethodResult
import com.stripe.android.paymentelement.CustomPaymentMethodResultHandler
import com.stripe.android.paymentelement.ExperimentalCustomPaymentMethodsApi

@OptIn(ExperimentalCustomPaymentMethodsApi::class)
class CustomPaymentMethodHandler(private val context: Context) : ConfirmCustomPaymentMethodCallback  {
    override fun onConfirmCustomPaymentMethod(
        customPaymentMethod: PaymentSheet.CustomPaymentMethod,
        billingDetails: PaymentMethod.BillingDetails,
    ) {
        CustomPaymentMethodResultHandler.handleCustomPaymentMethodResult(
            context,
            CustomPaymentMethodResult.failed(displayMessage = "Failed to complete payment"),
        )
    }
}