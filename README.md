# StripeAndroidExampleProject
Stripe Android Example Project

## Available Pages

This project demonstrates various Stripe integration patterns on Android:

### 1. Customer Sheet
Manage customer payment methods

<img width="399" height="839" alt="Customer_sheet" src="https://github.com/user-attachments/assets/84c2de16-c8de-461f-b74c-427773718252" />

- Creates `CustomerSheet` with merchant display name configuration
- `LaunchedEffect` configures the sheet and retrieves payment option selection
- Displays selected payment method with icon and label
- Button to present customer sheet for payment method management

### 2. Accept a Payment Sheet
Accept payments using Stripe Payment Sheet

<img width="354" height="746" alt="payment_sheet" src="https://github.com/user-attachments/assets/370e7cd1-2c66-4bfa-8eea-fab27f14aa4a" />


- `onCreate` sets content for `CheckoutScreen`
- `LaunchedEffect` creates PaymentIntent via API call
- On success, displays enabled "Pay now" button
- Presents `PaymentSheet` when button is clicked
- Handles payment result callbacks (Completed, Canceled, Failed)

### 3. Accept an in-app Payment
In-app embedded payment element integration

<img width="357" height="744" alt="in_app_payment" src="https://github.com/user-attachments/assets/ac6f14a1-da34-443a-bfbb-dd587e7f83ff" />


- `onCreate` sets content for checkout screen
- `EmbeddedPaymentElement.Builder` with `createIntentCallback` creates PaymentIntent
- `LaunchedEffect` configures the embedded payment element with intent configuration
- Shows embedded content: `embeddedPaymentElement.Content()`
- "Confirm payment" button triggers payment confirmation
- Handles result callbacks (Completed, Failed, Canceled)

### 4. Accept Custom Payment Method
Custom payment method implementation

<img width="360" height="746" alt="custom_payment" src="https://github.com/user-attachments/assets/1ca14bf8-cc20-444a-be53-c6aed2cf517f" />


- `onCreate` sets content for `CheckoutScreen`
- Creates `EmbeddedPaymentElement` with custom payment method handler
- `LaunchedEffect` configures with custom payment method ID (cpmt_1Sc61dAjmB9vIEQDbk6mWt1A)
- Displays `embeddedPaymentElement.Content()` with custom payment options
- "Confirm payment" button triggers payment with custom method

### 5. Accept Card Payment
Traditional card widget for card payments

<img width="347" height="730" alt="card_widget" src="https://github.com/user-attachments/assets/1ffe8594-b90d-4d00-9f37-45f47f972234" />

- `onCreate` sets content view to XML layout (activity_checkout)
- Creates `PaymentLauncher` and starts checkout coroutine
- Creates PaymentIntent via API
- Sets up `CardInputWidget` with pay button listener
- Confirms payment using `ConfirmPaymentIntentParams` when button clicked
- Handles payment result (Completed, Canceled, Failed)

### 6. WebView
WebView-based payment flow

<img width="359" height="756" alt="webview" src="https://github.com/user-attachments/assets/0ab901df-a2ff-4369-8d89-d22d41f824af" />

- `onCreate` sets content with `MyWebView` composable
- Creates WebView with JavaScript enabled
- Loads Stripe test payment element URL (https://4242.io/test/payment-element/)
- Uses `AndroidView` to display WebView in Jetpack Compose

### 7. Legacy Payment Sheet
Legacy payment sheet implementation with customer configuration

<img width="399" height="848" alt="legacy_payment_sheet" src="https://github.com/user-attachments/assets/251a6b40-8be3-4df3-97f5-e00973d850b8" />

- `onCreate` sets content view to XML layout (activity_legacy_payment_sheet)
- "New Payment" button creates PaymentIntent and EphemeralKey
- "Open PaymentSheet" button presents PaymentSheet with customer configuration
- Uses `CustomerConfiguration` with customer ID and ephemeral key secret
- Includes Google Pay configuration for test environment
- Handles payment result callbacks

## Setup

Add your Stripe keys to `local.properties`:
```properties
STRIPE_PUBLISHABLE_KEY=pk_test_your_key_here
ACCOUNT_ID=your_account_id_here
CUSTOMER_ID=your_customer_id_here
```
