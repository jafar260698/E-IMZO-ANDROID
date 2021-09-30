package com.example.myapplication
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
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
    var successStatus= false
    var timer=102
    var customCheckUrl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        subscribe()

        btnSend!!.setOnClickListener {
             if(apiUrl!!.text.isNotEmpty()){
                 viewModel.getDeepLink(apiUrl!!.text.toString())
             } else {
                 Toast.makeText(this, "API URL bo'sh bo'lishi mumkin emas", Toast.LENGTH_LONG).show()
             }
        }

        btnDeepLink!!.setOnClickListener {
            if(checkUrl!!.text.isNotEmpty()) {
                val result = QRCoder.make(
                    deepLinkResponse!!.siteId,
                    deepLinkResponse!!.documentId,
                    deepLinkResponse!!.challange
                )
                customCheckUrl = checkUrl!!.text.toString()
                Log.d("Gosthash", result)
                recursion(timer)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("eimzo://sign?qc=$result")
                startActivity(intent)
            } else {
                Toast.makeText(this, "CHECK URL bo'sh bo'lishi mumkin emas", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun recursion(i: Int){
        print("Counter: $i")
        if (!successStatus) {
            if (i > 0) {
                viewModel.checkStatus("$customCheckUrl?documentId=${deepLinkResponse!!.documentId}")
                val timer = object: CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }
                    override fun onFinish() {
                        timer -= 3
                        recursion(timer)
                    }
                }
                timer.start()
            } else {
                print("Tizimga kirish ma'lumotlari yangilandi. Boshqadan urinib ko'ring")
                Toast.makeText(
                    this,
                    "Tizimga kirish ma'lumotlari yangilandi. Boshqadan urinib ko'ring",
                    Toast.LENGTH_LONG
                ).show()

                return
            }
        } else {
            timer = 0
            print("Muvaffaqiyatli bajarildi");
            responseDeepLinkUrl!!.text="Muvaffaqiyatli bajarildi";
            Toast.makeText(this, "Muvaffaqiyatli bo'ldi", Toast.LENGTH_LONG).show()

            // getUserDataByDocumentID();
            return;
        }
    }

    private fun initView() {
        val viewModelProviderFactory = ViewModelProviderFactory()
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)

        btnSend=findViewById(R.id.btn_send)
        btnDeepLink=findViewById(R.id.btn_deeplink)
        apiUrl=findViewById(R.id.apiurl)
        checkUrl=findViewById(R.id.checkurl)
        responseApiUrl=findViewById(R.id.response_apiurl)
        responseDeepLinkUrl=findViewById(R.id.response_deeplink)
        progressBar =findViewById(R.id.progress_bar)

    }

    private fun subscribe() {
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
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
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
                    progressBar!!.visibility = View.GONE
                    response.data?.let { responseData ->
                        if (responseData.status == 1) {
                            successStatus = true
                        }
                    }
                }
                is Resource.Error -> {
                    progressBar!!.visibility = View.GONE
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    progressBar!!.visibility = View.VISIBLE
                }
            }
        })
    }
}
