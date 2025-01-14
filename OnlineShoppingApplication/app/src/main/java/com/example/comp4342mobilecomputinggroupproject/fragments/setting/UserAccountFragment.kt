package com.example.comp4342mobilecomputinggroupproject.fragments.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentUserAccountBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.tvAddressTitle
import com.example.comp4342mobilecomputinggroupproject.activities.tvEmail
import com.example.comp4342mobilecomputinggroupproject.activities.tvFullName
import com.example.comp4342mobilecomputinggroupproject.activities.tvPhone
import com.example.comp4342mobilecomputinggroupproject.activities.tvUserName
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo
import com.example.mobileproject.data.User
import org.json.JSONObject


class UserAccountFragment : Fragment() {

    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val getUserInfoURL: String = "${URL}Get_User_Info.php"
    private val updateUserInfoURL: String = "${URL}updateUserInfo.php"

    private lateinit var binding: FragmentUserAccountBinding
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private var imageUri: Uri? = null
    private lateinit var preUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
        getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserAccountBinding.inflate(inflater)
        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_profileFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {


                saveUserInfo()

        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }

    }

    private fun getUserInfo(){
        //set connection volley
        val queue = Volley.newRequestQueue(context)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, getUserInfoURL,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("error") // pass error msg
                    println("status: $status")
                    if (status.equals("")) {
                        val userObject = jsonObject.getJSONObject("userInfo")
                        println(userObject)
                        binding.edUserName.setText(userObject.getString("username"))
                        preUsername = userObject.getString("username")
                        binding.edFullName.setText(userObject.getString("customerName"))
                        binding.edPhone.setText(userObject.getString("phoneNo"))
                        binding.edEmail.setText(userObject.getString("userEmail"))
                        binding.edAddress.setText(userObject.getString("shippingAddress"))
                    }
                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["userID"] = UserInfo.userID
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun saveUserInfo(){
        val fullName =  binding.edFullName.text.toString().trim()
        val phone =  binding.edPhone.text.toString().trim()
        val email =  binding.edEmail.text.toString().trim()
        val address =  binding.edAddress.text.toString().trim()
        val username =  binding.edUserName.text.toString().trim()
        val user = User(fullName,phone, email, address, username)
        println(user)
        //set connection volley
        val queue = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, updateUserInfoURL,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try {
                    val jsonObject = JSONObject(response)
                    alterDialog(jsonObject.getString("error"))
                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["fullname"] = user.fullName
                paramV["phone"] = user.phone
                paramV["email"] = user.email
                paramV["address"] = user.address
                paramV["userId"] = UserInfo.userID
                paramV["newUsername"] = user.username
                paramV["username"] = preUsername
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun alterDialog(msg: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage(msg)
            .setTitle("Success Message")

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}