//
// Copyright (c) $$year$$ by ACI Worldwide, Inc.
// All rights reserved.
//
// This software is the confidential and proprietary information
// of ACI Worldwide Inc ("Confidential Information"). You shall
// not disclose such Confidential Information and shall use it
// only in accordance with the terms of the license agreement
// you entered with ACI Worldwide Inc.
//

#import "OPPTransaction.h"

/// :nodoc:
NS_ASSUME_NONNULL_BEGIN

@interface OPPThreeDSViewController : UIViewController

- (nullable instancetype)initWithTransaction:(OPPTransaction *)transaction
                             paymentProvider:(OPPPaymentProvider *)provider
                           completionHandler:(void (^)(OPPTransaction *transaction,
                                                       NSError * _Nullable error))completionHandler;

@end

NS_ASSUME_NONNULL_END
