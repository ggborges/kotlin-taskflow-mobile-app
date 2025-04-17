package borges.gustavo.taskflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import borges.gustavo.taskflow.activity.RegisterActivity
import borges.gustavo.taskflow.activity.TaskListActivity
import borges.gustavo.taskflow.databinding.ActivityMainBinding
import borges.gustavo.taskflow.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.login(email, password)
        }

        binding.goToRegisterText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        authViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TaskListActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}