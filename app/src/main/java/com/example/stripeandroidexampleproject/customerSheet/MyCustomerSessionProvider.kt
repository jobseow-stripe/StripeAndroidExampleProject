package com.example.stripeandroidexampleproject.customerSheet

import android.util.Log
import com.example.stripeandroidexampleproject.api.API
import com.stripe.android.customersheet.CustomerSheet
import com.stripe.android.customersheet.CustomerSheet.CustomerSessionClientSecret
import kotlin.fold

class MyCustomerSessionProvider : CustomerSheet.CustomerSessionProvider() {

    companion object {
        private const val TAG = "MyCustomerSessionProvider"
    }

    private val api = API()

    override suspend fun provideSetupIntentClientSecret(customerId: String): Result<String> {
        Log.d(TAG, "Providing setup intent client secret for customer: $customerId")
        return api.createSetupIntent(customerId).fold(
            onSuccess = { response ->
                Log.d(TAG, "Successfully provided setup intent client secret")
                Result.success(response.setupIntentId)
            },
            onFailure = { exception ->
                Log.e(TAG, "Failed to provide setup intent client secret", exception)
                Result.failure(exception)
            }
        )
    }

    override suspend fun providesCustomerSessionClientSecret(): Result<CustomerSessionClientSecret> {
        Log.d(TAG, "Providing customer session client secret")

        return api.createCustomer().fold(
            onSuccess = { response ->
                Log.d(TAG, "Successfully provided customer session client secret")
                Result.success(
                    CustomerSessionClientSecret.create(
                        customerId = response.customerId,
                        clientSecret = response.customerSessionClientSecret,
                    )
                )
            },
            onFailure = { exception ->
                Log.e(TAG, "Failed to provide customer session client secret", exception)
                Result.failure(exception)
            }
        )
    }
}