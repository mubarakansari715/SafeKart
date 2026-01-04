# Firebase Backend Database Plan

## Overview
This document describes how user data and shopping data will be structured and managed in Firebase Firestore for the MVP e-commerce platform.

---

## Database Structure Overview

Firebase Firestore will use **Collections** (like tables) to organize data. Each collection contains **Documents** (like rows) with **Fields** (like columns).

---

## 1. USER DATA MODELS

### 1.1 Users Collection
**Collection Name**: `users`

**Purpose**: Store all customer account information

**Document Structure** (One document per user, using Firebase Auth UID as document ID):
- **Basic Information**
  - `email` - User's email address
  - `phoneNumber` - User's phone number (for OTP login)
  - `displayName` - Full name of the user
  - `photoURL` - Profile picture URL (stored in Firebase Storage)
  - `emailVerified` - Boolean, whether email is verified
  - `phoneVerified` - Boolean, whether phone is verified

- **Account Details**
  - `createdAt` - Timestamp when account was created
  - `updatedAt` - Timestamp of last profile update
  - `lastLoginAt` - Timestamp of last login
  - `accountStatus` - String: "active", "suspended", "deleted"

- **Preferences**
  - `preferences` - Object containing:
    - `emailNotifications` - Boolean for marketing emails
    - `pushNotifications` - Boolean for push notifications
    - `smsNotifications` - Boolean for SMS notifications
    - `language` - Preferred language (default: "en")
    - `currency` - Preferred currency

- **Addresses** (Array of address objects)
  - Each address object contains:
    - `id` - Unique ID for this address
    - `fullName` - Recipient name
    - `phoneNumber` - Contact phone
    - `addressLine1` - Street address
    - `addressLine2` - Apartment, suite, etc. (optional)
    - `city` - City name
    - `state` - State/Province
    - `zipCode` - Postal/ZIP code
    - `country` - Country name
    - `isDefault` - Boolean, is this the default address
    - `createdAt` - When address was added

- **Payment Methods** (Array of payment objects)
  - Each payment method contains:
    - `id` - Unique ID for this payment method
    - `type` - String: "card", "paypal"
    - `last4` - Last 4 digits of card (for cards)
    - `brand` - Card brand: "visa", "mastercard", etc.
    - `expiryMonth` - Expiry month (for cards)
    - `expiryYear` - Expiry year (for cards)
    - `isDefault` - Boolean, is this the default payment method
    - `createdAt` - When payment method was added
    - Note: Full card details stored securely in payment gateway (Stripe/PayPal), not in Firebase

**Relationships**:
- Connected to `orders` collection via `userId` field
- Connected to `carts` collection via document ID (userId)
- Connected to `wishlists` collection via document ID (userId)

---

## 2. SHOPPING DATA MODELS

### 2.1 Products Collection
**Collection Name**: `products`

**Purpose**: Store all product information for the catalog

**Document Structure** (One document per product):
- **Basic Product Info**
  - `name` - Product name
  - `description` - Detailed product description
  - `shortDescription` - Brief description for listings
  - `sku` - Stock Keeping Unit (unique product identifier)
  - `category` - Main category: "men", "women", "unisex"
  - `subcategory` - Subcategory: "t-shirts", "pants", "dresses", etc.
  - `tags` - Array of tags for search: ["casual", "summer", "cotton"]

- **Pricing**
  - `price` - Current selling price (number)
  - `compareAtPrice` - Original price (for showing discounts)
  - `costPrice` - Internal cost price (for admin only)
  - `currency` - Currency code: "USD", "EUR", etc.

- **Images**
  - `images` - Array of image objects:
    - `url` - Image URL (stored in Firebase Storage)
    - `alt` - Alt text for accessibility
    - `order` - Display order (0, 1, 2, etc.)
    - `isPrimary` - Boolean, is this the main image

- **Variants** (Array of variant objects - different sizes/colors)
  - Each variant contains:
    - `id` - Unique variant ID
    - `size` - Size: "S", "M", "L", "XL", etc.
    - `color` - Color name: "Black", "White", "Navy"
    - `colorCode` - Hex color code: "#000000"
    - `sku` - Variant-specific SKU
    - `inventory` - Number of items in stock
    - `price` - Variant-specific price (if different)
    - `image` - Variant-specific image URL (if different)

- **Product Details**
  - `material` - Material composition: "100% Cotton"
  - `careInstructions` - How to care for the product
  - `sizeChart` - Object containing size chart data
  - `weight` - Product weight (for shipping)
  - `dimensions` - Object: {length, width, height}

- **Status & Visibility**
  - `isActive` - Boolean, is product available for sale
  - `isFeatured` - Boolean, show on homepage
  - `isNew` - Boolean, mark as new arrival
  - `isOnSale` - Boolean, is product on sale

