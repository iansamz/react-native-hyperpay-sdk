//
//  OPPWAPaymentProvider.h
//  OPPWAMobile
//
//  Created by Samalov, Artem on 6/8/16.
//  Copyright © 2017 ACI Worldwide. All rights reserved.
//

#import "OPPPaymentProvider.h"

/// :nodoc:

NS_ASSUME_NONNULL_BEGIN
@interface OPPWAPaymentProvider : OPPPaymentProvider
/// Initialization with a provider mode
- (instancetype)initWithMode:(OPPProviderMode)mode;
@end
NS_ASSUME_NONNULL_END
