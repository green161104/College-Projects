    package com.cmu.estg.cmu_geocaching.viewModel

    import android.app.Application
    import android.util.Log
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.viewModelScope
    import com.cmu.estg.cmu_geocaching.data.local.entities.User
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.auth
    import com.google.firebase.firestore.firestore
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await

    class AuthViewModel(application: Application) : AndroidViewModel(application){
        val authState : MutableLiveData<AuthStatus>
        val fAuth : FirebaseAuth
        private val firestore = Firebase.firestore
        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        init {
            authState = MutableLiveData(AuthStatus.NOLOGGIN)
            fAuth = Firebase.auth
        }


        fun registerUser(
            email: String,
            password: String,
            name: String,
            selectedPronoun: String
        ) {
            viewModelScope.launch {
                try {
                    // Create user in Firebase Authentication
                    val authResult = fAuth.createUserWithEmailAndPassword(email, password).await()
                    val firebaseUser = authResult.user

                    if (firebaseUser != null) {
                        // Create user document in Firestore
                        val userFirestore = User(
                            userId =  firebaseUser.uid,
                            name = name,
                            email = email,
                            pronouns = selectedPronoun,
                            points = 0
                        )

                        // Save user to Firestore
                        firestore.collection("Users")
                            .document(firebaseUser.uid)
                            .set(userFirestore)
                            .await()

                        authState.postValue(AuthStatus.LOGGED)
                        Log.d("Register", "User registered and profile saved")
                    } else {
                        authState.postValue(AuthStatus.NOLOGGIN)
                        Log.d("Register", "Registration failed")
                    }
                } catch (e: Exception) {
                    authState.postValue(AuthStatus.NOLOGGIN)
                    Log.e("Register", "Error registering user", e)
                }
            }
        }

        fun login(email: String, password: String) {
            viewModelScope.launch {
                try {
                    val result = fAuth.signInWithEmailAndPassword(email, password).await()
                    if (result.user != null) {
                        authState.postValue(AuthStatus.LOGGED)
                        _errorMessage.value = null  // Clear any previous error messages
                    } else {
                        _errorMessage.value = "Login failed. Please check your credentials."
                        authState.postValue(AuthStatus.NOLOGGIN)
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Login failed. Error: ${e.message}"
                    authState.postValue(AuthStatus.NOLOGGIN)
                }
            }
        }

        fun logout(){
            viewModelScope.launch {
                fAuth.signOut()
                authState.postValue(AuthStatus.NOLOGGIN)
                Log.d("Login","logout")
            }
        }

        enum class AuthStatus{
            LOGGED,
            NOLOGGIN
        }

    }

