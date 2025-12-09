package com.example.stripeandroidexampleproject.api

import android.util.Log
import com.example.stripeandroidexampleproject.BuildConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class API {
    private val okHttpClient = OkHttpClient()

    companion object {
        private const val TAG = "MyBackend"
    }

    var baseUrl = "https://jobseow-localhost.tunnel.stripe.me"

    suspend fun createCustomer(): Result<CustomerSessionResponse> {
        Log.d(TAG, "Creating customer session at: $baseUrl/customer-session")
        return try {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val emptyJson = JSONObject()

            val request = Request.Builder()
                .url("$baseUrl/customer-session")
                .post(emptyJson.toString().toRequestBody(mediaType))
                .build()

            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(TAG, "Failed to create customer: HTTP ${response.code}")
                return Result.failure(IOException("Unexpected response code: ${response.code}"))
            }

            val responseBody = response.body?.string()
                ?: return Result.failure(IOException("Empty response body"))

            Log.d(TAG, "Customer session response: $responseBody")
            val json = JSONObject(responseBody)
            val customerResponse = CustomerSessionResponse(
                customerSessionClientSecret = json.getString("customerSessionClientSecret"),
                customerId = json.getString("customer")
            )

            Log.d(TAG, "Successfully created customer: ${customerResponse.customerId}")
            Result.success(customerResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating customer", e)
            Result.failure(e)
        }
    }

    suspend fun createSetupIntent(customerId: String): Result<SetupIntentResponse> {
        Log.d(TAG, "Creating setup intent for customer: $customerId")
        return try {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val jsonObject = JSONObject().apply {
                put("customer", customerId)
            }

            val request = Request.Builder()
                .url("$baseUrl/create-setup-intent-android")
                .post(jsonObject.toString().toRequestBody(mediaType))
                .build()

            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(TAG, "Failed to create setup intent: HTTP ${response.code}")
                return Result.failure(IOException("Unexpected response code: ${response.code}"))
            }

            val responseBody = response.body?.string()
                ?: return Result.failure(IOException("Empty response body"))

            Log.d(TAG, "Setup intent response: $responseBody")
            val json = JSONObject(responseBody)
            val setupIntentResponse = SetupIntentResponse(
                setupIntentId = json.getString("setupIntent")
            )

            Log.d(TAG, "Successfully created setup intent")
            Result.success(setupIntentResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating setup intent", e)
            Result.failure(e)
        }
    }
    private fun extractClientSecretFromResponse(response: Response): String? {
        return try {
            val responseData = response.body?.string()
            val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()

            responseJson.getString("paymentIntent")
        } catch (exception: JSONException) {
            null
        }
    }
    suspend fun createPaymentIntent(): Result<String> = suspendCoroutine { continuation ->
        val url = "$baseUrl/create-payment-intent-android"

        val shoppingCartContent = """
            {
                "items": [
                    {"id":"xl-tshirt"}
                ]
            }
        """

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val body = shoppingCartContent.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resume(Result.failure(Exception(response.message)))
                    } else {
                        val clientSecret = extractClientSecretFromResponse(response)

                        clientSecret?.let { secret ->
                            continuation.resume(Result.success(secret))
                        } ?: run {
                            val error = Exception("Could not find payment intent client secret in response!")

                            continuation.resume(Result.failure(error))
                        }
                    }
                }
            })
    }
     fun createEphemeralKey(): String? {
        val jsonObject = JSONObject().let {
            it.put("customerId", BuildConfig.CUSTOMER_ID)
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request: Request = Request.Builder()
            .url("${baseUrl}/create-ephemeral-key")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()
        return try {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string()
        } catch (e: IOException) {
            null
        }
    }
}