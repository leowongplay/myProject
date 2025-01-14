package com.example.comp4342mobilecomputinggroupproject.fragments.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.adapters.BestProductAdapter
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentMainCategoryBinding
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentSearchBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.interfaces.ProductListAdapter
import com.example.comp4342mobilecomputinggroupproject.util.showBottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
lateinit var btnSearch: Button
@SuppressLint("StaticFieldLeak")
lateinit var edtSearch: EditText

class SearchFragment: Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var bestProductAdapter: BestProductAdapter
    private val productList = mutableListOf<Product>()

    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val url : String = "${URL}search.php"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        btnSearch = binding.btnSearch
        edtSearch = binding.edtSearch
        btnSearch.setOnClickListener{
            addProductToCart()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBestProductRV()
        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_searchFragment_to_productDetails, b)
        }
    }

    private fun setupBestProductRV() {
        bestProductAdapter = BestProductAdapter()
        binding.productRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun addProductToCart(){
        val param = edtSearch.text.toString()
        val temp = "%$param%"
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                println("return response: $response")
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = Product().apply {
                            productID = jsonObject.getInt("productID")
                            categoryID = jsonObject.getInt("categoryID")
                            productName = jsonObject.getString("productName")
                            color = jsonObject.getString("color").split(",")?.toMutableList()
                                ?: mutableListOf()
                            size = jsonObject.getString("size").split(",")?.toMutableList()
                                ?: mutableListOf()
                            description = jsonObject.getString("description")
                            sellPrice = jsonObject.getDouble("sellPrice")
                            discount = jsonObject.getDouble("discount")
                            imageURL = jsonObject.getString("imageURL").split(",")?.toMutableList()
                                ?: mutableListOf()
                        }
                        val existingProduct = productList.find { it.productName == product.productName}
                        if (existingProduct != null){
                            existingProduct.color?.let{ existingColors ->
                                product.color?.forEach { newColor ->
                                    if (!existingColors.contains(newColor)) {
                                        existingColors.add(newColor)
                                    }
                                }
                            }
                            existingProduct.size?.let { existingSizes ->
                                // Add new sizes if they do not exist
                                product.size?.forEach { newSize ->
                                    if (!existingSizes.contains(newSize)) {
                                        existingSizes.add(newSize)
                                    }
                                }
                            }
                            existingProduct.imageURL?.let { existingImages ->
                                // Add new images if they do not exist
                                product.imageURL?.forEach { newImage ->
                                    if (!existingImages.contains(newImage)) {
                                        existingImages.add(newImage)
                                    }
                                }
                            }
                        }else{
                            productList.add(product)
                        }
                        bestProductAdapter.submitProductList(productList)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): MutableMap<String, String> { //pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["param1"] = temp
                paramV["param2"] = temp
                println(paramV)
                return paramV
            }
        }
        queue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }

}