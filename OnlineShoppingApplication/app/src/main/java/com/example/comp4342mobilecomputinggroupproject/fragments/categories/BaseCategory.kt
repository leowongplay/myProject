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
import com.example.comp4342mobilecomputinggroupproject.adapters.BestProductAdapter
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentBaseCategoryBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.interfaces.ProductListAdapter
import com.example.comp4342mobilecomputinggroupproject.util.showBottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import java.net.URL

open class BaseCategory: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    protected val URL: String = ConnectionHelper.cURL + "/mobileproject/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRV()

        setupBestProductRV()

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetails, b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetails, b)
        }

    }

    protected fun setupBestProductRV() {
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    protected fun setupOfferRV() {
        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    protected fun getProduct(url : String, adapter: ProductListAdapter, productList: MutableList<Product>) {
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response -> println("return response: $response")
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = Product().apply {
                            productID = jsonObject.getInt("productID")
                            categoryID = jsonObject.getInt("categoryID")
                            description = jsonObject.getString("description")
                            productName = jsonObject.getString("productName")
                            sellPrice = jsonObject.getDouble("sellPrice")
                            discount = jsonObject.getDouble("discount")
                            imageURL = jsonObject.getString("imageURL").split(",")?.toMutableList()
                            color = jsonObject.getString("color").split(",")?.toMutableList()
                            size = jsonObject.getString("size").split(",")?.toMutableList()
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
                            product.size!!.sortedBy { it.toDouble() }.toString()


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
                    adapter.submitProductList(productList)
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