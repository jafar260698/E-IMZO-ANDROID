package com.example.myapplication
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.util.crc32.QRCoder

class MainActivity : AppCompatActivity() {

    var btn : AppCompatTextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn=findViewById(R.id.btn_send)
        btn!!.setOnClickListener {
            val result = QRCoder.make("860b", "D44CD5DC", "B1B3A9E81BA2CFDB34EF7647788F29EECBD7DFEBE77084D0AFC03A76F96FA013");
            Log.d("Gosthash", result)
        }
    }

}
