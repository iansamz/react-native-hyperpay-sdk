//
//  OPPErrors+Private.h
//  OPPWAMobile
//
//  Created by Teploukhova, Alisa on 30/08/16.
//  Copyright © 2017 ACI Worldwide. All rights reserved.
//

#import "OPPErrors.h"

@interface OPPErrors (Private)
+ (nonnull NSError *)epr_errorWithCode:(OPPErrorCode)code;
+ (nonnull NSError *)epr_errorWithCode:(OPPErrorCode)code description:(nullable NSString *)description userInfo:(nullable NSDictionary *)userInfo;
@end