- **Ratings & Reviews**
  - `rating` - Object containing:
    - `average` - Average rating (1-5)
    - `count` - Total number of reviews
  - Note: Individual reviews stored in separate `reviews` collection

- **Timestamps**
  - `createdAt` - When product was added
  - `updatedAt` - Last update timestamp
  - `publishedAt` - When product was published

**Relationships**:
- Connected to `reviews` collection via `productId` field
- Referenced in `orders` items via `productId`
- Referenced in `carts` items via `productId`
- Referenced in `wishlists` via `productId`

---

### 2.2 Carts Collection
**Collection Name**: `carts`

**Purpose**: Store shopping cart items for each user

**Document Structure** (One document per user, using userId as document ID):
- **Cart Items** (Array of cart item objects)
  - Each item contains:
    - `productId` - Reference to product document
    - `variantId` - Which variant (size/color) was selected
    - `productName` - Product name (for quick display)
    - `productImage` - Product image URL (for quick display)
    - `size` - Selected size
    - `color` - Selected color
    - `price` - Price at time of adding to cart
    - `quantity` - Number of items
    - `addedAt` - Timestamp when item was added

- **Cart Summary**
  - `subtotal` - Sum of all item prices
  - `shipping` - Shipping cost (calculated)
  - `tax` - Tax amount (calculated)
  - `discount` - Discount amount from promo code
  - `total` - Final total amount

- **Promo Code**
  - `promoCode` - Applied promo code (if any)
  - `promoCodeId` - Reference to promo code document

- **Metadata**
  - `updatedAt` - Last cart update timestamp
  - `expiresAt` - Cart expiration (for abandoned cart cleanup)

**Relationships**:
- Document ID is the `userId` (connects to `users` collection)
- Items reference `products` collection via `productId`
- References `promoCodes` collection via `promoCodeId`

---

### 2.3 Orders Collection
**Collection Name**: `orders`

**Purpose**: Store all customer orders

**Document Structure** (One document per order):
- **Order Identification**
  - `orderNumber` - Unique order number (e.g., "ORD-2024-001234")
  - `userId` - Reference to user who placed order
  - `orderDate` - Timestamp when order was placed

- **Order Items** (Array of order item objects)
  - Each item contains:
    - `productId` - Reference to product
    - `variantId` - Variant that was ordered
    - `productName` - Product name (snapshot at time of order)
    - `productImage` - Product image (snapshot)
    - `size` - Size ordered
    - `color` - Color ordered
    - `quantity` - Quantity ordered
    - `price` - Price at time of order (snapshot)
    - `subtotal` - Item subtotal (price × quantity)

- **Shipping Information**
  - `shippingAddress` - Object containing full shipping address
  - `shippingMethod` - String: "standard", "express"
  - `shippingCost` - Shipping cost
  - `trackingNumber` - Shipping tracking number (when shipped)
  - `carrier` - Shipping carrier name
  - `estimatedDelivery` - Estimated delivery date

- **Billing Information**
  - `billingAddress` - Object containing billing address
  - `paymentMethod` - Object containing payment method details
  - `paymentStatus` - String: "pending", "paid", "failed", "refunded"
  - `paymentId` - Payment gateway transaction ID

- **Order Totals**
  - `subtotal` - Sum of all items
  - `shipping` - Shipping cost
  - `tax` - Tax amount
  - `discount` - Discount from promo code
  - `total` - Final total amount

- **Order Status**
  - `status` - String: "pending", "processing", "shipped", "delivered", "cancelled", "refunded"
  - `statusHistory` - Array of status change objects:
    - `status` - Status value
    - `changedAt` - When status changed
    - `note` - Optional note

- **Promo Code**
  - `promoCode` - Promo code used (if any)
  - `promoCodeId` - Reference to promo code

- **Timestamps**
  - `createdAt` - Order creation timestamp
  - `updatedAt` - Last update timestamp
  - `shippedAt` - When order was shipped
  - `deliveredAt` - When order was delivered

**Relationships**:
- Connected to `users` collection via `userId`
- Items reference `products` collection via `productId`
- References `promoCodes` collection via `promoCodeId`

---

### 2.4 Wishlists Collection
**Collection Name**: `wishlists`

**Purpose**: Store user's saved/favorite products

**Document Structure** (One document per user, using userId as document ID):
- **Wishlist Items** (Array of wishlist item objects)
  - Each item contains:
    - `productId` - Reference to product
    - `addedAt` - Timestamp when added to wishlist
    - `notifyOnPriceDrop` - Boolean, send notification if price drops

- **Metadata**
  - `updatedAt` - Last wishlist update timestamp
  - `itemCount` - Total number of items (for quick display)

