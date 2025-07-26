package com.farid.inventory

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.farid.inventory.model.RegisterResponse
import com.farid.inventory.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var progressBar: ProgressBar // Tambahan untuk loading (opsional)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        // progressBar = findViewById(R.id.progressBar) // Uncomment jika pakai loading

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            register(name, email, password)
        }

        tvLogin.setOnClickListener {
            goToLogin()
        }
    }

    private fun register(name: String, email: String, password: String) {
        btnRegister.isEnabled = false
        // progressBar.visibility = View.VISIBLE

        ApiClient.instance.register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    btnRegister.isEnabled = true
                    // progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.success == true) {
                            Toast.makeText(
                                this@RegisterActivity,
                                registerResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            goToLogin()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                registerResponse?.message ?: "Register gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register gagal: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    btnRegister.isEnabled = true
                    // progressBar.visibility = View.GONE

                    Toast.makeText(
                        this@RegisterActivity,
                        "Koneksi gagal: ${t.localizedMessage ?: "Tidak diketahui"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
