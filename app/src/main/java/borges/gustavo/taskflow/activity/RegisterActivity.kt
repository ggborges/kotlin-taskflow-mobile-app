package borges.gustavo.taskflow.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import borges.gustavo.taskflow.databinding.ActivityRegisterBinding
import borges.gustavo.taskflow.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.register(name, email, password)
        }

        authViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Cadastro não realizado: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
