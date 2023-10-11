package dev.davron.regionaltaxidriver.di

import android.annotation.SuppressLint
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.davron.regionaltaxidriver.BuildConfig
import dev.davron.regionaltaxidriver.apiService.ApiService
import dev.davron.regionaltaxidriver.apiService.StatisticsService
import dev.davron.regionaltaxidriver.db.Database
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.Urls
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl() = Urls.DRIVER_URL

    @Provides
    @Singleton
    fun provideStatistics(client: OkHttpClient) =
        Retrofit.Builder().baseUrl(Urls.DRIVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(StatisticsService::class.java)

    @SuppressLint("SuspiciousIndentation")
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)

        val chuckerInterceptor = ChuckerInterceptor.Builder(Common.globalContext)
            .maxContentLength(500_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
            builder.addInterceptor(chuckerInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRequestsDao() = Database.getDatabase(Common.globalContext).apiRequestsDao()
}