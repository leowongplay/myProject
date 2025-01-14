package com.example.comp4342mobilecomputinggroupproject.activities.Admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.adapters.ProductCardAdapter
import com.example.comp4342mobilecomputinggroupproject.data.ProductKenny
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import org.json.JSONArray

//init add button
@SuppressLint("StaticFieldLeak")
lateinit var btnSearch: ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var edtSearch: EditText
@SuppressLint("StaticFieldLeak")
lateinit var btnAddProduct: Button
@SuppressLint("StaticFieldLeak")
lateinit var btnReload: ImageButton
val cHelper = ConnectionHelper

class AdminProductManActivity  : ComponentActivity() {

    //init category recycler view
    private lateinit var prodRecyclerView: RecyclerView
    //init category array list <Category> (for show item)
    private lateinit var prodArrayList: ArrayList<ProductKenny>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_man)

        btnSearch = findViewById(R.id.btnSearch)
        edtSearch = findViewById(R.id.edtSearch)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnReload = findViewById(R.id.btnReload)

        //build recycler view
        prodRecyclerView = findViewById(R.id.product_man_recyclerView) //match recycler view
        prodArrayList = ArrayList<ProductKenny>() //init arrayList
        getProductData() //get Category data from database
        buildRecyclerView() //build recycler view

        btnSearch.setOnClickListener(){

        }

        btnAddProduct.setOnClickListener(){

            finish()
            val intent = Intent(
                this@AdminProductManActivity,
                AdminNewProduct::class.java
            )
            startActivity(intent)
        }

        btnReload.setOnClickListener(){
            finish()
            startActivity(intent)
        }

    }

    private fun getProductData(){
        //get data from database
        selectProductRequest()
        println("getData size: ${prodArrayList.size}")
    }

    private fun buildRecyclerView(){
        //init adapter class
        val productAdapter = ProductCardAdapter(prodArrayList)

        //build in LinearLayout
        prodRecyclerView.layoutManager = LinearLayoutManager(this)
        prodRecyclerView.setHasFixedSize(true)

        //fill arrayList data -> adapter -> recycler view
        prodRecyclerView.adapter = productAdapter //assign categoryAdapter -> RecyclerView
        println("size: ${prodArrayList.size}")

        // Applying OnClickListener to our Adapter
        productAdapter.setOnClickListener(object :
            ProductCardAdapter.OnClickListener {
            override fun onClick(position: Int, product: ProductKenny) {
                println("ManActivity position: $position")
                println("ManActivity product name: ${product.productName} ${product.size} ${product.color}")

                //intent
                finish()
                val intent = Intent(
                    this@AdminProductManActivity,
                    AdminUpdateProduct::class.java
                )
                //put info

                println("intent productID: ${product.productID.toString()}")
                println("intent categoryName: ${product.categoryName.toString()}")

                intent.putExtra("productID", product.productID.toString())
                intent.putExtra("categoryID", product.categoryID.toString())
                intent.putExtra("categoryName", product.categoryName.toString())
                intent.putExtra("description", product.description.toString())
                intent.putExtra("productName", product.productName.toString())
                intent.putExtra("sellPrice", product.sellPrice.toString())
                intent.putExtra("discount", (product.discount?.times(100)).toString())
                intent.putExtra("qty", product.qty.toString())
                intent.putExtra("imageURL", product.imageURL.toString())
                intent.putExtra("color", product.color.toString())
                intent.putExtra("size", product.size.toString())
                intent.putExtra("useStatus", product.useStatus.toString())
                //start intent
                startActivity(intent)
            }
        })
    }

    private fun selectProductRequest() {
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/selectProduct.php"
        println("url: $url")

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonArray = JSONArray(response)

                    //foreach jsonObject
                    for(i in 0 until jsonArray.length()){
                        val tmpObject = jsonArray.getJSONObject(i)
                        val tmpProduct = ProductKenny(
                            productID = tmpObject.getInt("productID"),
                            categoryID = tmpObject.getInt("categoryID"),
                            categoryName = tmpObject.getString("categoryName"),
                            description = tmpObject.getString("description"),
                            productName = tmpObject.getString("productName"),
                            sellPrice = tmpObject.getDouble("sellPrice"),
                            discount = tmpObject.getDouble("discount"),
                            qty = tmpObject.getInt("remainingQTY"),
                            imageURL = tmpObject.getString("imageURL"),
                            color = tmpObject.getString("color"),
                            size = tmpObject.getString("size"),
                            useStatus = tmpObject.getInt("useStatus")
                        )

                        prodArrayList.add(tmpProduct) //add object into arrayList (for build recycler view)
                    }

                    println("request size: ${prodArrayList.size}")

                    //!Important! -> Rebuild RecyclerView
                    buildRecyclerView()
                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["empty"] = "empty"
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

}