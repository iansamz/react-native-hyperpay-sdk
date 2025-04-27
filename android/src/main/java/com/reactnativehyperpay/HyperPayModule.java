package com.reactnativehyperpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.payment.BrandsValidation;
import com.oppwa.mobile.connect.payment.CheckoutInfo;
import com.oppwa.mobile.connect.payment.ImagesRequest;
import com.oppwa.mobile.connect.payment.PaymentParams;
import com.oppwa.mobile.connect.payment.card.CardPaymentParams;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.ITransactionListener;
import com.oppwa.mobile.connect.provider.OppPaymentProvider;
import com.oppwa.mobile.connect.provider.ThreeDSWorkflowListener;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.reactnativehyperpay.activity.CheckoutUIActivity;


@ReactModule(name = HyperPayModule.NAME)
public class HyperPayModule extends ReactContextBaseJavaModule implements ITransactionListener {
    public static final String NAME = "HyperPay";

    private Context appContext;
    private Promise promisePaymentTransaction;
    private String shopperResultURL;
    private String merchantIdentifier;
    private String countryCode;
    private String mode;
    
    private static final String PREFS_NAME = "HyperPayPrefs";
    private static final String TOKEN_KEY = "token";
    private static final String PHONE_KEY = "phone_number";
    private static final String PLAN_ID_KEY = "planID";
    private static final String PLAN_TYPE_KEY = "planType";
    
    private Callback successCallback;
    private Callback errorCallback;

