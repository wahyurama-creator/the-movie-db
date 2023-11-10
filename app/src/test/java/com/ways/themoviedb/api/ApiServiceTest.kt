package com.ways.themoviedb.api

import com.google.common.truth.Truth
import com.ways.themoviedb.BuildConfig
import com.ways.themoviedb.EnqueueTest
import com.ways.themoviedb.data.remote.response.movie.ProductionCompanies
import com.ways.themoviedb.data.remote.response.movie.ProductionCountries
import com.ways.themoviedb.data.remote.response.movie.SpokenLanguage
import com.ways.themoviedb.data.remote.response.review.AuthorDetails
import com.ways.themoviedb.data.remote.services.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class ApiServiceTest {

    private lateinit var apiService: ApiService
    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()

        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer ${BuildConfig.TOKEN}")
                    .build()
                chain.proceed(newRequest)
            })
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun getNowPlaying_receivedResponse_correctPagedSize_20() {
        runBlocking {
            EnqueueTest.enqueueMockResponse("now_playing.json", mockServer)
            val responseBody = apiService.getMovieList().body()
            val movieList = responseBody?.results?.size ?: 0

            Truth.assertThat(movieList).isEqualTo(20)
        }
    }

    @Test
    fun getNowPlaying_receivedResponse_correctFirstMovie() {
        runBlocking {
            EnqueueTest.enqueueMockResponse("now_playing.json", mockServer)
            val responseBody = apiService.getMovieList().body()
            val movie = responseBody?.results?.first() ?: error("Movie is not find")

            Truth.assertThat(movie.id).isEqualTo(507089)
            Truth.assertThat(movie.adult).isEqualTo(false)
            Truth.assertThat(movie.backdropPath).isEqualTo("/t5zCBSB5xMDKcDqe91qahCOUYVV.jpg")
            Truth.assertThat(movie.genreIds).isEqualTo(listOf(27, 9648))
            Truth.assertThat(movie.originalLanguage).isEqualTo("en")
            Truth.assertThat(movie.originalTitle).isEqualTo("Five Nights at Freddy's")
            Truth.assertThat(movie.overview)
                .isEqualTo("Recently fired and desperate for work, a troubled young man named Mike agrees to take a position as a night security guard at an abandoned theme restaurant: Freddy Fazbear's Pizzeria. But he soon discovers that nothing at Freddy's is what it seems.")
            Truth.assertThat(movie.popularity).isEqualTo(2653.844)
            Truth.assertThat(movie.posterPath).isEqualTo("/A4j8S6moJS2zNtRR8oWF08gRnL5.jpg")
            Truth.assertThat(movie.releaseDate).isEqualTo("2023-10-25")
            Truth.assertThat(movie.title).isEqualTo("Five Nights at Freddy's")
            Truth.assertThat(movie.video).isEqualTo(false)
            Truth.assertThat(movie.voteAverage).isEqualTo(8.179)
            Truth.assertThat(movie.voteCount).isEqualTo(1745)
        }
    }

    @Test
    fun getMovieDetail_receivedResponse_correctMovieDetail() {
        runBlocking {
            EnqueueTest.enqueueMockResponse("movie_detail.json", mockServer)
            val responseBody = apiService.getMovieDetail(507089)
            val movie = responseBody.body() ?: error("Movie is not find")

            Truth.assertThat(movie.id).isEqualTo(507089)
            Truth.assertThat(movie.adult).isEqualTo(false)
            Truth.assertThat(movie.backdropPath).isEqualTo("/7NRGAtu8E4343NSKwhkgmVRDINw.jpg")
            Truth.assertThat(movie.originalLanguage).isEqualTo("en")
            Truth.assertThat(movie.originalTitle).isEqualTo("Five Nights at Freddy's")
            Truth.assertThat(movie.overview)
                .isEqualTo("Recently fired and desperate for work, a troubled young man named Mike agrees to take a position as a night security guard at an abandoned theme restaurant: Freddy Fazbear's Pizzeria. But he soon discovers that nothing at Freddy's is what it seems.")
            Truth.assertThat(movie.popularity).isEqualTo(2058.036)
            Truth.assertThat(movie.posterPath).isEqualTo("/j9mH1pr3IahtraTWxVEMANmPSGR.jpg")
            Truth.assertThat(movie.releaseDate).isEqualTo("2023-10-25")
            Truth.assertThat(movie.title).isEqualTo("Five Nights at Freddy's")
            Truth.assertThat(movie.video).isEqualTo(false)
            Truth.assertThat(movie.voteAverage).isEqualTo(8.125)
            Truth.assertThat(movie.voteCount).isEqualTo(1847)

            Truth.assertThat(movie.homepage).isEqualTo("https://www.fivenightsatfreddys.movie")
            Truth.assertThat(movie.productionCompanies).isEqualTo(
                listOf(
                    ProductionCompanies(
                        id = 3172,
                        logoPath = "/kDedjRZwO8uyFhuHamomOhN6fzG.png",
                        name = "Blumhouse Productions",
                        originCountry = "US"
                    ),
                    ProductionCompanies(
                        id = 211144,
                        logoPath = null,
                        name = "Scott Cawthon Productions",
                        originCountry = "US"
                    ),
                )
            )
            Truth.assertThat(movie.productionCountries).isEqualTo(
                listOf(
                    ProductionCountries(iso31661 = "US", name = "United States of America")
                )
            )
            Truth.assertThat(movie.spokenLanguages).isEqualTo(
                listOf(
                    SpokenLanguage(englishName = "English", iso6391 = "en", name = "English")
                )
            )
            Truth.assertThat(movie.tagline).isEqualTo("Can you survive five nights?")
        }
    }

    @Test
    fun getMovieReview_receivedResponse_correctReviews() {
        runBlocking {
            EnqueueTest.enqueueMockResponse("movie_review.json", mockServer)
            val responseBody = apiService.getMovieReview(507089).body()
            val reviews = responseBody?.results ?: error("Review is not find")
            val review = reviews.first()

            Truth.assertThat(reviews.size).isEqualTo(3)

            Truth.assertThat(review.id).isEqualTo("653a4ad08a0e9b010b29016c")
            Truth.assertThat(review.author).isEqualTo("bradley")
            Truth.assertThat(review.authorDetails).isEqualTo(
                AuthorDetails(
                    avatarPath = "/yyyRXn3sLTq9NTL4sNpJ2gJAcBe.png",
                    name = "bradley",
                    rating = 9,
                    username = "ivebeenspringlocked"
                )
            )
            Truth.assertThat(review.content)
                .isEqualTo("FNAF was great, Kinda wish it had blood, Every kill was offscreen or really dark and you cant really see the kill.")
            Truth.assertThat(review.createdAt).isEqualTo("2023-10-26T11:17:37.038Z")
            Truth.assertThat(review.updatedAt).isEqualTo("2023-11-01T16:19:28.585Z")
            Truth.assertThat(review.url)
                .isEqualTo("https://www.themoviedb.org/review/653a4ad08a0e9b010b29016c")
        }
    }

    @Test
    fun getMovieVideo_receivedResponse_correctVideo() {
        runBlocking {
            EnqueueTest.enqueueMockResponse("movie_video.json", mockServer)
            val responseBody = apiService.getMovieVideo(507089).body()
            val videos = responseBody?.results ?: error("Video is not find")
            val video = videos.first()

            Truth.assertThat(videos.size).isEqualTo(7)

            Truth.assertThat(video.id).isEqualTo("65428c2ce1ad79012c90bfd9")
            Truth.assertThat(video.iso6391).isEqualTo("en")
            Truth.assertThat(video.iso31661).isEqualTo("US")
            Truth.assertThat(video.name).isEqualTo("My Universal Story: Emily Poulliard")
            Truth.assertThat(video.key).isEqualTo("GYOQBfT8UU4")
            Truth.assertThat(video.site).isEqualTo("YouTube")
            Truth.assertThat(video.size).isEqualTo(1080)
            Truth.assertThat(video.type).isEqualTo("Featurette")
            Truth.assertThat(video.official).isEqualTo(true)
            Truth.assertThat(video.publishedAt).isEqualTo("2023-10-27T22:04:15.000Z")
        }
    }

}