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
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.databinding.ActivityNewProductBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AdminUpdateProduct : ComponentActivity() {
    private lateinit var imageBitmap: Bitmap
    var issetImage: Boolean = false
    var imageBase64: String = ""
    var oldImageURL: String = ""
    var productID: String = ""
    var categoryID: String = ""
    var categoryName: String = ""
    var description: String = ""
    var productName: String = ""
    var sellPrice: String  = ""
    var discount: String = ""
    var qty: String = ""
    var imageURL: String  = ""
    var color: String = ""
    var size: String = ""


    private val binding by lazy {
        ActivityNewProductBinding.inflate(layoutInflater)
    }

    //open gallery
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{

            // Uri -> Bitmap
            if(galleryUri != null){
                binding.imageProduct.setImageURI(galleryUri)
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
        val temString: String = "Update Product"
        binding.txtNewProduct.text = temString


        productID = intent.extras?.getString("productID") ?: "No productID"
        categoryID = intent.extras?.getString("categoryID") ?: "No categoryID"
        categoryName = intent.extras?.getString("categoryName") ?: "No categoryName"
        description = intent.extras?.getString("description") ?: "No description"
        productName = intent.extras?.getString("productName") ?: "No productName"
        sellPrice = intent.extras?.getString("sellPrice"  ) ?: "No sellPrice"
        discount = intent.extras?.getString("discount") ?: "No discount"
        qty = intent.extras?.getString("qty") ?: "No qty"
        imageURL = intent.extras?.getString("imageURL") ?: "No imageURL"
        color = intent.extras?.getString("color") ?: "No color"
        size = intent.extras?.getString("size") ?: "No size"


        oldImageURL = intent.extras?.getString("imageURL") ?: "No imageURL"
        binding.edtNewProdName.setText(productName)
        binding.edtCategory.setText(categoryName)
        binding.edtDescription.setText(description)
        binding.edtColor.setText(color)
        binding.edtSize.setText(size)
        binding.edtQty.setText(qty)
        binding.edtDiscount.setText(discount)
        binding.edtPrice.setText(sellPrice)


        if(imageURL != null) {
            //get server URL
            val cHelper = ConnectionHelper

            //get image path
            val currentImageUrl: String = if (imageURL.toString().take(5) == "https") {
                imageURL.toString()
            } else {
                cHelper.cURL + "/mobileproject/imageDatabase/" + imageURL
            }

            //debug
            println("Image URL: $currentImageUrl")
            println(imageURL)

            //if product contain image -> load into imageView
            if (imageURL != "null" && imageURL != "No imageURL") {
                try {
                    //load into imageView
                    //View, URL, xml layout
                    Glide.with(applicationContext).load(currentImageUrl)
                        .into(binding.imageProduct)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


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

            discount = (binding.edtDiscount.text.toString().toDouble() / 100).toString()

            if(!inputEmpty){
                println("empty")
                updateProductRequest()
            }else{
                alterDialog("Have Empty Input", "Please input all data (not include image)")
            }

        }

        binding.btnDelete.setOnClickListener(){
            deleteAlterDialog("Delete Product", "Are you sure delete?", productID)
        }

        binding.imageBtnBack.setOnClickListener() {
            finish()
            val intent = Intent(
                this@AdminUpdateProduct,
                AdminProductManActivity::class.java
            )
            startActivity(intent)
        }
    }


    private fun updateProductRequest(){
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/updateProduct.php"
        println("url: $url")

        val stringRequest: StringRequest = @SuppressLint("UnsafeIntentLaunch")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException

                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))

                    if(jsonObject.getString("error").equals("update success")){
                        alterDialog("Update success","You can continue")
                    }else if(jsonObject.getString("error").equals("Category Not Found")){
                        alterDialog("Category Not Found","Please input exit category.")
                    }else{
                        alterDialog("Something Wrong Input", "Try again")
                    }

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()

                paramV["productID"] = productID
                paramV["categoryID"] = categoryID
                paramV["productName"] = binding.edtNewProdName.text.toString()
                paramV["categoryName"] = binding.edtCategory.text.toString()
                paramV["description"] = binding.edtDescription.text.toString()
                paramV["sellPrice"] = binding.edtPrice.text.toString()
                paramV["color"] = binding.edtColor.text.toString()
                paramV["size"] = binding.edtSize.text.toString()
                paramV["discount"] = discount.toString()
                paramV["qty"] = binding.edtQty.text.toString()
                paramV["oldImageURL"] = oldImageURL
                if(issetImage){
                    paramV["image"] = imageBase64
                }
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun deleteProductRequest(productID : String){
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/deleteProduct.php"
        println("url: $url")

        val stringRequest: StringRequest = @SuppressLint("UnsafeIntentLaunch")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))

                    //intent back product management activity
                    finish()
                    val intent = Intent(
                        this@AdminUpdateProduct,
                        AdminProductManActivity::class.java
                    )
                    startActivity(intent)

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["productID"] = productID
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

    private fun deleteAlterDialog(title: String, msg: String, productID: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(msg)
            .setTitle(title)
            .setPositiveButton("Yes") { dialog, which ->
                if (title == "Delete Product"){
                    //Reload this activity
                    deleteProductRequest(productID)
                    finish()
                    val intent = Intent(
                        this@AdminUpdateProduct,
                        AdminProductManActivity::class.java
                    )
                    startActivity(intent)
                }
            }
            .setNegativeButton("No") { dialog, which ->
                //Do nothing
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

