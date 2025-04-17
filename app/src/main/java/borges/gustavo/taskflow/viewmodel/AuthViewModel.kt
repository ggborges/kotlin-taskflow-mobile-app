package borges.gustavo.taskflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<Result<FirebaseUser?>>()
    val authResult: LiveData<Result<FirebaseUser?>> = _authResult

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.postValue(Result.success(firebaseAuth.currentUser))
                } else {
                    _authResult.postValue(Result.failure(task.exception ?: Exception("Erro desconhecido no login.")))
                }
            }
    }

    fun register(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        _authResult.postValue(Result.success(firebaseAuth.currentUser))
                    }
                } else {
                    _authResult.postValue(Result.failure(task.exception ?: Exception("Erro desconhecido no cadastro.")))
                }
            }
    }
}