package com.example.myapplication
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.DeepLinkResponse
import com.example.myapplication.util.Resource
import com.example.myapplication.util.crc32.QRCoder


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    var progressBar : ProgressBar ?=null

    var btnSend : AppCompatTextView ?= null
    var btnDeepLink : AppCompatTextView ?= null

    var apiUrl : EditText?=null
    var checkUrl : EditText?=null

    var responseApiUrl : AppCompatTextView ? =null
    var responseDeepLinkUrl : AppCompatTextView ? =null

    var deepLinkResponse : DeepLinkResponse?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModelProviderFactory = ViewModelProviderFactory()
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)


        btnSend=findViewById(R.id.btn_send)
        btnDeepLink=findViewById(R.id.btn_deeplink)
        apiUrl=findViewById(R.id.apiurl)
        checkUrl=findViewById(R.id.checkurl)
        responseApiUrl=findViewById(R.id.response_apiurl)
        responseDeepLinkUrl=findViewById(R.id.response_deeplink)
        progressBar =findViewById(R.id.progress_bar)

        btnSend!!.setOnClickListener {
             viewModel.getDeepLink("/eimzo/frontend/auth")
        }

        btnDeepLink!!.setOnClickListener {
            val result = QRCoder.make(
                deepLinkResponse!!.siteId,
                deepLinkResponse!!.documentId,
                deepLinkResponse!!.challange
            )
            Log.d("Gosthash", result)
            //viewModel.checkStatus(deepLinkResponse!!.documentId,"/eimzo/frontend/status?documentId=")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("eimzo://sign?qc=$result")
        }

        viewModel.deepLinkResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar!!.visibility = View.GONE
                    response.data?.let { responseData ->
                        print("$responseData")
                        responseApiUrl!!.text =
                            "DokumentId: ${responseData.documentId} \n SiteID: ${responseData.siteId}\n Challange: ${responseData.challange}";
                        deepLinkResponse = responseData
                    }
                }
                is Resource.Error -> {
                    progressBar!!.visibility = View.GONE
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    progressBar!!.visibility = View.VISIBLE
                }
            }
        })


        viewModel.checkStatusResponse.observe(this, Observer { response ->
            when (response) {
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
