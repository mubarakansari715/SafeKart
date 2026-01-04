# Frontend Modules Plan - React Native Expo

## Overview
This document describes all frontend modules, their components, features, and how they integrate with Firebase database.

---

## MODULE 1: Authentication Module

### Purpose
Handle user registration, login, and authentication state management.

### Sub-Modules & Components

#### 1.1 Login Screen
**Components**:
- Login form container
- Email input field
- Password input field
- Phone number input field (for OTP)
- OTP input field (6 digits)
- "Remember Me" checkbox
- Login button
- Google login button
- "Forgot Password" link
- "Don't have account? Sign Up" link

**Firebase Integration**:
- **Firebase Auth**: `signInWithEmailAndPassword()` - Email login
- **Firebase Auth**: `signInWithPhoneNumber()` - Phone OTP login
- **Firebase Auth**: `signInWithCredential()` - Google login
- **Firestore**: Read `users/{userId}` after successful login
- **Firestore**: Update `lastLoginAt` in user document

**Features**:
- Email/password authentication
- Phone OTP authentication (SMS verification)
- Google Sign-In
- Form validation (email format, password strength)
- Error handling and display
- Loading states during authentication
- Auto-redirect after successful login

---

#### 1.2 Registration Screen
**Components**:
- Registration form container
- Full name input field
- Email input field
- Phone number input field
- Password input field
- Confirm password input field
- Terms & conditions checkbox
- Register button
- Google sign-up button
- "Already have account? Login" link

**Firebase Integration**:
- **Firebase Auth**: `createUserWithEmailAndPassword()` - Email signup
- **Firebase Auth**: `signInWithPhoneNumber()` - Phone signup
- **Firestore**: Create `users/{userId}` document with user data
- **Firestore**: Set initial user preferences
- **Firebase Auth**: Send email verification link

**Features**:
- Email/password registration
- Phone OTP registration
- Google Sign-Up
- Password strength indicator
- Email verification flow
- Phone verification (OTP)
- Form validation
- Duplicate account detection

---

#### 1.3 OTP Verification Screen
**Components**:
- OTP input container (6 digit boxes)
- Resend OTP button
- Verify button
- Timer countdown
- "Change phone number" link

**Firebase Integration**:
- **Firebase Auth**: `confirmationResult.confirm()` - Verify OTP code
- **Firestore**: Update `phoneVerified: true` in user document
- **Firebase Auth**: `signInWithPhoneNumber()` - Resend OTP

**Features**:
- OTP code input (auto-focus next box)
- Auto-verify on complete
- Resend OTP (with 60-second cooldown)
- Timer display
- Error handling for invalid OTP

---

#### 1.4 Forgot Password Screen
**Components**:
- Email input field
- Send reset link button
- Back to login link
- Success message display

**Firebase Integration**:
- **Firebase Auth**: `sendPasswordResetEmail()` - Send reset email

**Features**:
- Email validation
- Reset link sent confirmation
- Error handling

---

#### 1.5 Auth State Management
**Components**:
- Auth context provider
- Auth state listener
- Loading screen (while checking auth)

**Firebase Integration**:
- **Firebase Auth**: `onAuthStateChanged()` - Listen to auth state
- **Firestore**: Fetch user data when authenticated

**Features**:
- Auto-login if session exists
- Redirect to login if not authenticated
- Handle auth state changes
- Persist auth state

---

## MODULE 2: Home Screen Module

### Purpose
Display featured content, categories, and quick access to products.

### Sub-Modules & Components

#### 2.1 Home Screen
**Components**:
- Header component (logo, search icon, cart icon, profile icon)
- Banner carousel (promotional banners)
- Featured products section
- New arrivals section
- Categories grid
- Popular products section
- Footer navigation

**Firebase Integration**:
- **Firestore**: Query `products` collection where `isFeatured: true`
- **Firestore**: Query `products` collection where `isNew: true`, ordered by `createdAt`
- **Firestore**: Query `products` collection ordered by `rating.average`
- **Firestore**: Read categories from products (aggregate)
- **Storage**: Load banner images from `banners/` folder

**Features**:
- Swipeable banner carousel
- Horizontal scrolling product lists
- Quick product preview on tap
- Navigate to product detail
- Navigate to category page
- Pull to refresh
- Loading skeletons

