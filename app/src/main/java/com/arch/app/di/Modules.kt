package com.arch.app.di

import com.arch.app.BuildConfig
import com.arch.app.api.auth.AuthApi
import com.arch.app.api.story.StoryApi
import com.arch.app.common.navigation.Navigation
import com.arch.app.navigation.NavigationImpl
import com.arch.app.network.AuthInterceptor
import com.arch.app.network.NetworkAuthorizationProvider
import com.arch.app.network.NetworkAuthorizationProvider.Companion.proceedWithDefHeader
import com.arch.app.network.Rfc3339ZonedDateTimeJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

private const val DEF_TIMEOUT = 60L
private const val TOKENIZE_RETROFIT = "tokenizeRetrofit"
private const val TOKENIZE_CLIENT = "tokenizeClient"

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        // TODO: Have to be implemented through credentials ESP/SP.
        NetworkAuthorizationProvider("tokenHasToBeReplacedWithCredentialsESP", get())
    }

    single(named(TOKENIZE_CLIENT)) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                chain.proceedWithDefHeader()
            }
            .build()
    }

    single {
        OkHttpClient.Builder()
            .readTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(AuthInterceptor(get(), androidContext()))
            .build()
    }

    single {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(ZonedDateTime::class.java, Rfc3339ZonedDateTimeJsonAdapter())
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single(named(TOKENIZE_RETROFIT)) {
        Retrofit.Builder()
            .client(get(named(TOKENIZE_CLIENT)))
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>(named(TOKENIZE_RETROFIT)).create(AuthApi::class.java) }
    single { get<Retrofit>().create(StoryApi::class.java) }

}
val mainModule = module {
    single<Navigation> {
        // TODO:  userHasValidCredentials can be replaced with ESP/SP.
        NavigationImpl(true)
    }
}
