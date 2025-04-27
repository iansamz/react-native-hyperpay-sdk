//
//  OPPCheckoutProvider+DeviceAuth.h
//  OPPWAMobile
//
//  Created by Teploukhova, Alisa on 16/04/2018.
//  Copyright © 2018 ACI Worldwide. All rights reserved.
//

#import "OPPCheckoutProvider.h"
@import SafariServices;
@class OPPPaymentParams;
#import "OPPSecurityPolicy.h"

/// :nodoc:

NS_ASSUME_NONNULL_BEGIN
@interface OPPCheckoutProvider (DeviceAuth) <SFSafariViewControllerDelegate>

+ (void)cpd_requestDeviceAuthIfNeededForPaymentBrand:(nonnull NSString *)paymentBrand
                                             isToken:(BOOL)isToken
                                       paymentParams:(OPPPaymentParams *)paymentParams
                                          completion:(void (^)(BOOL success))completion;

+ (OPPSecurityPolicyMode)cpd_deviceAuthModeForPaymentBrand:(NSString *)paymentBrand;
+ (OPPSecurityPolicyMode)cpd_deviceAuthModeForTokens;

@end
NS_ASSUME_NONNULL_END