---

#### 2.2 Category Grid Component
**Components**:
- Category card (image, name)
- Category grid layout

**Firebase Integration**:
- **Firestore**: Query `products` collection, group by `category`
- **Storage**: Load category images

**Features**:
- Display all categories
- Category image display
- Navigate to category products page

---

## MODULE 3: Product Browsing Module

### Purpose
Browse, search, filter, and view product details.

### Sub-Modules & Components

#### 3.1 Product Listing Screen
**Components**:
- Filter button
- Sort dropdown
- Product grid/list view toggle
- Product cards
- Load more button / Infinite scroll
- Empty state component
- Loading skeleton

**Firebase Integration**:
- **Firestore**: Query `products` collection with filters:
  - Filter by `category`, `subcategory`
  - Filter by `isActive: true`
  - Filter by price range
  - Filter by size/color (in variants)
- **Firestore**: Order by `price`, `createdAt`, `rating.average`
- **Firestore**: Pagination (limit, startAfter)
- **Storage**: Load product images

**Features**:
- Category-based filtering
- Price range slider
- Size filter (S, M, L, XL)
- Color filter
- Sort options (price, newest, rating)
- Grid/List view toggle
- Infinite scroll pagination
- Product card with image, name, price, rating

---

#### 3.2 Product Detail Screen
**Components**:
- Image gallery (swipeable, zoomable)
- Product name
- Price display (original & discounted)
- Size selector (buttons)
- Color selector (color swatches)
- Quantity selector (+/- buttons)
- Add to cart button
- Add to wishlist button
- Size chart modal
- Product description
- Material & care info
- Reviews section preview
- Related products section
- Share button

**Firebase Integration**:
- **Firestore**: Read `products/{productId}` document
- **Firestore**: Query `reviews` collection filtered by `productId` and `isApproved: true`
- **Firestore**: Query related products (same category, different productId)
- **Storage**: Load product images from `products/{productId}/images/`
- **Firestore**: Read `carts/{userId}` to check if product already in cart
- **Firestore**: Read `wishlists/{userId}` to check if product in wishlist

**Features**:
- Image gallery with zoom
- Size selection with availability check
- Color selection
- Quantity selection (max based on inventory)
- Add to cart functionality
- Add/remove from wishlist
- Size chart modal
- Reviews preview (show top 3)
- Related products carousel
- Share product link
- Out of stock handling

---

#### 3.3 Search Screen
**Components**:
- Search input field
- Search suggestions dropdown
- Recent searches list
- Popular searches list
- Search results grid
- No results state
- Filter button (in results)

**Firebase Integration**:
- **Firestore**: Query `products` collection:
  - Search in `name`, `description`, `tags` fields
  - Use Firestore text search or client-side filtering
- **Firestore**: Store recent searches in user document (optional)
- **Storage**: Load product images

**Features**:
- Real-time search as user types
- Search suggestions
- Recent searches (local storage)
- Popular searches
- Search results with filters
- Clear search
- Navigate to product from results

---

#### 3.4 Filter Modal
**Components**:
- Price range slider
- Size checkboxes
- Color checkboxes
- Category checkboxes
- Availability toggle
- Apply filters button
- Reset filters button
- Clear all button

**Firebase Integration**:
- **Firestore**: Query `products` with multiple filters:
  - `price` range
  - `variants.size` array contains
  - `variants.color` array contains
  - `category` equals
  - `isActive` equals

**Features**:
- Multiple filter selection
- Price range selection
- Size multi-select
- Color multi-select
- Apply/Reset filters
- Filter count badge

---

## MODULE 4: Shopping Cart Module

### Purpose
Manage items in shopping cart before checkout.

### Sub-Modules & Components

#### 4.1 Cart Screen
**Components**:
- Cart header
- Cart items list
- Cart item card (image, name, size, color, price, quantity controls, remove button)
- Promo code input field
- Apply promo button
- Subtotal display
- Shipping cost display
- Tax display
- Discount display
- Total amount display
- Continue shopping button
- Proceed to checkout button
- Empty cart state

