package com.blackfoxis.tmdb.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val poster_path: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
)
data class MovieDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String?,

    @SerializedName("original_title")
    val originalTitle: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("genres")
    val genres: List<Genre>?,

    @SerializedName("runtime")
    val runtime: Int?, // в минутах

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    val voteCount: Int?,

    @SerializedName("tagline")
    val tagline: String?,

    @SerializedName("status")
    val status: String?, // Rumored, Planned, In Production, Post Production, Released, Canceled

    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>?,

    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>?,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>?,

    )
data class Genre(val id: Int, val name: String?)
data class ProductionCompany(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("logo_path") // Это должно точно совпадать с ключом в JSON
    val logoPath: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: String?
)data class ProductionCountry(val iso_3166_1: String?, val name: String?)
data class SpokenLanguage(val englishName: String?, val name: String?)
data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)