**Relationships**:
- Document ID is the `userId` (connects to `users` collection)
- Items reference `products` collection via `productId`

---

### 2.5 Reviews Collection
**Collection Name**: `reviews`

**Purpose**: Store product reviews and ratings from customers

**Document Structure** (One document per review):
- **Review Identification**
  - `reviewId` - Unique review ID
  - `productId` - Reference to product being reviewed
  - `userId` - Reference to user who wrote review
  - `orderId` - Reference to order (to verify purchase)

- **Review Content**
  - `rating` - Number (1-5 stars)
  - `title` - Review title/headline
  - `comment` - Review text/comment
  - `images` - Array of image URLs (customer photos)

- **Reviewer Information** (Snapshot at time of review)
  - `userName` - Reviewer's name
  - `userPhoto` - Reviewer's photo URL

- **Review Status**
  - `isVerifiedPurchase` - Boolean, was this a verified purchase
  - `isApproved` - Boolean, admin approved (for moderation)
  - `isHelpful` - Number, count of helpful votes

- **Helpful Votes** (Array of vote objects)
  - Each vote contains:
    - `userId` - User who voted
    - `votedAt` - When vote was cast

- **Timestamps**
  - `createdAt` - When review was written
  - `updatedAt` - Last update timestamp

**Relationships**:
- Connected to `products` collection via `productId`
- Connected to `users` collection via `userId`
- Connected to `orders` collection via `orderId` (for verification)

---

### 2.6 PromoCodes Collection
**Collection Name**: `promoCodes`

**Purpose**: Store discount codes and promotions

**Document Structure** (One document per promo code):
- **Code Details**
  - `code` - Promo code string (e.g., "SUMMER20")
  - `description` - Description of the promotion
  - `type` - String: "percentage", "fixed_amount"
  - `value` - Discount value (percentage or fixed amount)

- **Usage Rules**
  - `minPurchaseAmount` - Minimum order amount to use code
  - `maxDiscountAmount` - Maximum discount (for percentage codes)
  - `usageLimit` - Total number of times code can be used
  - `usedCount` - Current usage count
  - `usageLimitPerUser` - How many times one user can use it

- **Validity**
  - `validFrom` - Start date/time
  - `validUntil` - End date/time
  - `isActive` - Boolean, is code currently active

- **Applicability**
  - `applicableCategories` - Array of categories code applies to (empty = all)
  - `applicableProducts` - Array of product IDs (empty = all products)

- **Timestamps**
  - `createdAt` - When code was created
  - `updatedAt` - Last update timestamp

**Relationships**:
- Referenced in `carts` collection via `promoCodeId`
- Referenced in `orders` collection via `promoCodeId`

---

## 3. DATA RELATIONSHIPS & CONNECTIONS

### 3.1 User-Centric Relationships
```
User (users/{userId})
    ├── Has Cart (carts/{userId})
    │   └── Contains Products (products/{productId})
    ├── Has Wishlist (wishlists/{userId})
    │   └── Contains Products (products/{productId})
    ├── Has Orders (orders collection, filtered by userId)
    │   └── Contains Products (products/{productId})
    └── Has Reviews (reviews collection, filtered by userId)
        └── For Products (products/{productId})
```

### 3.2 Product-Centric Relationships
```
Product (products/{productId})
    ├── In Carts (carts collection, items array)
    ├── In Wishlists (wishlists collection, items array)
    ├── In Orders (orders collection, items array)
    └── Has Reviews (reviews collection, filtered by productId)
```

### 3.3 Order Flow Relationships
```
Order (orders/{orderId})
    ├── Belongs to User (users/{userId})
    ├── Contains Products (products/{productId})
    ├── Uses Promo Code (promoCodes/{promoCodeId}) - optional
    └── Can Generate Reviews (reviews collection)
```

---

## 4. DATA ACCESS PATTERNS

### 4.1 User Data Access
- **Get User Profile**: Read `users/{userId}` document
- **Update Profile**: Update `users/{userId}` document
- **Add Address**: Add to `addresses` array in `users/{userId}`
- **Add Payment Method**: Add to `paymentMethods` array in `users/{userId}`

### 4.2 Shopping Data Access
- **Browse Products**: Query `products` collection with filters
- **Get Product Details**: Read `products/{productId}` document
- **Get Product Reviews**: Query `reviews` collection filtered by `productId`

### 4.3 Cart Operations
- **Get Cart**: Read `carts/{userId}` document
- **Add to Cart**: Update `carts/{userId}`, add item to `items` array
- **Update Cart Item**: Update specific item in `carts/{userId}/items` array
- **Remove from Cart**: Remove item from `carts/{userId}/items` array
- **Apply Promo Code**: Update `carts/{userId}` with promo code info

