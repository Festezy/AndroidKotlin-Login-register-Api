package com.ariqandrean.androidregister.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ariqandrean.androidregister.MainActivity
import com.ariqandrean.androidregister.R
import com.ariqandrean.androidregister.api.ApiClient
import com.ariqandrean.androidregister.api.ApiInterface
import com.ariqandrean.androidregister.response.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnmasuk.setOnClickListener {
//            goToSignUpPage()
            LoginMasuk()
        }
    }

    fun LoginMasuk() {
        val username = etusername.text.toString()
        val password = etpassword.text.toString()

        if (username == "" || password == "") {
            Toast.makeText(this, "Masih kosong", Toast.LENGTH_LONG).show()
        } else {
            val loginUser = LoginResponse(token = null)

            var apiInterface: ApiInterface =
                ApiClient().getApiClient()!!.create(ApiInterface::class.java)

            var requestCall: Call<LoginResponse> = apiInterface.login(username, password)

            requestCall.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("log", response.body()?.token.toString())
                        val token: String = response.body()?.token.toString()

                        //penyimpanan token ke server
                        sharedPref = getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPref.edit()
                        editor.putString("token", token)
                        editor.apply()
                        //batas proses penyimpanan
                        Toast.makeText(this@LoginActivity, "Login Sukses!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(baseContext, "Failed", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}


