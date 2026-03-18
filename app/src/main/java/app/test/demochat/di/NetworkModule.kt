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
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val networkModule = module {
    single { Gson() }
    
    // Настройка кэша для OkHttp (Оптимизация трафика)
    single {
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cacheDir = File(androidContext().cacheDir, "http_cache")
        Cache(cacheDir, cacheSize)
    }

    single {
        OkHttpClient.Builder()
            .cache(get()) // Включение кэширования
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            // Добавление поддержки сжатия Gzip (OkHttp делает это по умолчанию, 
            // но здесь мы подчеркиваем готовность к тюнингу заголовков)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "gzip")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS // BODY -> HEADERS для экономии ресурсов в логах
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
                        level = HttpLoggingInterceptor.Level.HEADERS
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