**Firebase Integration**:
- **Firestore**: Read `carts/{userId}` document
- **Firestore**: Update `carts/{userId}` when quantity changes
- **Firestore**: Update `carts/{userId}` when item removed
- **Firestore**: Read `promoCodes` collection to validate promo code
- **Firestore**: Update `carts/{userId}` with promo code info
- **Firestore**: Read `products/{productId}` to get current price (if changed)
- **Storage**: Load product images

**Features**:
- Display all cart items
- Update quantity (+/- buttons)
- Remove item (with confirmation)
- Apply promo code
- Calculate subtotal, shipping, tax, total
- Show product images
- Show size/color selected
- Real-time price updates
- Empty cart state
- Continue shopping navigation
- Proceed to checkout navigation

---

#### 4.2 Add to Cart Flow
**Components**:
- Size selector (if not selected)
- Color selector (if not selected)
- Quantity selector
- Add to cart button
- Success toast notification

**Firebase Integration**:
- **Firestore**: Read `products/{productId}` to check inventory
- **Firestore**: Update `carts/{userId}` - add item to `items` array
- **Firestore**: Update `carts/{userId}` - recalculate totals
- **Firestore**: Update cart `updatedAt` timestamp

**Features**:
- Size selection (required)
- Color selection (required)
- Quantity selection
- Inventory validation
- Add to cart confirmation
- Cart badge update
- Navigate to cart option

---

## MODULE 5: Checkout Module

### Purpose
Complete purchase with shipping and payment information.

### Sub-Modules & Components

#### 5.1 Checkout Screen (Multi-step)
**Step 1 - Cart Review**:
- Cart items summary
- Edit cart button
- Continue button

**Step 2 - Shipping Address**:
- Saved addresses list (radio buttons)
- "Add new address" button
- Address form (if adding new)
- Shipping method selector (Standard/Express)
- Continue button

**Step 3 - Payment Method**:
- Saved payment methods list (radio buttons)
- "Add new card" button
- Payment form (card number, expiry, CVV, name)
- PayPal option
- Apple Pay button (iOS)
- Google Pay button (Android)
- Continue button

**Step 4 - Order Review**:
- Order summary
- Shipping address confirmation
- Payment method confirmation
- Terms & conditions checkbox
- Place order button

**Firebase Integration**:
- **Firestore**: Read `carts/{userId}` for cart items
- **Firestore**: Read `users/{userId}` for saved addresses
- **Firestore**: Read `users/{userId}` for saved payment methods
- **Firestore**: Create `orders/{orderId}` document
- **Firestore**: Update `products/{productId}` - decrease inventory
- **Firestore**: Clear `carts/{userId}` after order creation
- **Payment Gateway**: Process payment via Stripe/PayPal
- **Firestore**: Update `orders/{orderId}` with payment status
- **Cloud Functions**: Trigger order processing function

**Features**:
- Multi-step checkout flow
- Address selection/creation
- Payment method selection/creation
- Order summary review
- Secure payment processing
- Order confirmation
- Error handling
- Loading states

---

#### 5.2 Address Form Component
**Components**:
- Full name input
- Phone number input
- Address line 1 input
- Address line 2 input
- City input
- State/Province input
- Zip code input
- Country selector
- Set as default checkbox
- Save button

**Firebase Integration**:
- **Firestore**: Update `users/{userId}` - add to `addresses` array
- **Firestore**: Update `users/{userId}` - set `isDefault: true` if selected

**Features**:
- Form validation
- Address autocomplete (optional)
- Save address for future use
- Set default address

---

#### 5.3 Payment Form Component
**Components**:
- Card number input (formatted)
- Expiry date input (MM/YY)
- CVV input
- Cardholder name input
- Save card checkbox
- Pay button

**Firebase Integration**:
- **Payment Gateway**: Create payment method via Stripe/PayPal SDK
- **Firestore**: Update `users/{userId}` - add to `paymentMethods` array (only last4, brand, not full card)

**Features**:
- Card number formatting
- Expiry date formatting
- CVV validation
- Secure card input (no card data stored in Firebase)
- Save payment method option

---

#### 5.4 Order Confirmation Screen
**Components**:
- Success icon
- Order number display
- Order summary
- Estimated delivery date
- Continue shopping button
- Track order button
- Order details link

**Firebase Integration**:
- **Firestore**: Read `orders/{orderId}` to display order details
- **Cloud Functions**: Trigger order confirmation email

