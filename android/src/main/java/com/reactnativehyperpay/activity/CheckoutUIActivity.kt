package com.reactnativehyperpay.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResult
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResultContract
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.checkout.uicomponent.meta.UiComponentsConfig

class CheckoutUIActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 1
        const val EXTRA_CHECKOUT_ID = "CHECKOUT_ID"
        const val EXTRA_LANGUAGE = "LANGUAGE"
        const val PAYMENT_RESULT = "PAYMENT_RESULT"
        const val PAYMENT_METHOD = "PAYMENT_METHOD"
    }

    private val checkoutLauncher = registerForActivityResult(
        CheckoutActivityResultContract()
    ) { result: CheckoutActivityResult ->
        handleCheckoutActivityResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val checkoutId = intent.getStringExtra(EXTRA_CHECKOUT_ID)
        val language = intent.getStringExtra(EXTRA_LANGUAGE)

        if (checkoutId != null && language != null) {
            openCheckoutUI(checkoutId, language)
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun handleCheckoutActivityResult(result: CheckoutActivityResult) {
        val intent = Intent()
        if (result.isCanceled) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            val sharedPreferences = getSharedPreferences("CheckoutPrefs", Context.MODE_PRIVATE)
            val checkoutId = sharedPreferences.getString("checkoutId", null)
            val paymentBrand = sharedPreferences.getString("paymentBrand", null)

            if (checkoutId != null) {
                intent.putExtra(PAYMENT_RESULT, checkoutId)
                intent.putExtra(PAYMENT_METHOD, paymentBrand)
            }
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    private fun openCheckoutUI(checkoutId: String, language: String) {
        val paymentBrands = hashSetOf("VISA", "MASTER", "DIRECTDEBIT_SEPA", "MADA", "GOOGLEPAY")
        val checkoutSettings = CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.TEST)

        when (language) {
            "ar" -> checkoutSettings.setLocale("ar_AR")
            else -> checkoutSettings.setLocale("en_US")
        }

        checkoutLauncher.launch(checkoutSettings)
    }
}