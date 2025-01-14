package com.example.comp4342mobilecomputinggroupproject.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.helper.UserInfo
import org.json.JSONObject


@SuppressLint("StaticFieldLeak")
lateinit var applePay : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var bitcoin : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var mastercard : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var visa : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var wechatPay : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var alipay : ImageButton
@SuppressLint("StaticFieldLeak")
lateinit var backButton : ImageButton

class PaymentActivity : ComponentActivity() {

    private val URL: String = ConnectionHelper.cURL + "/mobileproject/"
    private val createInvoiceURL: String = "${URL}Create_Invoice.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_payment)

        applePay = findViewById(R.id.applePay)
        bitcoin = findViewById(R.id.bitcoin)
        mastercard = findViewById(R.id.mastercard)
        visa = findViewById(R.id.visa)
        wechatPay = findViewById(R.id.wechatPay)
        alipay = findViewById(R.id.alipay)
        backButton = findViewById(R.id.backButton)


        applePay.setOnClickListener(){createInvoice("applePay")}
        bitcoin.setOnClickListener(){createInvoice("bitcoin")}
        mastercard.setOnClickListener(){createInvoice("mastercard")}
        visa.setOnClickListener(){createInvoice("visa")}
        wechatPay.setOnClickListener(){createInvoice("wechatPay")}
        alipay.setOnClickListener(){createInvoice("alipay")}
        backButton.setOnClickListener(){
            finish()
        }
    }

    private fun createInvoice(paymentType: String) {
        val queue = Volley.newRequestQueue(applicationContext)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, createInvoiceURL,
            Response.Listener<String> { response -> println("return response: $response") //get response & print response
                try { //catch JSONException
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("error") // pass error msg
                    println("status: $status")
                    if (status.equals("Input empty")) { // not empty input
                        // TODO: pop up a dialog to show the response
                        okDialog("Error!","Input empty" )
                    }
                    else if (status.equals("Shipping address is empty")) {
                        // TODO: pop up a dialog to show the response
                        okDialog("Error!","Shipping address is empty" )
                    }
                    else if (status.equals("Cart empty")) {
                        // TODO: pop up a dialog to show the response
                        okDialog("Error!","Your cart is empty" )
                    }

                    else if (status.equals("Product out of stock")) {
                        val str = StringBuilder().append("Product Info:\n")
                        var productArray = jsonObject.getJSONArray("productOutOfStock")
                        for (i in 0 until productArray.length()) {
                            val productObj = productArray.getJSONObject(i)
                            val productName = productObj.getString("productName")
                            val size = productObj.getString("selectedSize")
                            val color = productObj.getString("selectedColor")
                            val stock = productObj.getInt("stock")
                            str.append(i + 1).append(":\t$productName, size = $size\n\t\tcolor = $color, have stock = $stock\n")
                        }

                        // return 'productID' => $productID, 'stock' => $checkStock
                        // TODO: pop up a dialog to show the response

                        okDialog("Product out of stock!",str.toString())
                    }
                    else if (status.equals("")) {
                        // TODO: pop up a dialog to show the response for success
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@PaymentActivity)
                        builder
                            .setTitle("Success!")
                            .setMessage("You have successfully purchased.")
                            .setPositiveButton("OK") { dialog, which ->
                                // Action to take when OK is clicked
                                dialog.dismiss() // Optionally dismiss the dialog
                                val intent = Intent(
                                    this, ShoppingActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                    }

                } catch (e: Exception){ //catch
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.localizedMessage?.let { Log.e("Error", it) } }) { //response error listener
            override fun getParams(): Map<String, String> { // pass data through request (-> php)
                val paramV: MutableMap<String, String> = HashMap()
                paramV["userID"] = UserInfo.userID
                paramV["paymentType"] = paymentType
                return paramV
            }
        }
        //add request -> queue (run request)
        queue.add(stringRequest)
    }

    private fun okDialog(title:String, Message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@PaymentActivity)
        builder
            .setTitle(title)
            .setMessage(Message)
            .setPositiveButton("OK") { dialog, which ->
                // Action to take when OK is clicked
                dialog.dismiss() // Optionally dismiss the dialog
                finish()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()


    }
}