**Features**:
- Order number display
- Order summary
- Delivery estimate
- Navigation options
- Share order (optional)

---

## MODULE 6: Orders Module

### Purpose
View order history and track order status.

### Sub-Modules & Components

#### 6.1 Order History Screen
**Components**:
- Order list
- Order card (order number, date, status, total, items count)
- Filter by status dropdown
- Sort by date dropdown
- Empty orders state

**Firebase Integration**:
- **Firestore**: Query `orders` collection filtered by `userId`
- **Firestore**: Order by `createdAt` descending
- **Firestore**: Filter by `status` if selected
- **Storage**: Load product images from orders

**Features**:
- List all user orders
- Order status badges
- Order date display
- Total amount display
- Filter by status
- Sort by date
- Navigate to order details
- Empty state

---

#### 6.2 Order Details Screen
**Components**:
- Order header (order number, date, status)
- Order status timeline
- Order items list (with images)
- Shipping address display
- Billing address display
- Payment method display
- Order totals breakdown
- Tracking number (if shipped)
- Cancel order button (if pending)
- Reorder button
- Contact support button

**Firebase Integration**:
- **Firestore**: Read `orders/{orderId}` document
- **Firestore**: Update `orders/{orderId}` - change status to "cancelled" (if allowed)
- **Storage**: Load product images from order items

**Features**:
- Complete order information
- Status timeline visualization
- Order items with images
- Shipping/billing addresses
- Payment method display
- Tracking number with link
- Cancel order (if pending)
- Reorder functionality
- Contact support

---

#### 6.3 Order Tracking Screen
**Components**:
- Order status timeline
- Current status highlight
- Tracking number display
- Carrier name
- Estimated delivery date
- Map view (if available)
- Refresh button

**Firebase Integration**:
- **Firestore**: Read `orders/{orderId}` - get `status`, `trackingNumber`, `carrier`
- **Firestore**: Listen to `orders/{orderId}` for status updates

**Features**:
- Visual status timeline
- Current status indicator
- Tracking number display
- Carrier link
- Delivery estimate
- Real-time status updates
- Map integration (optional)

---

## MODULE 7: Wishlist Module

### Purpose
Save favorite products for later purchase.

### Sub-Modules & Components

#### 7.1 Wishlist Screen
**Components**:
- Wishlist header
- Wishlist items grid
- Product card (image, name, price, remove button, add to cart button)
- Empty wishlist state
- Share wishlist button

**Firebase Integration**:
- **Firestore**: Read `wishlists/{userId}` document
- **Firestore**: Read `products/{productId}` for each wishlist item (to get current price)
- **Firestore**: Update `wishlists/{userId}` - remove item from `items` array
- **Storage**: Load product images

**Features**:
- Display all wishlist items
- Remove from wishlist
- Add to cart from wishlist
- Price display (with price drop indicator)
- Empty wishlist state
- Share wishlist (optional)

---

#### 7.2 Add to Wishlist Flow
**Components**:
- Heart icon button (on product card/detail)
- Success toast notification

**Firebase Integration**:
- **Firestore**: Update `wishlists/{userId}` - add `productId` to `items` array
- **Firestore**: Update `wishlists/{userId}` - update `updatedAt` timestamp

**Features**:
- Toggle wishlist (add/remove)
- Visual feedback (filled/outline heart)
- Toast notification
- Wishlist count badge update

---

## MODULE 8: Profile Module

### Purpose
Manage user account, addresses, and preferences.

### Sub-Modules & Components

#### 8.1 Profile Screen
**Components**:
- Profile header (photo, name, email)
- Edit profile button
- Menu items:
  - My Orders
  - My Addresses
  - Payment Methods
  - My Reviews
  - Wishlist
  - Notifications
  - Settings
  - Help & Support
  - Logout

**Firebase Integration**:
- **Firestore**: Read `users/{userId}` document
- **Storage**: Load profile picture

**Features**:
- Display user information
- Navigation to sub-sections
- Profile picture display
- Logout functionality

---

#### 8.2 Edit Profile Screen
**Components**:
- Profile picture (with edit button)
- Full name input
- Email input (read-only or editable)
- Phone number input
- Save button
- Change password button

