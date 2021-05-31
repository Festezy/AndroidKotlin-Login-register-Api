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
import com.ariqandrean.androidregister.api.DefaultResponse
import com.ariqandrean.androidregister.api.LoginResponse
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnsave.setOnClickListener {
            register()
        }
    }

    fun register(){
        var name = etnama.text.toString()
        var email = etemail.text.toString()
        var password = etpassword.text.toString()
        var password2 = etRepeatpassword.text.toString()
        if (password != password2){
            return Toast.makeText(this, "password tidak sesuai", Toast.LENGTH_LONG).show()
        }
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            return Toast.makeText(this, "Masih ada field yg kosong", Toast.LENGTH_LONG).show()
        }

        var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
        var requestCall: Call<DefaultResponse> = apiInterface.register(name, email, password)
        requestCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Proses regoister berhasil", Toast.LENGTH_LONG).show()
                    Log.d("log", response.body()?.success.toString())

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RegisterActivity, "Proses registrasi gagal", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(baseContext, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }
}