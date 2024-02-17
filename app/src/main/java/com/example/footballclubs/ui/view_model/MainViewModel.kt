package com.example.footballclubs.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footballclubs.models.Club
import com.example.footballclubs.models.User
import com.example.footballclubs.utils.CurrentUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private var _clubs = MutableStateFlow(emptyList<Club>())
    val clubs = _clubs.asStateFlow()

    private var _likedClubs = MutableStateFlow(emptyList<Club>())
    val likedClubs = _likedClubs.asStateFlow()

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    var currentClub = Club.empty()

    private var _isSignUpSuccess = MutableLiveData<Boolean>()
    val isSignUpSuccess: LiveData<Boolean> = _isSignUpSuccess

    private var _isSignInSuccess = MutableLiveData<Boolean>()
    val isSignInSuccess: LiveData<Boolean> = _isSignInSuccess

    private var _isLikeSuccess = MutableLiveData<Boolean>()
    val isLikeSuccess: LiveData<Boolean> = _isLikeSuccess

    private var _isUserDeleted = MutableLiveData<Boolean>()
    val isUserDeleted: LiveData<Boolean> = _isUserDeleted

    fun fetchClubs() {
        val clubs = mutableListOf<Club>()

        firestore.collection(CLUBS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    clubs.add(getClubFromDocument(document))
                }
                _clubs.value = clubs
            }
            .addOnFailureListener {
                _clubs.value = clubs
            }
    }

    fun fetchLikedClubs(ids: List<Int>) {
        if (ids.isEmpty()) _likedClubs.value = emptyList()
        val clubs = mutableListOf<Club>()
        val collectionReference = firestore.collection(CLUBS_COLLECTION)

        ids.forEach { clubId ->
            collectionReference
                .document(clubId.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        clubs.add(getClubFromDocument(documentSnapshot))
                        if (clubs.size == ids.size) _likedClubs.value = clubs
                    }
                }
                .addOnFailureListener {
                    _likedClubs.value = emptyList()
                }
        }
    }

    private fun getClubFromDocument(document: DocumentSnapshot): Club {
        val data = document.data
        val playersImagesList =
            data?.get(PLAYERS_IMAGES_KEY) as? List<*> ?: emptyList<String>()
        return Club(
            id = document.id.toInt(),
            name = data?.get(NAME_KEY).toString(),
            url = data?.get(IMAGE_KEY).toString(),
            description = data?.get(DESCRIPTION_KEY).toString(),
            playersImages = playersImagesList.map { it.toString() }
        )
    }


    fun createUser(user: User) {
        val userMap = with(user) {
            hashMapOf(
                EMAIL_KEY to email,
                PASSWORD_KEY to password,
                NAME_KEY to name,
                SURNAME_KEY to surname,
                PATRONYMIC_KEY to patronymic,
                BIRTHDAY_KEY to birthday,
                GENDER_KEY to gender,
                STATUS_KEY to status,
                DESCRIPTION_KEY to description,
                COUNTRY_KEY to country,
                CITY_KEY to city,
                LIKED_CLUBS_KEY to emptyList<Int>()
            )
        }

        firestore.collection(USERS_COLLECTION)
            .document(user.email)
            .set(userMap)
            .addOnSuccessListener { _ ->
                CurrentUser.user = user
                _isSignUpSuccess.value = true
            }
            .addOnFailureListener { _ ->
                _isSignUpSuccess.value = false
            }
    }

    fun getUser(findEmail: String, isSignIn: Boolean) {
        firestore.collection(USERS_COLLECTION)
            .whereEqualTo(EMAIL_KEY, findEmail)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    val ids = data[LIKED_CLUBS_KEY] as? List<*> ?: emptyList<Int>()
                    val currentUser = User(
                        email = data[EMAIL_KEY].toString(),
                        password = data[PASSWORD_KEY].toString(),
                        name = data[NAME_KEY].toString(),
                        surname = data[SURNAME_KEY].toString(),
                        patronymic = data[PATRONYMIC_KEY].toString(),
                        birthday = data[BIRTHDAY_KEY].toString(),
                        gender = data[GENDER_KEY].toString(),
                        status = data[STATUS_KEY].toString(),
                        description = data[DESCRIPTION_KEY].toString(),
                        country = data[COUNTRY_KEY].toString(),
                        city = data[CITY_KEY].toString(),
                        likedClubsIds = ids.map { it.toString().toInt() }.toMutableList()
                    )
                    CurrentUser.user = currentUser
                }
                if (isSignIn) _isSignInSuccess.value = true
                else _isUserSignedIn.value = true
            }
            .addOnFailureListener { _ ->
                if (isSignIn) _isSignInSuccess.value = false
                else _isUserSignedIn.value = false
            }
    }

    fun deleteUser(email: String) {
        firestore.collection(USERS_COLLECTION).document(email)
            .delete()
            .addOnSuccessListener {
                _isUserDeleted.value = true
            }
            .addOnFailureListener { _ ->
                _isUserDeleted.value = false
            }
    }

    fun addClubToFavourites(documentId: String, likedId: Int) {
        val docRef = firestore.collection(USERS_COLLECTION).document(documentId)

        docRef.update(LIKED_CLUBS_KEY, FieldValue.arrayUnion(likedId))
            .addOnSuccessListener {
                _isLikeSuccess.value = true
            }
            .addOnFailureListener { _ ->
                _isLikeSuccess.value = false
            }
    }

    fun removeClubFromFavourites(documentId: String, dislikedId: Int) {
        val docRef = firestore.collection(USERS_COLLECTION).document(documentId)

        docRef.update(LIKED_CLUBS_KEY, FieldValue.arrayRemove(dislikedId))
            .addOnSuccessListener {
                _isLikeSuccess.value = true
            }
            .addOnFailureListener { _ ->
                _isLikeSuccess.value = false
            }
    }

    class MainFactory(private val firestore: FirebaseFirestore) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(firestore) as T
        }
    }

    private companion object {
        const val CLUBS_COLLECTION = "football_clubs"
        const val USERS_COLLECTION = "users"
        const val PLAYERS_IMAGES_KEY = "players_images"
        const val IMAGE_KEY = "image"
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val NAME_KEY = "name"
        const val SURNAME_KEY = "surname"
        const val PATRONYMIC_KEY = "patronymic"
        const val BIRTHDAY_KEY = "birthday"
        const val GENDER_KEY = "gender"
        const val STATUS_KEY = "status"
        const val DESCRIPTION_KEY = "description"
        const val COUNTRY_KEY = "country"
        const val CITY_KEY = "city"
        const val LIKED_CLUBS_KEY = "liked_clubs"
    }
}