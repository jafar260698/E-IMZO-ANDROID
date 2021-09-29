package com.example.myapplication
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.util.Resource
import com.example.myapplication.util.crc32.QRCoder

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel

    var btn : AppCompatTextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModelProviderFactory = ViewModelProviderFactory()
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)


        btn=findViewById(R.id.btn_send)
        btn!!.setOnClickListener {
        viewModel.getDeepLink("")
        viewModel.checkStatus("","")
//            val result = QRCoder.make("860b", "D44CD5DC", "B1B3A9E81BA2CFDB34EF7647788F29EECBD7DFEBE77084D0AFC03A76F96FA013");
//            Log.d("Gosthash", result)
        }

        viewModel.deepLinkResponse.observe(viewLifecycleOwner, Observer {response->
            when(response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let { responseData ->
                        print("$responseData")
                    }
                }
                is Resource.Error -> {
                    //hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    //showProgressBar()
                }
            }
        })


        viewModel.checkStatusResponse.observe(viewLifecycleOwner, Observer {response->
            when(response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let { responseData ->
                        print("$responseData")
                    }
                }
                is Resource.Error -> {
                    //hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    //showProgressBar()
                }
            }
        })
    }

}
