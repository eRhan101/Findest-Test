package com.example.findesttest.di

import com.example.findesttest.ui.cart.CartViewModel
import com.example.findesttest.ui.checkout.CheckoutViewModel
import com.example.findesttest.ui.detail.DetailViewModel
import com.example.findesttest.ui.home.HomeViewModel
import com.example.findesttest.ui.order.OrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        DetailViewModel(get(), get())
    }
    viewModel {
        CartViewModel(get())
    }
    viewModel {
        CheckoutViewModel(get(), get())
    }
    viewModel {
        OrderViewModel(get())
    }
}