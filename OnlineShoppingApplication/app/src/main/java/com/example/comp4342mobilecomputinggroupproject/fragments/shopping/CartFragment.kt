package com.example.comp4342mobilecomputinggroupproject.fragments.shopping

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.AddressActivity
import com.example.comp4342mobilecomputinggroupproject.adapters.CartProductCardAdapter
import com.example.comp4342mobilecomputinggroupproject.data.CartProduct
import com.example.comp4342mobilecomputinggroupproject.data.ProductKenny
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentCartBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo
import org.json.JSONObject


class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    //init category recycler view
    private lateinit var prodRecyclerView: RecyclerView
    //init category array list <Category> (for show item)
    private lateinit var prodArrayList: ArrayList<CartProduct>

    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val showCartItemURL: String = "${URL}Show_Cart_Item.php"
    private val updateItemNumberURL: String = "${URL}Update_Item_Number_In_Cart.php"
    private val deleteItemInCartURL: String = "${URL}Detele_Item_In_Cart.php"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        prodRecyclerView =   binding.rvCart
        prodArrayList = ArrayList<CartProduct>() //init arrayList
        getCartData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCheckout.setOnClickListener(){
            val intent = Intent(
                requireContext(), AddressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun buildRecyclerView(){
        //init adapter class
        val productAdapter = CartProductCardAdapter(prodArrayList)

        //build in LinearLayout
        prodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        prodRecyclerView.setHasFixedSize(true)

        //fill arrayList data -> adapter -> recycler view
        prodRecyclerView.adapter = productAdapter //assign categoryAdapter -> RecyclerView
        println("size: ${prodArrayList.size}")

        // Applying OnClickListener to our Adapter
        productAdapter.setOnProductClickListener(object :
            CartProductCardAdapter.OnProductClickListener {
            override fun onClick(position: Int, cartProduct: CartProduct) {
                println("OnProductClickListener:")
                println("MainActivity position: $position")
                println("MainActivity product name: ${cartProduct.product.productName} ${cartProduct.selectedSize} ${cartProduct.selectedColor}")
            }
        })
        productAdapter.setOnImagePlusClickListener(object :
            CartProductCardAdapter.OnImagePlusClickListener {
            override fun onClick(position: Int, cartProduct: CartProduct) {
                println("OnImagePlusClickListener:")
                println("MainActivity position: $position")
                println("MainActivity product name: ${cartProduct.product.productName} ${cartProduct.selectedSize} ${cartProduct.selectedColor}")
                updateItemNumber("+", cartProduct.product.productID)

            }
        })
        productAdapter.setOnImageMinusClickListener(object :
            CartProductCardAdapter.OnImageMinusClickListener {
            override fun onClick(position: Int, cartProduct: CartProduct) {
                println("OnImageMinusClickListener:")
                println("MainActivity position: $position")
                println("MainActivity product name: ${cartProduct.product.productName} ${cartProduct.selectedSize} ${cartProduct.selectedColor}")
                updateItemNumber("-", cartProduct.product.productID)
            }
        })
    }

    private fun getCartData(){
        prodArrayList.clear()
        //get data from database
        showCartItems()
        println("getData size: ${prodArrayList.size}")
    }

    private fun showCartItems() {
        // set connection volley
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.POST,
                showCartItemURL,
                Response.Listener<String> { response ->
                    println("return response: $response")
                    try { // catch JSONException
                        var totalPrice = 0.00
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("error") // pass error msg
                        println("status: $status")
                        if (status.equals("Input empty")) { // not empty input
                            okDialog("Error!",response)
                        }
                        else if (status.equals("Cart is empty")) { // not empty input
                            okDialog("Error!","Cart is empty.")
                        } else if (status.equals("")) {

                            val tempProductArray = jsonObject.getJSONArray("product")
                            for (i in 0 until tempProductArray.length()) {
                                val productObject = tempProductArray.getJSONObject(i)
                                val product =
                                    ProductKenny(
                                        productObject.getInt("productID"),
                                        productObject.getInt("categoryID"),
                                        productObject.getString("categoryName"),
                                        productObject.getString("description"),
                                        productObject.getString("productName"),
                                        productObject.getDouble("sellPrice"),
                                        productObject.getDouble("discount"),
                                        productObject.getInt("remainingQTY"),
                                        productObject.getString("imageURL"),
                                        productObject.getString("color"),
                                        productObject.getString("size"),
                                        productObject.getInt("useStatus"),
                                    )
                                var cartProduct =
                                    CartProduct(
                                        product,
                                        productObject.getInt("productQTY"),
                                        productObject.getDouble("totalPrice"),
                                        productObject.getString("selectedColor"),
                                        productObject.getString("selectedSize"),
                                    )
                                prodArrayList.add(cartProduct)
                                totalPrice += cartProduct.totalPrice
                            }

                        }
                        buildRecyclerView()
                        binding.tvTotalPrice.text = String.format("$ %.2f", totalPrice)
                    } catch (e: Exception) { // catch
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.localizedMessage?.let { Log.e("Error", it) }
                }
            ) { // response error listener
            override fun getParams(): MutableMap<String, String> { // pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["userID"] = UserInfo.userID
                return paramV
            }
        }
        // add request -> queue (run request)
        queue.add(stringRequest)
    }


    private fun updateItemNumber(opt: String, productID: Int) {
        // set connection volley
        val queue = Volley.newRequestQueue(activity?.applicationContext)

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.POST,
                updateItemNumberURL,
                Response.Listener<String> { response ->
                    println("return response: $response")
                    try { // catch JSONException
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("error") // pass error msg
                        println("status: $status")
                        if (response.equals("Input empty")) { // not empty input
                            okDialog("Error!",response)
                        } else {
                            // decode JSON object -> retrieve data(JSON format) from php
                            val jsonObject = JSONObject(response)
                            var productQTY = jsonObject.getString("productQTY")
                            println("productQTY = $productQTY")
                            if(productQTY == "0"){
                                //delete confirmation dialog
                                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                                builder
                                    .setMessage("Product numbers has set to 0, do you want to remove this product from your cart?")
                                    .setTitle("Delete Product")
                                    .setPositiveButton("Delete") { dialog, which ->
                                        //confirm delete
                                        deleteItemInCart(productID)
                                    }
                                    .setNegativeButton("Cancel") { dialog, which ->
                                        //cancel delete
                                    }

                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                            }
                            getCartData()
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
                if (opt == "-") paramV["opt"] = "-"
                else if (opt == "+") paramV["opt"] = "+"
                paramV["userID"] = UserInfo.userID
                paramV["productID"] = productID.toString()
                return paramV
            }
        }
        // add request -> queue (run request)
        queue.add(stringRequest)
    }


    private fun deleteItemInCart(productID: Int) {
        // set connection volley
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST,
                deleteItemInCartURL,
                Response.Listener<String> { response ->
                    println("return response: $response")
                    try { // catch JSONException
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("error") // pass error msg
                        println("status: $status")
                        if (status.equals("Input empty")) { // not empty input
                            okDialog("Error!",response)
                        } else if (status.equals("")) {
                            okDialog("Success!!!","Product removed.")

                            getCartData()
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
                paramV["productID"] = productID.toString()
                return paramV
            }
        }
        queue.add(stringRequest)
    }


    private fun okDialog(title:String, Message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)
            .setMessage(Message)
            .setPositiveButton("OK") { dialog, which ->
                // Action to take when OK is clicked
                dialog.dismiss() // Optionally dismiss the dialog
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}