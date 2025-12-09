# StripeAndroidExampleProject
Stripe Android Example Project

## Available Pages

This project demonstrates various Stripe integration patterns on Android:

### 1. Customer Sheet
Manage customer payment methods

![Customer Sheet](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content
- Creates `CustomerSheet` with merchant display name configuration
- `LaunchedEffect` configures the sheet and retrieves payment option selection
- Displays selected payment method with icon and label
- Button to present customer sheet for payment method management

### 2. Accept a Payment Sheet
Accept payments using Stripe Payment Sheet

![Payment Sheet](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content for `CheckoutScreen`
- `LaunchedEffect` creates PaymentIntent via API call
- On success, displays enabled "Pay now" button
- Presents `PaymentSheet` when button is clicked
- Handles payment result callbacks (Completed, Canceled, Failed)

### 3. Accept an in-app Payment
In-app embedded payment element integration

![In-app Payment](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content for checkout screen
- `EmbeddedPaymentElement.Builder` with `createIntentCallback` creates PaymentIntent
- `LaunchedEffect` configures the embedded payment element with intent configuration
- Shows embedded content: `embeddedPaymentElement.Content()`
- "Confirm payment" button triggers payment confirmation
- Handles result callbacks (Completed, Failed, Canceled)

### 4. Accept Custom Payment Method
Custom payment method implementation

![Custom Payment Method](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content for `CheckoutScreen`
- Creates `EmbeddedPaymentElement` with custom payment method handler
- `LaunchedEffect` configures with custom payment method ID (cpmt_1Sc61dAjmB9vIEQDbk6mWt1A)
- Displays `embeddedPaymentElement.Content()` with custom payment options
- "Confirm payment" button triggers payment with custom method

### 5. Accept Card Payment
Traditional card widget for card payments

![Card Payment](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content view to XML layout (activity_checkout)
- Creates `PaymentLauncher` and starts checkout coroutine
- Creates PaymentIntent via API
- Sets up `CardInputWidget` with pay button listener
- Confirms payment using `ConfirmPaymentIntentParams` when button clicked
- Handles payment result (Completed, Canceled, Failed)

### 6. WebView
WebView-based payment flow

![WebView Payment](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content with `MyWebView` composable
- Creates WebView with JavaScript enabled
- Loads Stripe test payment element URL (https://4242.io/test/payment-element/)
- Uses `AndroidView` to display WebView in Jetpack Compose

### 7. Legacy Payment Sheet
Legacy payment sheet implementation with customer configuration

![Legacy Payment Sheet](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*d6mo6NC-KPMqLF1LyQ4XXg.png)

- `onCreate` sets content view to XML layout (activity_legacy_payment_sheet)
- "New Payment" button creates PaymentIntent and EphemeralKey
- "Open PaymentSheet" button presents PaymentSheet with customer configuration
- Uses `CustomerConfiguration` with customer ID and ephemeral key secret
- Includes Google Pay configuration for test environment
- Handles payment result callbacks

## Setup

Add your Stripe keys to `secrets.properties`:
```properties
STRIPE_PUBLISHABLE_KEY=pk_test_your_key_here
ACCOUNT_ID=your_account_id_here
CUSTOMER_ID=your_customer_id_here
```