    public HyperPayModule(ReactApplicationContext reactContext) {
        super(reactContext);
        appContext = reactContext.getApplicationContext();
        
        // Add activity listener
        reactContext.addActivityEventListener(new BaseActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                if (requestCode == CheckoutUIActivity.REQUEST_CODE) {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        WritableMap resultData = Arguments.createMap();
                        resultData.putString("checkoutID", data.getStringExtra(CheckoutUIActivity.PAYMENT_RESULT));
                        resultData.putString("paymentBrand", data.getStringExtra(CheckoutUIActivity.PAYMENT_METHOD));
                        resultData.putString("resourcePath", "");
                        successCallback.invoke(resultData);
                    } else {
                        errorCallback.invoke("Payment was cancelled by the user");
                    }
                    successCallback = null;
                    errorCallback = null;
                }
            }
        });
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public WritableMap setup(ReadableMap params) {
        WritableMap config = Arguments.createMap();
        if (params.hasKey("shopperResultURL"))
            shopperResultURL = params.getString("shopperResultURL");
        if (params.hasKey("merchantIdentifier"))
            merchantIdentifier = params.getString("merchantIdentifier");
        if (params.hasKey("countryCode"))
            countryCode = params.getString("countryCode");
        if (params.hasKey("mode"))
            mode = params.getString("mode");
        config.putString("shopperResultURL", shopperResultURL);
        config.putString("merchantIdentifier", merchantIdentifier);
        config.putString("countryCode", countryCode);
        config.putString("mode", mode);
        return config;
    }

    @ReactMethod
    public void createPaymentTransaction(ReadableMap params, Promise promise) {
        promisePaymentTransaction = promise;
        this.emitListeners("onProgress", true);
        try {
            PaymentParams paymentParams = new CardPaymentParams(
                    params.getString("checkoutID"),
                    params.getString("paymentBrand"),
                    params.getString("cardNumber"),
                    params.getString("holderName"),
                    params.getString("expiryMonth"),
                    params.getString("expiryYear"),
                    params.getString("cvv"));

            if (params.hasKey("shopperResultURL")) {
                shopperResultURL = params.getString("shopperResultURL");
            }
            paymentParams.setShopperResultUrl(shopperResultURL);
            Transaction transaction = null;

            try {
                OppPaymentProvider paymentProvider = new OppPaymentProvider(appContext, Connect.ProviderMode.TEST);
                paymentProvider.setThreeDSWorkflowListener(new ThreeDSWorkflowListener() {
                    @Override
                    public Activity onThreeDSChallengeRequired() {
                        return getCurrentActivity();
                    }
                });

                if (mode.equals("LiveMode")) {
                    paymentProvider.setProviderMode(Connect.ProviderMode.LIVE);
                }
                transaction = new Transaction(paymentParams);
                paymentProvider.submitTransaction(transaction, this);
            } catch (PaymentException e) {
                this.emitListeners("onProgress", false);
                promisePaymentTransaction.reject(e);
            }
        } catch (PaymentException e) {
            this.emitListeners("onProgress", false);
            promisePaymentTransaction.reject(e);
        }
    }

    @ReactMethod
    public void openCheckoutUI(ReadableMap params, Callback successCallback, Callback errorCallback) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            errorCallback.invoke("Activity Error: Current activity is null");
            return;
        }

        try {
            String checkoutId = params.getString("checkoutId");
            String token = params.getString("token");
            
            SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN_KEY, token);

            if (params.hasKey("phoneNumber")) {
                editor.putString(PHONE_KEY, params.getString("phoneNumber"));
            }
            if (params.hasKey("planId")) {
                editor.putInt(PLAN_ID_KEY, params.getInt("planId"));
            }
            if (params.hasKey("planType")) {
                editor.putString(PLAN_TYPE_KEY, params.getString("planType"));
            }
            editor.apply();

            Intent intent = new Intent(currentActivity, CheckoutUIActivity.class);
            intent.putExtra(CheckoutUIActivity.EXTRA_CHECKOUT_ID, checkoutId);
            intent.putExtra(CheckoutUIActivity.EXTRA_LANGUAGE, "en");
            currentActivity.startActivityForResult(intent, CheckoutUIActivity.REQUEST_CODE);

            this.successCallback = successCallback;
            this.errorCallback = errorCallback;
        } catch (Exception e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    private void emitListeners(String eventName, boolean isLoading) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onProgress", isLoading);
    }

    @Override
    public void transactionCompleted(@NonNull Transaction transaction) {
        this.emitListeners("onProgress", false);

        WritableMap paymentResponse = Arguments.createMap();
        paymentResponse.putString("checkoutId", transaction.getPaymentParams().getCheckoutId());

        if (transaction.getTransactionType() == TransactionType.SYNC) {
            paymentResponse.putString("status", "completed");
        } else {
            paymentResponse.putString("status", "pending");
            paymentResponse.putString("redirectURL", transaction.getRedirectUrl());
        }

        promisePaymentTransaction.resolve(paymentResponse);
    }

    @Override
    public void transactionFailed(@NonNull Transaction transaction, @NonNull PaymentError paymentError) {
        this.emitListeners("onProgress", false);
        promisePaymentTransaction.reject(paymentError.getErrorInfo());
    }

    @Override
    public void brandsValidationRequestSucceeded(@NonNull BrandsValidation brandsValidation) {
        ITransactionListener.super.brandsValidationRequestSucceeded(brandsValidation);
    }

    @Override
    public void brandsValidationRequestFailed(@NonNull PaymentError paymentError) {
        ITransactionListener.super.brandsValidationRequestFailed(paymentError);
    }

    @Override
    public void paymentConfigRequestSucceeded(@NonNull CheckoutInfo checkoutInfo) {
        Log.d("paymentCond", checkoutInfo.getResourcePath());
        ITransactionListener.super.paymentConfigRequestSucceeded(checkoutInfo);
    }

    @Override
    public void paymentConfigRequestFailed(@NonNull PaymentError paymentError) {
        ITransactionListener.super.paymentConfigRequestFailed(paymentError);
    }

    @Override
    public void imagesRequestSucceeded(@NonNull ImagesRequest imagesRequest) {
        ITransactionListener.super.imagesRequestSucceeded(imagesRequest);
    }

    @Override
    public void imagesRequestFailed() {
        ITransactionListener.super.imagesRequestFailed();
    }

    @Override
    public void binRequestSucceeded(@NonNull String[] strings) {
        ITransactionListener.super.binRequestSucceeded(strings);
    }

    @Override
    public void binRequestFailed() {
        ITransactionListener.super.binRequestFailed();
    }

}
