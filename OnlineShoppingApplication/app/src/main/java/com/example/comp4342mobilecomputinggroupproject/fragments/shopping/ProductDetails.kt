package com.example.comp4342mobilecomputinggroupproject.fragments.shopping

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.PaymentActivity
import com.example.comp4342mobilecomputinggroupproject.activities.ShoppingActivity
import com.example.comp4342mobilecomputinggroupproject.adapters.ColorAdapter
import com.example.comp4342mobilecomputinggroupproject.adapters.SizesAdapter
import com.example.comp4342mobilecomputinggroupproject.adapters.ViewPager2Images
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentBaseCategoryBinding
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentProductDetailBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo.userID
import com.example.comp4342mobilecomputinggroupproject.util.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import org.json.JSONObject

class ProductDetails : Fragment() {
    val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    val addToCartURL: String = "${URL}addToCart.php"

    private val args by navArgs<ProductDetailsArgs>()
    private lateinit var binding : FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizeAdapter by lazy { SizesAdapter() }
    private val colorAdapter by lazy { ColorAdapter() }
    private var selectedColor : String? = null
    private var selectedSize : String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        binding = FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizeRV()
        setupColorsRV()
        setupViewPager()

        binding.imageClose.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnAddToCart.setOnClickListener{

            if (selectedColor == null && selectedSize == null){
                notSelectedDialog("Error!", "You should select the color and the size you want.")
            }
            else if (selectedColor == null){
                notSelectedDialog("Error!", "You should select the color you want.")
            }
            else if (selectedSize == null){
                notSelectedDialog("Error!", "You should select the size you want.")
            }
            else {
                addProductToCart(
                    userID, product.productID, 1,
                    product.sellPrice!!, selectedSize, selectedColor, product.discount!!
                )
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder
                    .setTitle("Success")
                    .setMessage("Product added to cart.")
                    .setPositiveButton("OK") { dialog, which ->
                        // Action to take when OK is clicked
                        val intent = Intent(
                            requireContext(), ShoppingActivity::class.java
                        )
                        startActivity(intent)
                        dialog.dismiss() // Optionally dismiss the dialog

                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        sizeAdapter.onItemClick = {
            selectedSize = it
        }

        colorAdapter.onItemClick = {
            val index: Int = product.imageURL!!.indexOf(it)
            selectedColor = product.color!!?.get(index)
        }

        binding.apply {
            product.discount?.let {
                val priceAfterOffer = product.sellPrice!! * product.discount!!
                tvProductPrice.text = "$ ${String.format("%.0f", priceAfterOffer)}"
            }
            tvProductName.text = product.productName
            tvProductDescription.text = product.description

            if(product.color.isNullOrEmpty()){
                tvProductColor.visibility = View.INVISIBLE
            }

            if(product.size.isNullOrEmpty()){
                tvProductSizes.visibility = View.INVISIBLE
            }
        }

        viewPagerAdapter.differ.submitList(product.imageURL)
        product.color?.let { colorAdapter.differ.submitList(product.imageURL) }
        product.size?.let { sizeAdapter.differ.submitList(it) }
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRV() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizeRV() {
        binding.rvSizes.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun addProductToCart(userID: String, productID : Int, productQTY : Int, productPrice : Double, selectedSize: String?, selectedColor : String? = null, discount: Double){
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, addToCartURL,
            Response.Listener<String> { response ->
                println("return response: $response")
                try {
                    val jsonObject = JSONObject(response)
                    println(jsonObject.getString("error"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): MutableMap<String, String> { //pass data through request (-> php)
                val totalPrice = productQTY * productPrice!! * discount
                val paramV: MutableMap<String, String> = HashMap()
                paramV["userID"] = userID
                paramV["productID"] = productID.toString()
                paramV["productQTY"] = productQTY.toString()
                paramV["totalPrice"] = totalPrice.toString()
                paramV["selectedColor"] = selectedColor.toString()
                paramV["selectedSize"] = selectedSize.toString()
                println(paramV)
                return paramV
            }
        }
        queue.add(stringRequest)
    }

    private fun notSelectedDialog(title:String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                // Action to take when OK is clicked
                dialog.dismiss() // Optionally dismiss the dialog
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}