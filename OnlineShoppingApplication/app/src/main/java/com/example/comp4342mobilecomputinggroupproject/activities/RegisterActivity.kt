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
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.AddressActivity
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo
import org.json.JSONObject


//init
@SuppressLint("StaticFieldLeak")
lateinit var btnRegister: Button
@SuppressLint("StaticFieldLeak")
lateinit var edtRegisterUsername: EditText
@SuppressLint("StaticFieldLeak")
lateinit var edtRegisterPassword: EditText
@SuppressLint("StaticFieldLeak")
lateinit var edtRegisterEmail: EditText
@SuppressLint("StaticFieldLeak")
lateinit var edtRegisterPhone: EditText
@SuppressLint("StaticFieldLeak")
lateinit var edtRegisterCustName: EditText

class RegisterActivity : ComponentActivity() {

    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val registerURL: String = "${URL}Create_User.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //set view
        setContentView(R.layout.activity_register)
        btnRegister = findViewById(R.id.register_submit)
        edtRegisterUsername = findViewById(R.id.register_username)
        edtRegisterPassword = findViewById(R.id.login_password)
        edtRegisterPhone = findViewById(R.id.register_phoneNumber)
        edtRegisterEmail = findViewById(R.id.register_custEmail)
        edtRegisterCustName = findViewById(R.id.login_customerName)

        //register onClickListener
        btnRegister.setOnClickListener{
            onButtonRegister()
        }
    }

    fun onButtonRegister() {
        //retrieve data
        val username = edtRegisterUsername.text.toString()
        val password = edtRegisterPassword.text.toString()
        val email = edtRegisterEmail.text.toString()
        val phone = edtRegisterPhone.text.toString()
        val custName = edtRegisterCustName.text.toString()

        //debug
        println("username: $username")
        println("password: $password")
        println("email: $email")
        println("phone: $phone")
        println("custName: $custName")

        //set request
        val queue = Volley.newRequestQueue(applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, registerURL,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                val jsonObject = JSONObject(response)
                val status = jsonObject.getString("error")

                if(status.equals("")){ //if login success
                    println("Register Success!!!")
                    val userid = jsonObject.getString("userID")
                    UserInfo.userID = userid

                    val intent = Intent(
                        this@RegisterActivity, ShoppingActivity::class.java)
                    startActivity(intent)

                } else if (status.equals("User not found")){ //if register fail
                    println("User not found")
                    okDialog("Error!", "User not found")
                } else if (status.equals("Username existed")){ //if Username existed
                    println("Username existed")
                    okDialog("Error!", "Username existed! Please try another username.")
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["username"] = username //pass username
                paramV["password"] = password //pass password
                paramV["email"] = email //pass email
                paramV["phone"] = phone //pass phone
                paramV["customerName"] = custName //pass customer name
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun okDialog(title:String, Message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
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