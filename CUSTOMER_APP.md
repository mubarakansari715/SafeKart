# 👤 Customer App Requirements

The Customer App is designed for end-users who browse products, make purchases, and track their orders.

---

## 📋 Table of Contents

- [User Flow](#-user-flow)
- [Modules](#-modules)
  - [Authentication](#-1-authentication)
  - [Home Screen](#-2-home-screen)
  - [Product Discovery](#-3-product-discovery)
  - [Product Details](#-4-product-details)
  - [Shopping Cart](#-5-shopping-cart)
  - [Checkout](#-6-checkout)
  - [Orders](#-7-orders)
  - [Wishlist](#-8-wishlist)
  - [Profile](#-9-profile)
  - [Notifications](#-10-notifications)
- [Screen Specifications](#-screen-specifications)

---

## 🔄 User Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         CUSTOMER APP FLOW                                    │
└─────────────────────────────────────────────────────────────────────────────┘

                              ┌──────────┐
                              │  Splash  │
                              └────┬─────┘
                                   │
                    ┌──────────────┼──────────────┐
                    ▼              ▼              ▼
              ┌──────────┐  ┌──────────┐  ┌──────────────┐
              │  Login   │  │  SignUp  │  │    Guest     │
              └────┬─────┘  └────┬─────┘  │   Continue   │
                   │             │        └──────┬───────┘
                   └─────────────┼───────────────┘
                                 ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                              MAIN APP                                       │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐          │
│  │  Home   │  │Categories│  │  Cart   │  │ Orders  │  │ Profile │          │
│  │   🏠    │  │    📂    │  │   🛒    │  │   📦    │  │   👤    │          │
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘  └─────────┘          │
└───────│────────────│────────────│────────────│─────────────────────────────┘
        │            │            │            │
        ▼            ▼            │            ▼
   ┌─────────┐  ┌─────────┐      │      ┌─────────────┐
   │ Search  │  │ Product │      │      │Order Detail │
   │ Results │  │  List   │      │      │  Tracking   │
   └────┬────┘  └────┬────┘      │      └─────────────┘
        │            │           │
        └─────┬──────┘           │
              ▼                  │
        ┌───────────┐            │
        │  Product  │            │
        │  Details  │────────────┤
        └───────────┘            │
              │                  │
              ▼                  ▼
        ┌─────────────────────────────┐
        │          CART               │
        └──────────────┬──────────────┘
                       ▼
        ┌─────────────────────────────┐
        │    CHECKOUT FLOW            │
        │  Address → Payment → Place  │
        └──────────────┬──────────────┘
                       ▼
        ┌─────────────────────────────┐
        │    ORDER CONFIRMATION       │
        └─────────────────────────────┘
```

---

## 📦 Modules

### 🔐 1. Authentication

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Splash Screen | App logo, loading animation | P0 |
| Onboarding | First-time user walkthrough (3-4 screens) | P1 |
| Sign Up | Register with email/phone | P0 |
| Sign In | Login with credentials | P0 |
| Social Login | Google, Facebook, Apple Sign-In | P1 |
| OTP Verification | Phone/Email verification | P0 |
| Forgot Password | Reset via email/OTP | P0 |
| Guest Mode | Browse without account | P1 |
| Biometric Login | Fingerprint/Face ID | P2 |

#### Sign Up Fields
```
- Full Name (required)
- Email Address (required)
- Phone Number (required)
- Password (required, min 8 chars)
- Confirm Password (required)
- Terms & Conditions (checkbox)
```

#### Sign In Fields
```
- Email/Phone (required)
- Password (required)
- Remember Me (checkbox)
- Forgot Password (link)
```

#### Validations
- Email format validation
- Phone number format (10 digits)
- Password strength (uppercase, lowercase, number, special char)
- OTP expiry (5 minutes)
- Maximum login attempts (5)

---

### 🏠 2. Home Screen

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Search Bar | Quick search with voice input | P0 |
| Banner Carousel | Promotional banners (auto-scroll) | P0 |
| Categories | Horizontal scrollable categories | P0 |
| Featured Products | Trending items section | P0 |
| New Arrivals | Recently added products | P1 |
| Flash Sale | Time-limited deals with countdown | P1 |
| Recommended | Personalized suggestions | P2 |
| Recently Viewed | Browsing history | P1 |

#### UI Components
```
┌─────────────────────────────────────────┐
│  🔍 Search products...            🔔 🛒 │
├─────────────────────────────────────────┤
│  ┌─────────────────────────────────┐    │
│  │                                 │    │
│  │      PROMOTIONAL BANNER         │    │
│  │         (Carousel)              │    │
│  │                                 │    │
│  └─────────────────────────────────┘    │
│         • • ○ ○ ○                       │
├─────────────────────────────────────────┤
│  Categories                    See All > │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐       │
│  │ 👕  │ │ 👟  │ │ 📱  │ │ 💄  │       │
│  │Cloth│ │Shoes│ │Tech │ │Beaut│       │
│  └─────┘ └─────┘ └─────┘ └─────┘       │
├─────────────────────────────────────────┤
│  ⚡ Flash Sale              02:45:30    │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │ [Image] │ │ [Image] │ │ [Image] │   │
│  │ ₹999    │ │ ₹1,499  │ │ ₹599    │   │
│  │ ₹1,999  │ │ ₹2,999  │ │ ₹1,199  │   │
│  └─────────┘ └─────────┘ └─────────┘   │
├─────────────────────────────────────────┤
│  Featured Products             See All > │
│  ┌─────────┐ ┌─────────┐               │
│  │ [Image] │ │ [Image] │               │
│  │ Title   │ │ Title   │               │
│  │ ⭐ 4.5  │ │ ⭐ 4.2  │               │
│  │ ₹1,299  │ │ ₹899    │               │
│  └─────────┘ └─────────┘               │
└─────────────────────────────────────────┘
│  🏠   📂   🛒   📦   👤  │
└─────────────────────────────────────────┘
```

---

### 🔍 3. Product Discovery

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Category Listing | Browse by category/subcategory | P0 |
| Search | Text search with suggestions | P0 |
| Voice Search | Speech-to-text search | P2 |
| Search History | Recent searches | P1 |
| Auto-suggestions | Real-time search hints | P1 |
| Filters | Multi-select filters | P0 |
| Sort Options | Various sorting criteria | P0 |
| View Toggle | Grid/List view | P1 |
| Infinite Scroll | Pagination with lazy loading | P0 |

#### Filter Options
```
□ Price Range
  ├── ₹0 - ₹500
  ├── ₹500 - ₹1,000
  ├── ₹1,000 - ₹2,500
  ├── ₹2,500 - ₹5,000
  └── ₹5,000+
  └── Custom Range [___] - [___]

□ Brand
  ├── □ Nike
  ├── □ Adidas
  ├── □ Puma
  └── □ More...

□ Rating
  ├── ○ 4★ & above
  ├── ○ 3★ & above
  └── ○ 2★ & above

□ Discount
  ├── □ 10% and above
  ├── □ 25% and above
  ├── □ 50% and above
  └── □ 70% and above

□ Size (for apparel)
  ├── □ XS  □ S  □ M
  ├── □ L   □ XL □ XXL
  └── □ Custom

□ Color
  ├── ⚫ ⚪ 🔴 🔵 🟢 🟡
  └── More colors...

□ Availability
  ├── □ In Stock
  └── □ Include Out of Stock
```

#### Sort Options
```
○ Relevance (Default)
○ Price: Low to High
○ Price: High to Low
○ Newest First
○ Popularity
○ Rating: High to Low
○ Discount: High to Low
```

---

### 📦 4. Product Details

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Image Gallery | Multiple images with zoom | P0 |
| Image Carousel | Swipe through images | P0 |
| 360° View | Rotate product view | P2 |
| Video | Product video | P2 |
| Product Title | Name and brand | P0 |
| Price | MRP, Selling price, Discount % | P0 |
| Variants | Size, Color selection | P0 |
| Quantity Selector | +/- quantity | P0 |
| Add to Cart | Add item to cart | P0 |
| Buy Now | Direct checkout | P1 |
| Add to Wishlist | Save for later | P0 |
| Share | Share product link | P1 |
| Description | Product details | P0 |
| Specifications | Technical specs | P0 |
| Reviews & Ratings | Customer feedback | P0 |
| Q&A | Product questions | P1 |
| Related Products | Similar items | P1 |
| Seller Info | Vendor details | P1 |
| Delivery Info | Estimated delivery, Pincode check | P0 |
| Return Policy | Return/Exchange info | P0 |

#### UI Layout
```
┌─────────────────────────────────────────┐
│  ←                              ♡    ⎙  │
├─────────────────────────────────────────┤
│  ┌─────────────────────────────────┐    │
│  │                                 │    │
│  │                                 │    │
│  │        PRODUCT IMAGE            │    │
│  │         (Zoomable)              │    │
│  │                                 │    │
│  └─────────────────────────────────┘    │
│         • • • ○ ○                       │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐       │
│  │thumb│ │thumb│ │thumb│ │thumb│       │
│  └─────┘ └─────┘ └─────┘ └─────┘       │
├─────────────────────────────────────────┤
│  Brand Name                             │
│  Product Title Goes Here - Full Name    │
│                                         │
│  ⭐ 4.5 (2,345 reviews)  |  5K+ bought │
│                                         │
│  ₹1,299  ₹2,499  48% OFF               │
│  inclusive of all taxes                 │
├─────────────────────────────────────────┤
│  Select Size                            │
│  ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐        │
│  │ S │ │ M │ │ L │ │XL │ │XXL│        │
│  └───┘ └───┘ └───┘ └───┘ └───┘        │
│                                         │
│  Select Color                           │
│  ⚫ 🔵 🔴 ⚪ 🟢                         │
├─────────────────────────────────────────┤
│  📍 Delivery                            │
│  Enter Pincode [______] [Check]         │
│  Free delivery by Mon, 15 Dec           │
├─────────────────────────────────────────┤
│  ─────────────────────────────────────  │
│  📋 Product Details              ▼      │
│  ─────────────────────────────────────  │
│  📏 Specifications               ▼      │
│  ─────────────────────────────────────  │
│  ⭐ Ratings & Reviews            ▼      │
│  ─────────────────────────────────────  │
│  ❓ Questions & Answers          ▼      │
│  ─────────────────────────────────────  │
├─────────────────────────────────────────┤
│  Similar Products                       │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │ [Image] │ │ [Image] │ │ [Image] │   │
│  │ ₹999    │ │ ₹1,199  │ │ ₹899    │   │
│  └─────────┘ └─────────┘ └─────────┘   │
├─────────────────────────────────────────┤
│  ┌─────────────────┐ ┌─────────────────┐│
│  │   Add to Cart   │ │    Buy Now      ││
│  └─────────────────┘ └─────────────────┘│
└─────────────────────────────────────────┘
```

---

### 🛒 5. Shopping Cart

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Cart Items List | All added products | P0 |
| Product Image | Thumbnail | P0 |
| Product Info | Name, variant, price | P0 |
| Quantity Selector | +/- buttons | P0 |
| Remove Item | Delete from cart | P0 |
| Save for Later | Move to wishlist | P1 |
| Price Breakdown | Subtotal, taxes, discounts | P0 |
| Coupon/Promo Code | Apply discount codes | P0 |
| Delivery Estimate | Expected delivery date | P1 |
| Empty Cart State | When no items | P0 |
| Continue Shopping | Navigate back | P0 |
| Proceed to Checkout | Go to checkout | P0 |

#### Price Summary
```
┌─────────────────────────────────────────┐
│  Price Details                          │
├─────────────────────────────────────────┤
│  Price (3 items)            ₹4,997      │
│  Discount                   - ₹1,200    │
│  Delivery Charges           + ₹40       │
│  ─────────────────────────────────────  │
│  Total Amount               ₹3,837      │
│                                         │
│  You will save ₹1,200 on this order     │
└─────────────────────────────────────────┘
```

#### Cart Item Card
```
┌─────────────────────────────────────────┐
│  ┌────────┐  Product Name Here          │
│  │        │  Size: M | Color: Blue      │
│  │ Image  │  Seller: VendorName         │
│  │        │                             │
│  └────────┘  ₹1,299  ₹2,499  48% off    │
│                                         │
│  Delivery by Mon, 15 Dec                │
│                                         │
│  ┌───┐ ┌───┐ ┌───┐                     │
│  │ - │ │ 1 │ │ + │    🗑️ Remove        │
│  └───┘ └───┘ └───┘    ♡ Save for Later │
└─────────────────────────────────────────┘
```

---

### 💳 6. Checkout

#### Flow
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                           CHECKOUT FLOW                                       │
│                                                                              │
│   ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐      │
│   │  Address   │───▶│  Delivery  │───▶│  Payment   │───▶│   Order    │      │
│   │ Selection  │    │  Options   │    │   Method   │    │  Summary   │      │
│   └────────────┘    └────────────┘    └────────────┘    └────────────┘      │
│        ●                 ○                 ○                 ○               │
└──────────────────────────────────────────────────────────────────────────────┘
```

#### Step 1: Address Selection

| Feature | Description | Priority |
|---------|-------------|----------|
| Saved Addresses | List of saved addresses | P0 |
| Add New Address | Form to add address | P0 |
| Edit Address | Modify existing | P0 |
| Delete Address | Remove address | P0 |
| Default Address | Set primary address | P1 |
| Address Type | Home, Work, Other | P0 |
| Location Picker | Map-based selection | P2 |

**Address Fields:**
```
- Full Name (required)
- Phone Number (required)
- Pincode (required)
- Address Line 1 (required)
- Address Line 2 (optional)
- Landmark (optional)
- City (auto-filled from pincode)
- State (auto-filled from pincode)
- Address Type: ○ Home ○ Work ○ Other
- □ Make this my default address
```

#### Step 2: Delivery Options

| Option | Description | Price |
|--------|-------------|-------|
| Standard Delivery | 5-7 business days | Free / ₹40 |
| Express Delivery | 2-3 business days | ₹99 |
| Same Day Delivery | Within 24 hours | ₹149 |
| Scheduled Delivery | Choose date/time | ₹49 |

#### Step 3: Payment Methods

| Method | Description | Priority |
|--------|-------------|----------|
| Credit/Debit Card | Visa, Mastercard, RuPay | P0 |
| UPI | Google Pay, PhonePe, Paytm | P0 |
| Net Banking | All major banks | P0 |
| Wallets | Paytm, Amazon Pay, etc. | P1 |
| Cash on Delivery | Pay at doorstep | P0 |
| EMI | No-cost EMI options | P1 |
| Pay Later | Buy now pay later | P2 |
| Saved Cards | Previously used cards | P1 |

#### Step 4: Order Summary

```
┌─────────────────────────────────────────┐
│  Order Summary                          │
├─────────────────────────────────────────┤
│  Delivering to:                         │
│  John Doe, 123 Main Street...    Change │
├─────────────────────────────────────────┤
│  3 Items                                │
│  ┌────────┐ Product 1          ₹1,299   │
│  └────────┘ Qty: 1                      │
│  ┌────────┐ Product 2          ₹999     │
│  └────────┘ Qty: 2                      │
├─────────────────────────────────────────┤
│  Price Details                          │
│  ─────────────────────────────────────  │
│  Subtotal                    ₹3,297     │
│  Discount (SAVE10)           - ₹330     │
│  Delivery                    + ₹0       │
│  ─────────────────────────────────────  │
│  Total                       ₹2,967     │
├─────────────────────────────────────────┤
│  Payment: UPI - Google Pay      Change  │
├─────────────────────────────────────────┤
│  ┌─────────────────────────────────────┐│
│  │         PLACE ORDER                 ││
│  │          ₹2,967                     ││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
```

---

### 📋 7. Orders

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Order History | List of all orders | P0 |
| Order Filters | By status, date | P1 |
| Order Search | Search by ID/product | P1 |
| Order Details | Full order information | P0 |
| Order Tracking | Real-time status | P0 |
| Track Shipment | Delivery partner tracking | P0 |
| Cancel Order | Before shipment | P0 |
| Return Request | Post-delivery | P0 |
| Exchange Request | Replace item | P1 |
| Refund Status | Track refunds | P0 |
| Reorder | Quick reorder | P1 |
| Invoice Download | PDF invoice | P0 |
| Rate Order | Post-delivery rating | P0 |
| Help | Order-specific support | P0 |

#### Order Status Flow
```
┌──────────────┐
│ Order Placed │
└──────┬───────┘
       ▼
┌──────────────┐
│  Confirmed   │
└──────┬───────┘
       ▼
┌──────────────┐
│  Processing  │
└──────┬───────┘
       ▼
┌──────────────┐
│   Shipped    │───────────┐
└──────┬───────┘           │
       ▼                   │
┌──────────────┐           │
│Out for       │           │
│Delivery      │           │
└──────┬───────┘           │
       ▼                   ▼
┌──────────────┐    ┌──────────────┐
│  Delivered   │    │  Cancelled   │
└──────────────┘    └──────────────┘
       │
       ▼
┌──────────────┐
│Return/Exchange│
│  (Optional)   │
└──────────────┘
```

#### Order Card
```
┌─────────────────────────────────────────┐
│  Order #ORD123456789                    │
│  Placed on 10 Dec 2024                  │
├─────────────────────────────────────────┤
│  ┌────────┐  Product Name               │
│  │ Image  │  Size: M | Qty: 1           │
│  └────────┘  ₹1,299                     │
│                                         │
│  🟢 Delivered on 15 Dec 2024            │
├─────────────────────────────────────────┤
│  ⭐ Rate Product    📄 View Details     │
└─────────────────────────────────────────┘
```

---

### ❤️ 8. Wishlist

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Wishlist Items | Saved products list | P0 |
| Add to Cart | Move to cart | P0 |
| Remove | Delete from wishlist | P0 |
| Price Alert | Notify on price drop | P2 |
| Stock Alert | Notify when back in stock | P1 |
| Share Wishlist | Share with friends | P2 |
| Multiple Lists | Create collections | P2 |
| Empty State | When no items saved | P0 |

---

### 👤 9. Profile

#### Features

| Feature | Description | Priority |
|---------|-------------|----------|
| View Profile | User info display | P0 |
| Edit Profile | Update name, email, phone | P0 |
| Profile Picture | Upload/change photo | P1 |
| My Addresses | Manage saved addresses | P0 |
| Saved Cards | Manage payment methods | P1 |
| My Orders | Quick access to orders | P0 |
| My Wishlist | Quick access to wishlist | P0 |
| My Reviews | Reviews written | P1 |
| Notifications | Manage preferences | P0 |
| Language | Change app language | P1 |
| Theme | Dark/Light mode | P1 |
| Help Center | FAQs, Contact us | P0 |
| About | App info, Legal | P0 |
| Logout | Sign out | P0 |
| Delete Account | Account deletion | P1 |

#### Profile Screen Layout
```
┌─────────────────────────────────────────┐
│          ┌─────────┐                    │
│          │  Photo  │                    │
│          └─────────┘                    │
│           John Doe                      │
│        john@email.com                   │
│         📱 9876543210                   │
│                              [Edit]     │
├─────────────────────────────────────────┤
│  📦 My Orders                        >  │
│  ❤️ My Wishlist                      >  │
│  📍 Manage Addresses                 >  │
│  💳 Saved Payment Methods            >  │
├─────────────────────────────────────────┤
│  🔔 Notification Settings            >  │
│  🌐 Language                         >  │
│  🎨 Theme                            >  │
├─────────────────────────────────────────┤
│  ❓ Help & Support                   >  │
│  ℹ️ About                            >  │
│  📜 Terms & Conditions               >  │
│  🔒 Privacy Policy                   >  │
├─────────────────────────────────────────┤
│  🚪 Logout                              │
└─────────────────────────────────────────┘
```

---

### 🔔 10. Notifications

#### Notification Types

| Type | Description | Priority |
|------|-------------|----------|
| Order Updates | Status changes | P0 |
| Delivery Updates | Shipping info | P0 |
| Promotional | Offers, Sales | P1 |
| Price Drop | Wishlist item price reduced | P1 |
| Back in Stock | Item available again | P1 |
| Reminders | Cart abandonment | P2 |
| Account | Security, Profile updates | P0 |

---

## 📱 Screen Specifications

### Screen List

| # | Screen Name | Route |
|---|-------------|-------|
| 1 | Splash | `/splash` |
| 2 | Onboarding | `/onboarding` |
| 3 | Login | `/auth/login` |
| 4 | Sign Up | `/auth/signup` |
| 5 | OTP Verification | `/auth/otp` |
| 6 | Forgot Password | `/auth/forgot-password` |
| 7 | Home | `/home` |
| 8 | Search | `/search` |
| 9 | Search Results | `/search/results` |
| 10 | Categories | `/categories` |
| 11 | Category Products | `/category/{id}` |
| 12 | Product List | `/products` |
| 13 | Product Details | `/product/{id}` |
| 14 | Reviews | `/product/{id}/reviews` |
| 15 | Cart | `/cart` |
| 16 | Apply Coupon | `/cart/coupon` |
| 17 | Checkout - Address | `/checkout/address` |
| 18 | Add/Edit Address | `/address/edit` |
| 19 | Checkout - Delivery | `/checkout/delivery` |
| 20 | Checkout - Payment | `/checkout/payment` |
| 21 | Order Summary | `/checkout/summary` |
| 22 | Order Confirmation | `/order/confirmation` |
| 23 | Orders List | `/orders` |
| 24 | Order Details | `/order/{id}` |
| 25 | Track Order | `/order/{id}/track` |
| 26 | Wishlist | `/wishlist` |
| 27 | Profile | `/profile` |
| 28 | Edit Profile | `/profile/edit` |
| 29 | Addresses | `/profile/addresses` |
| 30 | Saved Cards | `/profile/cards` |
| 31 | Notifications | `/notifications` |
| 32 | Settings | `/settings` |
| 33 | Help & Support | `/support` |

---

## ✅ Acceptance Criteria Checklist

### Authentication
- [ ] User can register with email/phone
- [ ] User can login with credentials
- [ ] User can reset password
- [ ] OTP verification works correctly
- [ ] Social login integration works
- [ ] Session persists after app restart

### Shopping Flow
- [ ] User can browse categories
- [ ] User can search and filter products
- [ ] User can view product details
- [ ] User can add/remove items from cart
- [ ] User can apply coupon codes
- [ ] User can complete checkout
- [ ] User receives order confirmation

### Order Management
- [ ] User can view order history
- [ ] User can track orders in real-time
- [ ] User can cancel orders
- [ ] User can request returns
- [ ] User can download invoices

### Profile
- [ ] User can update profile info
- [ ] User can manage addresses
- [ ] User can manage payment methods
- [ ] User can manage notification preferences

