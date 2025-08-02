package com.blackfoxis.tmdb.di

import com.blackfoxis.tmdb.Constants
import com.blackfoxis.tmdb.network.MovieApi
import com.blackfoxis.tmdb.network.MovieApiService
import com.blackfoxis.tmdb.repository.MovieRepository
import com.blackfoxis.tmdb.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // Зависимости будут жить как синглтоны на уровне приложения
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        return MovieApi.retrofitService
    }
    @Provides
    @Singleton
    fun provideLanguageProvider(): LanguageProvider {
        return DefaultLanguageProvider() // Hilt создаст и предоставит экземпляр
    }
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        @Named("TMDB_API_KEY") apiKey: String, // <--- Запрашиваем apiKey
        languageProvider: LanguageProvider
    ): MovieRepository {
        // Передаем apiKey в конструктор MovieRepositoryImpl
        return MovieRepositoryImpl(movieApiService, apiKey, languageProvider)
    }
    @Provides
    @Singleton
    @Named("TMDB_API_KEY")
    fun provideApiKey(): String {
        return Constants.API_KEY // Или лучше из BuildConfig
    }
}