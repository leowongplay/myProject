package com.example.comp4342mobilecomputinggroupproject.activities.Admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.adapters.CategoryCardAdapter
import com.example.comp4342mobilecomputinggroupproject.data.Category
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import org.json.JSONArray
import org.json.JSONObject


//init add button
@SuppressLint("StaticFieldLeak")
lateinit var btnAddCategory: Button
@SuppressLint("StaticFieldLeak")
lateinit var edtNewCategory: EditText

class AdminCategoryManActivity : ComponentActivity() {
    //init category recycler view
    private lateinit var cateRecyclerView: RecyclerView
    //init category array list <Category> (for show item)
    private lateinit var cateArrayList: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_man)

        btnAddCategory = findViewById(R.id.btnAddCategory)
        edtNewCategory = findViewById(R.id.edtCategory)

        //build recycler view
        cateRecyclerView = findViewById(R.id.cate_man_recyclerView) //match recycler view
        cateArrayList = ArrayList<Category>() //init arrayList
        getCategoryData() //get Category data from database
        buildRecyclerView() //build recycler view

        btnAddCategory.setOnClickListener(){
            addNewCategory()
        }
    }

    private fun addNewCategory(){
        if(edtNewCategory.text.toString() == ""){
            alterDialog("Empty Input", "Try again")
        }else{
            //alterDialog("Your Input", edtNewCategory.text.toString())
            insertCategoryRequest(edtNewCategory.text.toString())
        }
    }

    private fun getCategoryData(){
        //hardcode example
        //cateArrayList.add(Category("Food"))
        //cateArrayList.add(Category("Drink"))

        //get data from database
        selectCategoryRequest()
        println("getData size: ${cateArrayList.size}")
    }

    private fun buildRecyclerView(){
        //init adapter class
        val categoryAdapter = CategoryCardAdapter(cateArrayList)

        //build in LinearLayout
        cateRecyclerView.layoutManager = LinearLayoutManager(this)
        cateRecyclerView.setHasFixedSize(true)

        //fill arrayList data -> adapter -> recycler view
        cateRecyclerView.adapter = categoryAdapter //assign categoryAdapter -> RecyclerView
        println("size: ${cateArrayList.size}")

        // Applying OnClickListener to our Adapter
        categoryAdapter.setOnClickListener(object :
            CategoryCardAdapter.OnClickListener {
            override fun onClick(position: Int, category: Category) {
                println("MainActivity position: $position")
                println("MainActivity category name: ${category.categoryName}")

                //delete confirmation dialog
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@AdminCategoryManActivity)
                builder
                    .setMessage("All related category of product would setting to NULL")
                    .setTitle("Delete Category:  ${category.categoryName}")
                    .setPositiveButton("Delete") { dialog, which ->
                        //confirm delete
                        deleteCategoryRequest(category.categoryName.toString())
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        //cancel delete
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
    }

    private fun selectCategoryRequest() {
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/selectCategory.php"
        println("url: $url")

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonArray = JSONArray(response)

                    //foreach jsonObject
                    for(i in 0 until jsonArray.length()){
                        val categoryObject = jsonArray.getString(i)
                        val category = Category(categoryObject)
                        println("add: ${category.categoryName}")
                        cateArrayList.add(category) //add object into arrayList (for build recycler view)
                    }

                    println("request size: ${cateArrayList.size}")

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

    private fun deleteCategoryRequest(categoryName : String){
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/deleteCategory.php"
        println("url: $url")

        val stringRequest: StringRequest = @SuppressLint("UnsafeIntentLaunch")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))

                    //Reload this activity
                    finish()
                    startActivity(intent);

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["categoryName"] = categoryName
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun insertCategoryRequest(categoryName : String){
        val cHelper = ConnectionHelper
        //set connection volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = cHelper.cURL + "/mobileproject/insertCategory.php"
        println("url: $url")

        val stringRequest: StringRequest = @SuppressLint("UnsafeIntentLaunch")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))

                    //Reload this activity
                    finish()
                    startActivity(intent);

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["categoryName"] = categoryName
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

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