### 4.4 Order Operations
- **Create Order**: Create new document in `orders` collection
- **Get User Orders**: Query `orders` collection filtered by `userId`
- **Get Order Details**: Read `orders/{orderId}` document
- **Update Order Status**: Update `status` field in `orders/{orderId}`

### 4.5 Wishlist Operations
- **Get Wishlist**: Read `wishlists/{userId}` document
- **Add to Wishlist**: Add item to `items` array in `wishlists/{userId}`
- **Remove from Wishlist**: Remove item from `wishlists/{userId}/items` array

---

## 5. DATA SECURITY RULES (Conceptual)

### 5.1 Users Collection
- Users can only read/write their own user document
- Admins can read all user documents
- Email and phone number are readable by user only

### 5.2 Products Collection
- Everyone can read products (public)
- Only admins can create/update/delete products

### 5.3 Carts Collection
- Users can only access their own cart document
- Users can read/write their own cart
- No one else can access user carts

### 5.4 Orders Collection
- Users can read their own orders (filtered by userId)
- Users can create orders (with their userId)
- Users can update order status only if pending
- Admins can read/update all orders

### 5.5 Wishlists Collection
- Users can only access their own wishlist document
- Users can read/write their own wishlist

### 5.6 Reviews Collection
- Everyone can read approved reviews
- Users can create reviews (for products they purchased)
- Users can update/delete their own reviews
- Admins can moderate all reviews

### 5.7 PromoCodes Collection
- Everyone can read active promo codes
- Only admins can create/update promo codes

---

## 6. DATA INDEXING REQUIREMENTS

### 6.1 Products Collection Indexes
- Index on `category` + `isActive` (for category browsing)
- Index on `isFeatured` + `createdAt` (for homepage)
- Index on `isOnSale` + `updatedAt` (for sale page)
- Index on `rating.average` (for sorting by rating)
- Index on `price` (for price sorting)

### 6.2 Orders Collection Indexes
- Index on `userId` + `createdAt` (for user order history)
- Index on `status` + `createdAt` (for admin order management)
- Index on `orderNumber` (for order lookup)

### 6.3 Reviews Collection Indexes
- Index on `productId` + `createdAt` (for product reviews)
- Index on `userId` + `createdAt` (for user reviews)
- Index on `isApproved` + `rating` (for review moderation)

---

## 7. DATA VALIDATION & CONSISTENCY

### 7.1 Data Validation Rules
- **User Email**: Must be valid email format, unique
- **User Phone**: Must be valid phone format, unique
- **Product Price**: Must be positive number
- **Product Inventory**: Cannot be negative
- **Order Total**: Must match sum of items + shipping + tax - discount
- **Review Rating**: Must be between 1 and 5

### 7.2 Data Consistency Checks
- When order is created, verify product inventory is available
- When order status changes to "shipped", update inventory
- When review is added, recalculate product average rating
- When promo code is used, increment usage count
- When cart item is added, verify product is still active

---

## 8. STORAGE STRUCTURE (Firebase Cloud Storage)

### 8.1 Product Images
- Path: `products/{productId}/images/{imageName}`
- Purpose: Store product photos
- Access: Public read, admin write

### 8.2 User Profile Pictures
- Path: `users/{userId}/profile/{photoName}`
- Purpose: Store user profile photos
- Access: User can read/write own, others can read

### 8.3 Review Images
- Path: `reviews/{reviewId}/images/{imageName}`
- Purpose: Store customer review photos
- Access: Public read, user can write own

### 8.4 Banner/Promotional Images
- Path: `banners/{bannerId}/{imageName}`
- Purpose: Store promotional banners
- Access: Public read, admin write

---

## 9. MVP DATA PRIORITY

### Phase 1 - Essential Collections (MVP Launch)
1. **users** - User accounts and profiles
2. **products** - Product catalog
3. **carts** - Shopping cart functionality
4. **orders** - Order management

### Phase 2 - Enhanced Collections (Post-MVP)
5. **wishlists** - Save for later
6. **reviews** - Product reviews and ratings
7. **promoCodes** - Discount codes

### Phase 3 - Advanced Collections (Future)
8. **notifications** - In-app notifications
9. **analytics** - User behavior tracking
10. **inventory** - Advanced inventory management

---

## 10. DATA MIGRATION & BACKUP

### 10.1 Backup Strategy
- Daily automatic backups of all collections
- Weekly full database export
- Monthly archive backups

### 10.2 Data Migration
- Plan for future schema changes
- Version control for data structure
- Migration scripts for data updates

---

**Version**: 1.0  
**Last Updated**: [Date]  
**Focus**: MVP Database Structure

