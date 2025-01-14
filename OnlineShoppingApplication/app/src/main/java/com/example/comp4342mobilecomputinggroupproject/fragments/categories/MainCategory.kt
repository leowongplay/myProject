package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.adapters.BestDealAdapter
import com.example.comp4342mobilecomputinggroupproject.adapters.BestProductAdapter
import com.example.comp4342mobilecomputinggroupproject.adapters.SpeicalProductsAdapter
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentMainCategoryBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.interfaces.ProductListAdapter
import com.example.comp4342mobilecomputinggroupproject.util.showBottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import java.net.URL


class MainCategory: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpeicalProductsAdapter
    private lateinit var bestDealAdapter: BestDealAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val specialProductList = mutableListOf<Product>()
    private val bestProductList = mutableListOf<Product>()
    private val bestDealList = mutableListOf<Product>()

    val URL: String = ConnectionHelper.cURL + "/mobileproject/"

    val specialProductURL: String = "${URL}main/specialProduct.php"
    val bestDealURL: String = "${URL}main/bestDeal.php"
    val bestProductURL : String = "${URL}main/bestProduct.php"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        getProduct(specialProductURL, specialProductsAdapter, specialProductList)
        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetails, b)
        }
        specialProductList.clear()


        setupBestDealRV()
        getProduct(bestDealURL, bestDealAdapter, bestDealList)
        bestDealAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetails, b)
        }
        bestDealList.clear()

        setupBestProductRV()
        getProduct(bestProductURL, bestProductAdapter, bestProductList)
        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetails, b)
        }
        bestProductList.clear()
    }

    private fun setupBestProductRV() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealRV() {
        bestDealAdapter = BestDealAdapter()
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealAdapter
        }
    }

    private fun setupSpecialProductRv() {
        specialProductsAdapter = SpeicalProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    private fun getProduct(url : String, adapter: ProductListAdapter, productList: MutableList<Product>) {
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> //println("return response: $response")
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = Product().apply {
                            productID = jsonObject.getInt("productID")
                            categoryID = jsonObject.getInt("categoryID")
                            productName = jsonObject.getString("productName")
                            color = jsonObject.getString("color").split(",")?.toMutableList() ?: mutableListOf()
                            size = jsonObject.getString("size").split(",")?.toMutableList() ?: mutableListOf()
                            description = jsonObject.getString("description")
                            sellPrice = jsonObject.getDouble("sellPrice")
                            discount = jsonObject.getDouble("discount")
                            imageURL = jsonObject.getString("imageURL").split(",")?.toMutableList() ?: mutableListOf()
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
                    }
                    if(productList == specialProductList && specialProductList.size >=5 || productList == bestDealList && bestDealList.size >=5){
                        println(productList.size)
                        adapter.submitProductList(productList.subList(0, 5))
                    }else{
                        adapter.submitProductList(productList)
                        println(productList.size)

                    }
                }
                catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) {
        }
        queue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }

}