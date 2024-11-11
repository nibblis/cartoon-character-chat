package app.test.demochat.di

import app.test.demochat.BuildConfig
import app.test.demochat.data.api.ApiClient
import app.test.demochat.data.api.AuthenticatedApiClient
import app.test.demochat.data.interceptor.AuthInterceptor
import app.test.demochat.data.interceptor.TokenAuthenticator
import app.test.demochat.domain.network.ErrorHandler
import app.test.demochat.domain.network.ErrorHandlerImpl
import app.test.demochat.domain.network.SafeApiCall
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { Gson() }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(AuthenticatedApiClient::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }

    single<ErrorHandler> { ErrorHandlerImpl(get()) }
    single { SafeApiCall(get()) }
}