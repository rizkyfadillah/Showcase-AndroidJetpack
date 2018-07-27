package com.rizkyfadillah.popularmovies.movie.common.di

import com.rizkyfadillah.popularmovies.Constants
import com.rizkyfadillah.popularmovies.movie.common.api.MovieDBService
import com.rizkyfadillah.popularmovies.movie.common.api.MovieDBServiceInterceptor
import com.rizkyfadillah.popularmovies.movie.common.repository.MovieRepository
import com.rizkyfadillah.popularmovies.movie.common.viewmodel.MovieViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rizky on 18/04/18.
 */
@Module
class MovieModule {

    @MovieScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(MovieDBServiceInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @MovieScope
    @Provides
    fun provideMovieRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_MOVIE_DB)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @MovieScope
    @Provides
    fun provideMovieDBService(restAdapter: Retrofit): MovieDBService {
        return restAdapter.create(MovieDBService::class.java)
    }

    @MovieScope
    @Provides
    fun provideMovieDetailViewModelFactory(movieRepository: MovieRepository): MovieViewModelFactory {
        return MovieViewModelFactory(movieRepository)
    }

}