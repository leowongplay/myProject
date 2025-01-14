package com.example.comp4342mobilecomputinggroupproject.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.Admin.AdminMainActivity
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper


//init
@SuppressLint("StaticFieldLeak")
lateinit var bottonLogin: Button
@SuppressLint("StaticFieldLeak")
lateinit var bottonRegister: Button
@SuppressLint("StaticFieldLeak")
lateinit var edtUsername: EditText
@SuppressLint("StaticFieldLeak")
lateinit var edtPassword: EditText

class LoginActivity : ComponentActivity() {
    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val loginURL: String = "${URL}both_login.php"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //set view
        setContentView(R.layout.actitivty_login)
        bottonLogin = findViewById(R.id.loginButton)
        bottonRegister = findViewById(R.id.registerButtton)
        edtUsername = findViewById(R.id.login_customerName)
        edtPassword = findViewById(R.id.login_password)


        //login onClick
        bottonLogin.setOnClickListener{
            login()
        }

        bottonRegister.setOnClickListener{
            val intent = Intent(
                this@LoginActivity, RegisterActivity::class.java
            )
            startActivity(intent)
        }

    }

    //login function
    private fun login() {
        //retrieve data
        val username = edtUsername.text.toString()
        val password = edtPassword.text.toString()
        //debug
        println("username: $username")
        println("password: $password")
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        //set request
        val stringRequest: StringRequest = object : StringRequest(Method.POST, loginURL,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    if (!response.equals("input empty")) { //not empty input
                        //decode JSON object -> retrieve data(JSON format) from php
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("error") //pass error msg
                        var privilege = ""
                        var retrieveName = ""

                        if (status.equals("")) { //no error (empty error msg)
                            privilege = jsonObject.getString("privilege")
                            retrieveName = jsonObject.getString("username")
                            println("Welcome $privilege $retrieveName") //eg. (Welcome user testuser1)
                            val builder: AlertDialog.Builder =
                                AlertDialog.Builder(this@LoginActivity)
                            builder
                                .setTitle("Welcome back!")
                                .setMessage("Welcome $privilege $retrieveName")
                                .setPositiveButton("Continue") { dialog, which ->
                                    // Action to take when OK is clicked
                                    dialog.dismiss() // Optionally dismiss the dialog
                                    if (privilege == "user") {
                                        val intent = Intent(
                                            this@LoginActivity, ShoppingActivity::class.java
                                        )
                                        startActivity(intent)

                                    } else if (privilege == "admin") {
                                        val intent = Intent(
                                            this@LoginActivity, AdminMainActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        } else if (status.equals("Invalid username or password")) { //error
                            println(status)
                            okDialog("Error!", "Invalid username or password")
                        }
                    }
                } catch (e: JSONException){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["username"] = username //pass username
                paramV["password"] = password //pass password
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun okDialog(title:String, Message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        builder
            .setTitle(title)
            .setMessage(Message)
            .setPositiveButton("OK") { dialog, which ->
                // Action to take when OK is clicked
                dialog.dismiss() // Optionally dismiss the dialog
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}