package ipp.estg.mobile.data.retrofit

import android.content.Context
import ipp.estg.mobile.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Intercepts HTTP requests to add an Authorization header with a bearer token.
 *
 * @constructor Creates an instance of [AuthInterceptor] using the provided context.
 * @param context The application context used to access the shared preferences file.
 */
class AuthInterceptor(context: Context) : Interceptor {

    private val sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFERENCES_FILE, Context.MODE_PRIVATE)

    /**
     * Intercepts an HTTP request, adding the "Authorization" header if a token exists.
     *
     * @param chain The interceptor chain used to process the request.
     * @return The HTTP response after the request is processed.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("authToken", null)
        val request = chain.request().newBuilder()

        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}