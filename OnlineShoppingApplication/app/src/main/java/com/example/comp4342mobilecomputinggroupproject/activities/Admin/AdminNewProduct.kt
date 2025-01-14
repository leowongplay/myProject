package com.example.comp4342mobilecomputinggroupproject.activities.Admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.databinding.ActivityNewProductBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AdminNewProduct : ComponentActivity() {
    private lateinit var imageBitmap: Bitmap
    var imageBase64: String = null.toString()
    var issetImage: Boolean = false
    var discount: Double = 100.0

    private val binding by lazy {
        ActivityNewProductBinding.inflate(layoutInflater)
    }

    //open gallery
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{
            binding.imageProduct.setImageURI(galleryUri)

            // Uri -> Bitmap
            if(galleryUri != null){
                imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, galleryUri))
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, galleryUri)
                }
                //Bitmap -> base64
                imageBase64 = getStringImage(imageBitmap).toString()
                issetImage = true
            }
        }catch(e:Exception){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnDelete.visibility = View.INVISIBLE

        //upload image
        binding.btnSetImage.setOnClickListener(){
            println("Name: ${binding.edtNewProdName.text}")
            println("Category: ${binding.edtCategory.text}")
            println("Color: ${binding.edtColor.text}")
            println("Size: ${binding.edtSize.text}")
            println("Price: ${binding.edtPrice.text}")
            println("Qty: ${binding.edtQty.text}")
            println("Description: ${binding.edtDescription.text}")
            println("Discount: ${binding.edtDiscount.text}")

            //upload image from gallery
            galleryLauncher.launch("image/*")

        }

        //submit
        binding.btnSubmit.setOnClickListener(){
            var inputEmpty: Boolean = false

            println("Name: ${binding.edtNewProdName.text}")
            println("Category: ${binding.edtCategory.text}")
            println("Color: ${binding.edtColor.text}")
            println("Size: ${binding.edtSize.text}")
            println("Price: ${binding.edtPrice.text}")
            println("Qty: ${binding.edtQty.text}")
            println("Description: ${binding.edtDescription.text}")
            println("Discount: ${binding.edtDiscount.text}")

            if(binding.edtNewProdName.text.isEmpty()) {inputEmpty = true}
            if(binding.edtCategory.text.isEmpty()) {inputEmpty = true}
            if(binding.edtColor.text.isEmpty()) {inputEmpty = true}
            if(binding.edtSize.text.isEmpty()) {inputEmpty = true}
            if(binding.edtPrice.text.isEmpty()) {inputEmpty = true}
            if(binding.edtQty.text.isEmpty()) {inputEmpty = true}
            if(binding.edtDescription.text.isEmpty()) {inputEmpty = true}
            if(binding.edtDiscount.text.isEmpty()) {inputEmpty = true}

            if(!inputEmpty){
                println("not empty")

                if(binding.edtDiscount.text.isNotEmpty()){
                    discount = binding.edtDiscount.text.toString().toDouble() / 100
                }


                insertProductRequest()
            }else{
                alterDialog("Have Empty Input", "Please input all data (not include image)")
            }

        }

        binding.imageBtnBack.setOnClickListener() {
            finish()
            val intent = Intent(
                this@AdminNewProduct,
                AdminProductManActivity::class.java
            )
            startActivity(intent)
        }

    }


    private fun insertProductRequest(){
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/insertProduct.php"
        println("url: $url")

        val stringRequest: StringRequest = @SuppressLint("UnsafeIntentLaunch")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))

                    if(jsonObject.getString("error").equals("Category Not Found")){
                        alterDialog("Category Not Found","Please input exist and active category")
                    }else if(jsonObject.getString("error").take(15) == "Duplicate entry"){
                        alterDialog("Insert Fail","Product already exist")
                    }else if(jsonObject.getString("error") == "Product exist"){
                        alterDialog("Insert Fail","Product already exist")
                    }else{
                        alterDialog("Insert Success","You can continue")
                    }

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["productName"] = binding.edtNewProdName.text.toString()
                paramV["categoryName"] = binding.edtCategory.text.toString()
                paramV["description"] = binding.edtDescription.text.toString()
                paramV["sellPrice"] = binding.edtPrice.text.toString()
                paramV["color"] = binding.edtColor.text.toString()
                paramV["size"] = binding.edtSize.text.toString()
                paramV["discount"] = discount.toString()
                paramV["qty"] = binding.edtQty.text.toString()
                if(issetImage){
                    paramV["image"] = imageBase64
                }
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun alterDialog(title: String, msg: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(msg)
            .setTitle(title)
            .setPositiveButton("Ok") { dialog, which ->
                if (title == "Insert Success"){
                    //Reload this activity
                    finish()
                    startActivity(intent);
                }

            }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getStringImage(bm: Bitmap): String? {
        val baOutputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baOutputStream)
        val imageByte = baOutputStream.toByteArray()
        val base64 = Base64.encodeToString(imageByte,Base64.DEFAULT)
        return base64
    }
}

