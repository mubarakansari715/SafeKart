package com.safekart.safekart.data.remote.api

import com.safekart.safekart.data.model.AddCartItemRequest
import com.safekart.safekart.data.model.AddressDto
import com.safekart.safekart.data.model.ApiResponse
import com.safekart.safekart.data.model.CartResponse
import com.safekart.safekart.data.model.OrderDto
import com.safekart.safekart.data.model.PlaceOrderRequest
import com.safekart.safekart.data.model.SaveAddressRequest
import com.safekart.safekart.data.model.UpdateCartItemRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApiService {

    // Cart
    @GET("customer/cart")
    suspend fun getCart(): ApiResponse<CartResponse>

    @POST("customer/cart")
    suspend fun addToCart(@Body body: AddCartItemRequest): ApiResponse<CartResponse>

    @PUT("customer/cart")
    suspend fun updateCartItem(@Body body: UpdateCartItemRequest): ApiResponse<CartResponse>

    @DELETE("customer/cart/{productId}")
    suspend fun removeCartItem(@Path("productId") productId: String): ApiResponse<CartResponse>

    // Addresses
    @GET("customer/addresses")
    suspend fun getAddresses(): ApiResponse<List<AddressDto>>

    @POST("customer/addresses")
    suspend fun addAddress(@Body body: SaveAddressRequest): ApiResponse<AddressDto>

    @DELETE("customer/addresses/{id}")
    suspend fun deleteAddress(@Path("id") id: String): ApiResponse<Unit>

    // Orders
    @GET("customer/orders")
    suspend fun getOrders(): ApiResponse<List<OrderDto>>

    @GET("customer/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): ApiResponse<OrderDto>

    @POST("customer/orders")
    suspend fun placeOrder(@Body body: PlaceOrderRequest): ApiResponse<OrderDto>
}
