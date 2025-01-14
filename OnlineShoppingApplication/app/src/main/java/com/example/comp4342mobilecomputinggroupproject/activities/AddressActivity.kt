package com.example.comp4342mobilecomputinggroupproject.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo
import org.json.JSONObject


@SuppressLint("StaticFieldLeak")
lateinit var tvAddressTitle : TextView
@SuppressLint("StaticFieldLeak")
lateinit var tvUserName : TextView
@SuppressLint("StaticFieldLeak")
lateinit var tvFullName : TextView
@SuppressLint("StaticFieldLeak")
lateinit var tvEmail : TextView
@SuppressLint("StaticFieldLeak")
lateinit var tvPhone : TextView
@SuppressLint("StaticFieldLeak")
lateinit var buttonCancel : AppCompatButton
@SuppressLint("StaticFieldLeak")
lateinit var buttonSave : AppCompatButton


class AddressActivity : ComponentActivity() {
    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val getUserInfoURL: String = "${URL}Get_User_Info.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_address)

        tvAddressTitle = findViewById(R.id.tvAddressTitle)
        tvUserName = findViewById(R.id.tvUserName)
        tvFullName = findViewById(R.id.tvFullName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        buttonCancel = findViewById(R.id.buttonCancel)
        buttonSave = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener() {
            val intent = Intent(
                this@AddressActivity, PaymentActivity::class.java)
            startActivity(intent)
        }
        buttonCancel.setOnClickListener() {
            finish()
        }
        getUserInfo()
    }

    private fun getUserInfo(){
        // set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val stringRequest: StringRequest =
            object :
                StringRequest(
                    Method.POST,
                    getUserInfoURL,
                    Response.Listener<String> { response ->
                        println("return response: $response")
                        try { // catch JSONException
                            val jsonObject = JSONObject(response)
                            val status = jsonObject.getString("error") // pass error msg
                            println("status: $status")
                            if (status.equals("Input empty")) { // not empty input
                                // TODO: pop up a dialog to show the response
                                okDialog("Error!", "Input empty")
                            } else if (status.equals("User not found")) {
                                // TODO: pop up a dialog to show the response
                                okDialog("Error!", "User not found")
                            } else if (status.equals("")) {
                                val userObject = jsonObject.getJSONObject("userInfo")
                                println(userObject)
                                tvAddressTitle.text = userObject.getString("shippingAddress");
                                tvFullName.text = userObject.getString("username")
                                tvUserName.text = userObject.getString("customerName")
                                tvPhone.text = userObject.getString("phoneNo")
                                tvEmail.text = userObject.getString("userEmail")


                            }
                        } catch (e: Exception) { // catch
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        error.localizedMessage?.let { Log.e("Error", it) }
                    }
                ) { // response error listener
                override fun getParams():
                        Map<String, String> { // pass data through request (-> php)
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV["userID"] = UserInfo.userID
                    return paramV
                }
            }
        queue.add(stringRequest)
    }

    private fun okDialog(title:String, Message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@AddressActivity)
        builder
            .setTitle(title)
            .setMessage(Message)
            .setPositiveButton("OK") { dialog, which ->
                // Action to take when OK is clicked
                dialog.dismiss() // Optionally dismiss the dialog
                finish()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()


    }

}
