package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.repositories.AuthRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.register.RegisterRequest
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for handling user authentication.
 *
 * @constructor Creates an instance of [AuthViewModel].
 * @param application The application context.
 */
class AuthViewModel(
    application: Application,
) : AndroidViewModel(application) {

    /**
     * Repository responsible for handling authentication requests.
     */
    private val authRepository = AuthRepository(
        LeaflingsApiInstance.getLeaflingsApi(application),
        AuthPreferences(application)
    )

    /**
     * Holds the _error.value message.
     */
    private val _error = MutableStateFlow("")
    var error = _error.asStateFlow()
        private set

    /**
     * Indicates if a process is running.
     */
    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()
        private set


    /**
     * Validates the input for the registration form.
     *
     * @param registerRequest The request containing registration data.
     * @return `true` if the input is valid, `false` otherwise.
     */
    private fun validateRegisterInput(registerRequest: RegisterRequest): Boolean {

        val username = registerRequest.username
        val email = registerRequest.email
        val password = registerRequest.password
        val location = registerRequest.location
        val contact = registerRequest.contact

        _error.value = if (username.isEmpty()) {
            "Username is required"
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            "Invalid email"
        } else if (password.isEmpty() || password.length < 6) {
            "Password must be at least 6 characters long"
        } else if (location.isEmpty()) {
            "Location is required"
        } else if (contact.isEmpty()) {
            "Contact is required"
        } else if (contact.length != 9) {
            "Contact must have 9 digits"
        } else {
            return true
        }

        return false
    }

    /**
     * Registers a new user using the provided registration data.
     *
     * @param registerRequest The request containing user registration details.
     */
    fun register(
        registerRequest: RegisterRequest,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {},
    ) {
        if (!validateRegisterInput(registerRequest)) {
            return
        }

        _isLoading.value = true
        _error.value = ""

        try {
            authRepository.register(registerRequest) { result ->
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${result.message}"
                        _isLoading.value = false
                        result.message?.let { onError(it) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    /**
     * Logs in a user using their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _error.value = "Please fill in all fields"
            onError("Please fill in all fields")
            return
        }


        _isLoading.value = true
        try {
            authRepository.login(email, password) { result ->
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${result.message}"
                        _isLoading.value = false
                        result.message?.let { onError(it) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    /**
     * Checks if a user is logged in.
     *
     * @param onSuccess A callback executed if the user is successfully authenticated.
     */
    fun isLoggedIn(
        onSuccess: () -> Unit = {},
    ) {

        _isLoading.value = true
        try {
            authRepository.isLoggedIn() { result ->
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${result.message}"
                        _isLoading.value = false

                        // will clear the authPreferences
                        authRepository.logout()
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun logout(onSuccess: () -> Unit = {}) {
        authRepository.logout()
        onSuccess()
    }
}