package ipp.estg.mobile.data.preferences

import android.content.Context
import android.content.SharedPreferences
import ipp.estg.mobile.utils.Constants

/**
 * Handles authentication-related preferences using [SharedPreferences].
 *
 * @constructor Creates an instance of [AuthPreferences] with the specified context.
 * @param context The application context used to access the shared preferences file.
 */
class AuthPreferences(context: Context) {
    private val authPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.AUTH_PREFERENCES_FILE, Context.MODE_PRIVATE)

    /**
     * Retrieves the stored user ID.
     *
     * @return The user ID as an [Int]. Defaults to -1 if no value is stored.
     */
    fun getUserId(): Int {
        return authPreferences.getInt("userId", -1)
    }

    /**
     * Stores the user ID in the shared preferences.
     *
     * @param userId The user ID to store.
     */
    fun setUserId(userId: Int) {
        authPreferences.edit().putInt("userId", userId).apply()
    }

    /**
     * Retrieves the stored authentication token.
     *
     * @return The authentication token as a [String], or `null` if no token is stored.
     */
    fun getAuthToken(): String? {
        return authPreferences.getString("authToken", null)
    }

    /**
     * Stores the authentication token in the shared preferences.
     *
     * @param authToken The authentication token to store.
     */
    fun setAuthToken(authToken: String) {
        authPreferences.edit().putString("authToken", authToken).apply()
    }

    /**
     * Retrieves the user's paid role status.
     *
     * @return `true` if the user has a paid role, `false` otherwise. Defaults to `false`.
     */
    fun getRolePaid(): Boolean {
        return authPreferences.getBoolean("rolePaid", false)
    }

    /**
     * Stores the user's paid role status in the shared preferences.
     *
     * @param rolePaid `true` if the user has a paid role, `false` otherwise.
     */
    fun setRolePaid(rolePaid: Boolean) {
        authPreferences.edit().putBoolean("rolePaid", rolePaid).apply()
    }

    /**
     * Clears all stored authentication-related preferences.
     */
    fun clear() {
        authPreferences.edit().clear().apply()
    }
}