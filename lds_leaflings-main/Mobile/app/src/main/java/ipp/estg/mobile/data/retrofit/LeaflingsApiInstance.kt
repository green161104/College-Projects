package ipp.estg.mobile.data.retrofit

import android.content.Context
import ipp.estg.mobile.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides a singleton instance of [LeaflingsApi].
 */
class LeaflingsApiInstance {
    companion object {

//        private val loggerInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }

        // útil para fazer chamadas HTTP
//        private val client = OkHttpClient.Builder()
//            .addInterceptor(loggerInterceptor)
//            .protocols(listOf(Protocol.HTTP_1_1))
//            .build()

//        private val client : OkHttpClient = OkHttpClient.Builder().apply {
//            addInterceptor(loggerInterceptor)
//            protocols(listOf(Protocol.HTTP_1_1))
//        }.build()


        private var retrofit: Retrofit? = null

        /**
         * Returns an instance of [LeaflingsApi]. Creates a new instance if one does not already exist.
         *
         * @param context The application context used to create the [AuthInterceptor].
         * @return An implementation of the [LeaflingsApi] interface.
         */
        fun getLeaflingsApi(context: Context): LeaflingsApi {
            val LEAFLINGS_API_BASE_URL = BuildConfig.LEAFLINGS_API_BASE_URL

            if (retrofit == null) {
                val client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(context)) // Add auth AuthInterceptor
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(LEAFLINGS_API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(LeaflingsApi::class.java)
        }
    }
}