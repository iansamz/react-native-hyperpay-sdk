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

@import Foundation;
#import <WebKit/WebKit.h>

NS_ASSUME_NONNULL_BEGIN

/// :nodoc:
@interface OPPThreeDSMpgs : NSObject

- (instancetype)init NS_UNAVAILABLE;
- (instancetype)initWithShopperResultUrl:(nullable NSString *)shopperResultUrl;

- (void)proceedMpgsChallengeCompletionWithURL:(NSString *)urlString
                                navController:(UINavigationController *)controller
                               challengeError:(nullable NSError *)error
                                   completion:(void (^)(NSError * _Nullable error))completion;

@end

NS_ASSUME_NONNULL_END