**Firebase Integration**:
- **Firestore**: Update `users/{userId}` - update `displayName`, `phoneNumber`
- **Storage**: Upload new profile picture to `users/{userId}/profile/`
- **Firestore**: Update `users/{userId}` - update `photoURL`
- **Firebase Auth**: Update email/phone in Auth

**Features**:
- Edit personal information
- Upload profile picture
- Change password flow
- Form validation
- Save changes

---

#### 8.3 Addresses Management Screen
**Components**:
- Addresses list
- Address card (full address, default badge, edit/delete buttons)
- Add new address button
- Empty addresses state

**Firebase Integration**:
- **Firestore**: Read `users/{userId}` - read `addresses` array
- **Firestore**: Update `users/{userId}` - add/edit/delete addresses
- **Firestore**: Update `users/{userId}` - set `isDefault: true` for one address

**Features**:
- List all saved addresses
- Add new address
- Edit address
- Delete address
- Set default address
- Address validation

---

#### 8.4 Payment Methods Screen
**Components**:
- Payment methods list
- Payment method card (type, last4, expiry, default badge, delete button)
- Add new card button
- Add PayPal button
- Empty payment methods state

**Firebase Integration**:
- **Firestore**: Read `users/{userId}` - read `paymentMethods` array
- **Payment Gateway**: Add new payment method via SDK
- **Firestore**: Update `users/{userId}` - add payment method (last4, brand only)
- **Firestore**: Update `users/{userId}` - delete payment method
- **Firestore**: Update `users/{userId}` - set `isDefault: true`

**Features**:
- List saved payment methods
- Add new card/PayPal
- Delete payment method
- Set default payment method
- Secure payment method storage

---

## MODULE 9: Reviews Module

### Purpose
View and write product reviews.

### Sub-Modules & Components

#### 9.1 Product Reviews Screen
**Components**:
- Reviews header (average rating, total reviews)
- Rating filter (1-5 stars)
- Sort dropdown (newest, helpful, rating)
- Reviews list
- Review card (rating, title, comment, images, reviewer name, date, verified badge, helpful button)
- Write review button (if purchased)
- Load more reviews button

**Firebase Integration**:
- **Firestore**: Query `reviews` collection filtered by `productId` and `isApproved: true`
- **Firestore**: Order by `createdAt` or `rating` or `isHelpful`
- **Firestore**: Update `reviews/{reviewId}` - increment `isHelpful` count
- **Storage**: Load review images

**Features**:
- Display all reviews
- Filter by rating
- Sort reviews
- Helpful vote
- Verified purchase badge
- Review images
- Write review (if purchased)

---

#### 9.2 Write Review Screen
**Components**:
- Product card (image, name)
- Star rating selector (1-5)
- Review title input
- Review text input
- Photo upload (multiple)
- Submit review button
- Cancel button

**Firebase Integration**:
- **Firestore**: Read `orders` collection - verify user purchased product
- **Storage**: Upload review images to `reviews/{reviewId}/images/`
- **Firestore**: Create `reviews/{reviewId}` document
- **Firestore**: Update `products/{productId}` - recalculate `rating.average` and `rating.count`
- **Cloud Functions**: Trigger review moderation

**Features**:
- Star rating selection
- Review text input
- Photo upload (up to 5 images)
- Form validation
- Submit review
- Only for purchased products

---

## MODULE 10: Navigation Module

### Purpose
App navigation structure and routing.

### Sub-Modules & Components

#### 10.1 Bottom Tab Navigation
**Tabs**:
- Home (home icon)
- Categories (grid icon)
- Cart (cart icon with badge)
- Wishlist (heart icon with badge)
- Profile (user icon)

**Features**:
- Tab bar at bottom
- Badge counts (cart items, wishlist items)
- Active tab indicator
- Navigate between main sections

---

#### 10.2 Stack Navigation
**Stacks**:
- Auth Stack (Login, Register, OTP, Forgot Password)
- Home Stack (Home, Product Detail, Search)
- Cart Stack (Cart, Checkout)
- Orders Stack (Order History, Order Details, Tracking)
- Profile Stack (Profile, Edit Profile, Addresses, Payment Methods, Settings)

