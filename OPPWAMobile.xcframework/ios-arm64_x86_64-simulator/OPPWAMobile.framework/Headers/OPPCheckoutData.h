//
//
// Copyright (c) $$year$$ by ACI Worldwide, Inc.
// © Copyright ACI Worldwide, Inc. 2018,2024
// All rights reserved.
//
// This software is the confidential and proprietary information
// of ACI Worldwide Inc ("Confidential Information"). You shall
// not disclose such Confidential Information and shall use it
// only in accordance with the terms of the license agreement
// you entered with ACI Worldwide Inc.
//
        

#import <Foundation/Foundation.h>

/** Class to encapsulate the parameters related to the checkout data. */

NS_ASSUME_NONNULL_BEGIN

@interface OPPCDBilling : NSObject
@property (nonatomic, copy) NSString *street1;
@property (nonatomic, copy) NSString *street2;
@property (nonatomic, copy) NSString *city;
@property (nonatomic, copy) NSString *state;
@property (nonatomic, copy) NSString *postcode;
@property (nonatomic, copy) NSString *country;
@end

@interface OPPCDItem : NSObject
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *quantity;
@property (nonatomic, copy) NSString *price;
@property (nonatomic, copy) NSString *sku;
@property (nonatomic, copy) NSString *productURL;
@property (nonatomic, copy) NSString *imageURL;
@property (nonatomic, copy) NSString *totalDiscountAmount;
@end

@interface OPPCDCart : NSObject
@property (nonatomic, copy) NSArray<OPPCDItem *> *items;
@end

@interface OPPCDMerchant : NSObject
@property (nonatomic, copy) NSString *name;
@end

@interface OPPCDCustomer : NSObject
@property (nonatomic, copy) NSString *givenName;
@property (nonatomic, copy) NSString *surname;
@property (nonatomic, copy) NSString *email;
@property (nonatomic, copy) NSString *fullName;
@end

@interface OPPCDShipping : NSObject
@property (nonatomic, copy)   NSString *street1;
@property (nonatomic, copy)   NSString *street2;
@property (nonatomic, copy)   NSString *city;
@property (nonatomic, copy)   NSString *state;
@property (nonatomic, copy)   NSString *postcode;
@property (nonatomic, copy)   NSString *country;
@property (nonatomic, copy)   NSString *cost;
@property (nonatomic, strong) OPPCDCustomer *customer;
@property (nonatomic, copy)   NSString *phone;
@end

@interface OPPCheckoutData : NSObject

/// @name Properties

/** Checkout Data shopper's shipping address. */
@property (nonatomic, strong) OPPCDShipping *shipping;

/** Checkout Data shopper's billing address. */
@property (nonatomic, strong) OPPCDBilling *billing;

/** Checkout Data shopper's cart object. */
@property (nonatomic, strong) OPPCDCart *cart;

/** Checkout Data shopper's cart total amount. */
@property (nonatomic, copy)   NSString *amount;

/** Checkout Data shopper's currency. */
@property (nonatomic, copy)   NSString *currency;

/** Checkout Data tax amount. */
@property (nonatomic, copy)   NSString *taxAmount;

/** Checkout Data merchnat transaction id. */
@property (nonatomic, copy)   NSString *merchantTransactionID;

/** Checkout Data merchnat object. */
@property (nonatomic, strong) OPPCDMerchant *merchant;

/// @name Initialization

/**
 Creates checkout info object from the JSON that can be received from the Server by checkout ID.
 @param JSON A dictionary with parameters received from the Server.
 @param error An error if JSON is invalid.
 @return Returns an `OPPCheckoutInfo` object, or `nil` if JSON is invalid.
 */
+ (nullable instancetype)checkoutDataFromJSON:(NSDictionary *)JSON error:(NSError * _Nullable *)error;

@end
NS_ASSUME_NONNULL_END
