package com.example.findesttest.di

import com.example.findesttest.ui.detail.DetailViewModel
import com.example.findesttest.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(productRepository = get())
    }
    viewModel {
        DetailViewModel(productRepository = get())
    }
}