**Features**:
- Screen transitions
- Back button handling
- Header customization per screen
- Deep linking support

---

#### 10.3 Header Component
**Components**:
- Logo/Brand name
- Search icon button
- Cart icon button (with badge)
- Profile icon button
- Back button (when in stack)

**Firebase Integration**:
- **Firestore**: Read `carts/{userId}` - get item count for badge
- **Firestore**: Read `wishlists/{userId}` - get item count for badge

**Features**:
- Consistent header across screens
- Badge counts
- Navigation actions
- Search access

---

## MODULE 11: Common UI Components

### Purpose
Reusable UI components used across modules.

### Components

#### 11.1 Buttons
- Primary button (filled, main actions)
- Secondary button (outlined, secondary actions)
- Text button (text only, minimal actions)
- Icon button (icon only)

#### 11.2 Input Fields
- Text input
- Email input (with validation)
- Password input (with show/hide)
- Phone input (with formatting)
- Number input
- Search input

#### 11.3 Product Card
- Product image
- Product name
- Price (original & discounted)
- Rating stars
- Add to cart button
- Add to wishlist button

#### 11.4 Loading Components
- Loading spinner
- Skeleton loaders (for products, lists)
- Full screen loader

#### 11.5 Empty States
- Empty cart state
- Empty wishlist state
- Empty orders state
- No results state
- No internet state

#### 11.6 Modals
- Size chart modal
- Image zoom modal
- Confirmation modal
- Filter modal

#### 11.7 Toast Notifications
- Success toast
- Error toast
- Info toast
- Warning toast

---

## MODULE 12: State Management

### Purpose
Manage app-wide state and data.

### Sub-Modules

#### 12.1 Auth State
- Current user object
- Authentication status
- Login/logout actions

**Firebase Integration**:
- **Firebase Auth**: `onAuthStateChanged()` listener
- **Firestore**: Fetch user data when authenticated

#### 12.2 Cart State
- Cart items array
- Cart totals
- Cart loading state

**Firebase Integration**:
- **Firestore**: Read `carts/{userId}` on app start
- **Firestore**: Listen to `carts/{userId}` for real-time updates

#### 12.3 Wishlist State
- Wishlist items array
- Wishlist loading state

**Firebase Integration**:
- **Firestore**: Read `wishlists/{userId}` on app start
- **Firestore**: Listen to `wishlists/{userId}` for updates

#### 12.4 Products State
- Products list (cached)
- Current product
- Filters state
- Search results

**Firebase Integration**:
- **Firestore**: Query `products` collection
- **Firestore**: Cache products locally
- **Firestore**: Listen to product updates

---

## MODULE 13: Settings Module

### Purpose
App settings and preferences.

### Sub-Modules & Components

#### 13.1 Settings Screen
**Components**:
- Notification settings toggle
- Email notifications toggle
- Push notifications toggle
- Language selector
- Theme selector (light/dark)
- About section
- Terms & Conditions link
- Privacy Policy link
- Return Policy link
- Contact Us
- Logout button

**Firebase Integration**:
- **Firestore**: Read `users/{userId}` - read `preferences`
- **Firestore**: Update `users/{userId}` - update `preferences`

**Features**:
- Toggle notifications
- Change language
- Change theme
- View policies
- Contact support
- Logout

---

## Module Summary & Firebase Integration Map

| Module | Firebase Collections Used | Firebase Services Used |
|--------|-------------------------|----------------------|
| Authentication | `users` | Auth, Firestore |
| Home | `products` | Firestore, Storage |
| Product Browsing | `products`, `reviews` | Firestore, Storage |
| Cart | `carts`, `products`, `promoCodes` | Firestore, Storage |
| Checkout | `orders`, `users`, `products`, `carts` | Firestore, Auth, Payment Gateway |
| Orders | `orders`, `products` | Firestore, Storage |
| Wishlist | `wishlists`, `products` | Firestore, Storage |
| Profile | `users` | Firestore, Storage, Auth |
| Reviews | `reviews`, `products`, `orders` | Firestore, Storage |
| Navigation | `carts`, `wishlists` | Firestore |
| Settings | `users` | Firestore |

---

**Version**: 1.0  
**Last Updated**: [Date]  
**Focus**: Frontend Module Structure & Firebase Integration

