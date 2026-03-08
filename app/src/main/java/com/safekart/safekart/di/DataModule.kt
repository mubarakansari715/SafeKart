package com.safekart.safekart.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.safekart.safekart.data.repository.AddressRepositoryImpl
import com.safekart.safekart.data.repository.AuthRepositoryImpl
import com.safekart.safekart.data.repository.CartRepositoryImpl
import com.safekart.safekart.data.repository.HomeRepositoryImpl
import com.safekart.safekart.data.repository.OrderRepositoryImpl
import com.safekart.safekart.data.repository.UserRepositoryImpl
import com.safekart.safekart.domain.repository.AddressRepository
import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.repository.CartRepository
import com.safekart.safekart.domain.repository.HomeRepository
import com.safekart.safekart.domain.repository.OrderRepository
import com.safekart.safekart.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds @Singleton
    abstract fun bindAddressRepository(impl: AddressRepositoryImpl): AddressRepository

    @Binds @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("safekart_prefs", Context.MODE_PRIVATE)

    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
