import { NativeModules, Platform } from 'react-native';
import { getPaymentStatus } from './paymentStatus'

const LINKING_ERROR =
  `The package 'react-native-hyperpay-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const HyperPaySDK = NativeModules.HyperPay
  ? NativeModules.HyperPay
  : new Proxy(
    {},
    {
      get() {
        throw new Error(LINKING_ERROR);
      },
    }
  );
export function init(params) {
  return HyperPaySDK.setup(params);
}

export function createPaymentTransaction(params) {
  return HyperPaySDK.createPaymentTransaction(params);
}

export function openCheckoutUI(params) {
  return HyperPaySDK.openCheckoutUI(params);
}

export function applePay(checkoutID) {
  return HyperPaySDK.applePay(checkoutID);
}

const Hyperpay = {
  init,
  applePay,
  createPaymentTransaction,
  openCheckoutUI,
  getPaymentStatus
}

export default Hyperpay