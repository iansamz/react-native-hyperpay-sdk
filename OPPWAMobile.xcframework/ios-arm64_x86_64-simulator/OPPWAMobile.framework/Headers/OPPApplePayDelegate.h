//
//  OPPApplePayDelegate.h
//  OPPWAMobile
//
//  Created by Teploukhova, Alisa on 25/01/17.
//  Copyright © 2017 ACI Worldwide. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <PassKit/PassKit.h>
@class OPPPaymentParams;
@class OPPCheckoutProvider;

NS_ASSUME_NONNULL_BEGIN
@interface OPPApplePayDelegate : NSObject <PKPaymentAuthorizationViewControllerDelegate>
@property (nonatomic, copy, nullable) void (^successfulCompletion) (OPPPaymentParams *paymentParams);
@property (nonatomic, nullable) OPPCheckoutProvider *checkoutProvider;
- (void)cleanUp;
@end
NS_ASSUME_NONNULL_END
