package com.tekskills.er_tekskills.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.ActivityLoginBinding
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.Common
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: MainActivityViewModel by viewModels()

//    @Inject
//    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        setupListeners()

        if (viewModel.checkIfUserLogin() != Common.PREF_DEFAULT) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.resLoginResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            res.let { token ->
                                token.let {
                                    if (viewModel.saveAuthToken(
                                            res.accessToken,
                                            res.refreshToken
                                        )
                                    ) {
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })


        binding.btnLogin.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
            if (isValidate()) {
                viewModel.userLogin(
                    binding.edtUsername.text.toString(),
                    binding.edtPassword.text.toString()
                )
                Toast.makeText(this, "validated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateUserName() && validatePassword()

    private fun setupListeners() {
        binding.edtUsername.addTextChangedListener(TextFieldValidation(binding.edtUsername))
        binding.edtPassword.addTextChangedListener(TextFieldValidation(binding.edtPassword))
    }

    /**
     * field must not be empty
     */
    private fun validateUserName(): Boolean {
        if (binding.edtUsername.text.toString().trim().isEmpty()) {
            binding.edtLoginUserName.error = "Required Field!"
            binding.edtUsername.requestFocus()
            return false
        } else {
            binding.edtLoginUserName.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) field must not be empty
     * 2) password lenght must not be less than 6
     * 3) password must contain at least one digit
     * 4) password must contain atleast one upper and one lower case letter
     * 5) password must contain atleast one special character.
     */
    private fun validatePassword(): Boolean {
        if (binding.edtPassword.text.toString().trim().isEmpty()) {
            binding.edtLoginPassword.error = "Required Field!"
            binding.edtPassword.requestFocus()
            return false
        } else if (binding.edtPassword.text.toString().length < 6) {
            binding.edtLoginPassword.error = "password can't be less than 6"
            binding.edtPassword.requestFocus()
            return false
//        }
//        else if (!isStringContainNumber(binding.edtPassword.text.toString())) {
//            binding.edtLoginPassword.error = "Required at least 1 digit"
//            binding.edtPassword.requestFocus()
//            return false
//        } else if (!isStringLowerAndUpperCase(binding.edtPassword.text.toString())) {
//            binding.edtLoginPassword.error =
//                "Password must contain upper and lower case letters"
//            binding.edtPassword.requestFocus()
//            return false
//        } else if (!isStringContainSpecialCharacter(binding.edtPassword.text.toString())) {
//            binding.edtLoginPassword.error = "1 special character required"
//            binding.edtPassword.requestFocus()
//            return false
        } else {
            binding.edtLoginPassword.isErrorEnabled = false
        }
        return true
    }

    /**
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.edt_username -> {
                    validateUserName()
                }

                R.id.edt_password -> {
                    validatePassword()
                }
            }
        }
    }
}