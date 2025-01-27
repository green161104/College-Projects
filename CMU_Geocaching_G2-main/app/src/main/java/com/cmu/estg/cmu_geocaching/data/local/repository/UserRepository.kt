package com.cmu.estg.cmu_geocaching.data.local.repository

import android.util.Log
import com.cmu.estg.cmu_geocaching.data.local.dao.UserDao
import com.cmu.estg.cmu_geocaching.data.local.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {
    // Get current user's UID
    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    // Save user to Firestore
    suspend fun saveUserToFirestore(user: User) {
        val currentUser = firebaseAuth.currentUser ?: return

        firestore.collection("Users")
            .document(currentUser.uid)
            .set(user.copy(userId = currentUser.uid))
            .await()
    }

    // Fetch user from Firestore
    suspend fun fetchUserFromFirestore(): User? {
        val currentUser = firebaseAuth.currentUser ?: return null

        return try {
            val snapshot = firestore.collection("Users")
                .document(currentUser.uid)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.d("fetchUserFromFirestore: ", e.message.toString());
            null
        }
    }

    // Cache user to Room
    suspend fun cacheUserLocally(user: User) {
        userDao.upsertUser(user)
    }

    suspend fun getCachedUser(): User? {
        val currentUserId = getCurrentUserId()
        return currentUserId?.let { userDao.getUserById(it) }
    }

    // Sync process: Fetch from Firestore and cache to Room
    suspend fun syncUserData() {
        val firestoreUser = fetchUserFromFirestore()
        firestoreUser?.let {
            cacheUserLocally(
                it
            )
        }
    }

    suspend fun updateUserProfile(updatedUser: User) {
        val userDoc =
            firestore.collection("Users").document(firebaseAuth.currentUser?.uid ?: return)
        userDoc.update(
            mapOf(
                "name" to updatedUser.name,
                "pronouns" to updatedUser.pronouns,
            )
        )

        // Update local cache
        cacheUserLocally(updatedUser)
    }

    suspend fun updateUserPoints(updatedUser: User) {
        // Update in Firestore
        val userDoc =
            firestore.collection("Users").document(firebaseAuth.currentUser?.uid ?: return)
        userDoc.update(
            mapOf(
                "points" to updatedUser.points,
            )
        )

        // Update local cache
        cacheUserLocally(updatedUser)
    }

    suspend fun updateProfilePicture(userId: String, picturePath: String): User? {
        // Update local database
        userDao.updateProfilePicturePath(userId, picturePath)

        // Update Firestore
        val userDoc = firestore.collection("Users").document(userId)
        userDoc.update("profilePicture", picturePath)

        return userDao.getUserById(userId)
    }
//    suspend fun updateUserEmailFirebase(newEmail: String) {
//        try {
//            // Get the current authenticated user
//            val currentUser: FirebaseUser = FirebaseAuth.getInstance().currentUser
//                ?: throw IllegalStateException("No user is currently signed in")
//
//            currentUser.verifyBeforeUpdateEmail(newEmail).await()
//        } catch (e: Exception) {
//            // Handle different types of exceptions
//            when (e) {
//                is IllegalStateException -> {
//                    // No user signed in
//                    throw e
//                }
//                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
//                    // Invalid email format
//                    throw IllegalArgumentException("Invalid email format")
//                }
//                is com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException -> {
//                    // Recent login required - user needs to re-authenticate
//                    throw SecurityException("Recent authentication required to update email")
//                }
//                else -> {
//                    // Other Firebase-related errors
//                    throw RuntimeException("Failed to update email: ${e.message}")
//                }
//            }
//        }
//    }
}