//
//  OPPBrandConfig.h
//  OPPWAMobile
//
//  Created by Dalvi, Vikas on 09/08/24.
//  Copyright © 2024 ACI Worldwide. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OPPPaymentBrand.h"

NS_ASSUME_NONNULL_BEGIN

@interface OPPBrandConfig : NSObject

@property (nonatomic, nonnull, readonly) OPPPaymentBrand paymentBrand;

@end

NS_ASSUME_NONNULL_END
