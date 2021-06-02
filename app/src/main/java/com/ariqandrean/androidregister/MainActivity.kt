package com.ariqandrean.androidregister

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariqandrean.androidregister.Adapter.CategoryAdapter
import com.ariqandrean.androidregister.api.ApiClient
import com.ariqandrean.androidregister.api.ApiInterface
import com.ariqandrean.androidregister.login.LoginActivity
import com.ariqandrean.androidregister.login.RegisterActivity
import com.ariqandrean.androidregister.model.CategoryModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBar: ActionBarDrawerToggle
    private lateinit var navDrawerView: NavigationView

    //initialize the navigationBottom bar
    private lateinit var bottomNavigation : BottomNavigationView
//    private fun setCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply{
//            replace(R.id.fragmentContainer, fragment)
//            commit()
//        }

    lateinit var sharedPref: SharedPreferences
    var token: String = ""

    var myAdapter : CategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8){
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
            token = sharedPref.getString("token", "")!!

            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
            var requestCall: Call<JsonObject> = apiInterface.getCategories("Brearer$token")
            requestCall.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("gagal", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("category Log", response.body().toString())
                    val myJson = response.body()
                    val myData = myJson!!.getAsJsonArray("data")

                    Log.d("My Log", myData.size().toString())
                    myAdapter = CategoryAdapter(this@MainActivity)

                    val arrayItem = ArrayList<CategoryModel>()
                    for (i in 0 until myData.size()) {
                        var myRecord: JsonObject = myData.get(i).asJsonObject
                        var id = myRecord.get("id").asInt
                        var name = myRecord.get("name").asString
                        var image_link = myRecord.get("image_link").asString

                        Log.d("Log$i", myData.get(i).toString())
                        arrayItem.add(CategoryModel(id, name, image_link))
                    }
                    Log.d("Array Item", arrayItem.toString())
                    myAdapter!!.setData(arrayItem)

                    Product_recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    Product_recyclerView.adapter = myAdapter
                }
            })

//            // nav_bottom fragment
//            val firstFragment = Dashboard()
//            val secondFragment = Notifikasi()
//            val thirdFragment = MyProfile()
//            setCurrentFragment(firstFragment)

            bottomNavigation = findViewById(R.id.navBottom)
            bottomNavigation.setOnNavigationItemReselectedListener {
                when (it.itemId){
                    R.id.navigation_dashboard -> {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_notifikasi -> {
                        Toast.makeText(applicationContext, "notifikasi", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.navigation_profile -> {
                        Toast.makeText(applicationContext, "profile", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    false
                }
                }
            }
//
//            drawerLayout = findViewById(R.id.drawer)
//            actionBar = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
//
//            drawerLayout.addDrawerListener(actionBar)
        }
    }
}
