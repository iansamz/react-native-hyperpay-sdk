//
//  OPPPaymentParams+Brand.h
//  OPPWAMobile
//
//  Created by Teploukhova, Alisa on 08/11/16.
//  Copyright © 2017 ACI Worldwide. All rights reserved.
//

#import "OPPPaymentParams.h"

/// :nodoc:
NS_ASSUME_NONNULL_BEGIN
@interface OPPPaymentParams (Brand)

/** Apple Pay */
+ (BOOL)ppb_isApplePayBrand:(nonnull NSString *)brand;
+ (nullable NSString *)ppb_findApplePayBrand:(nullable NSArray<NSString *> *) brands;

/** Klarna */
+ (nonnull NSArray<NSString *> *)ppb_klarnaSupportedCountries;
+ (nonnull NSString *)ppb_klarnaCountryForLocale:(nonnull NSLocale *)locale;
+ (BOOL)ppb_isKlarnaInlineBrand:(nonnull NSString *)brand;

/** CashAppPay */
+ (BOOL)ppb_isCashAppPay:(nonnull NSString *)brand;
@end
NS_ASSUME_NONNULL_END
