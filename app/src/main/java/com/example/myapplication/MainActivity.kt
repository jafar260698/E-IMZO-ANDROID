package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.crc32.QRCoder
import com.example.myapplication.ghostFile.GostHashJava

class MainActivity : AppCompatActivity() {

    var btn : AppCompatTextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Map<String, AuthInfo> testSet = new HashMap();
//        testSet.put("860bD44CD5DCbefdac1710cdb263539b4470ec45bf942d2685756c43aef0223e175c924b943fdc4428d8", new AuthInfo("860b", "D44CD5DC", "B1B3A9E81BA2CFDB34EF7647788F29EECBD7DFEBE77084D0AFC03A76F96FA013"));
//        testSet.put("860b5782C7664a0ca4bc0f98e92c4bac5c81adb3532b74852560b31bedcf254135a2513244ba7c46a307", new AuthInfo("860b", "5782C766", "BD120E18D4D9E4BFA5506FDDF12EAAC6C4CBA4903E1DB6C0AD22E36464B3E3E7"));
//        testSet.put("860b1D622242e5fdb3a7da5faff47b3a03064066d327e18522f3c2e5ef20fb0788f81850d091acd7ca62", new AuthInfo("860b", "1D622242", "CE491727618C408B1FDC3E35B684A83D9ABC62049E705E7BC4E14796BB2485DA"));
//
//        for (String qrCode : testSet.keySet()) {
//            AuthInfo ai = testSet.get(qrCode);
//            String expResult = qrCode;
//            String result = QRCoder.make(ai.siteId, ai.documentId, ai.challenge);
//            System.out.println(expResult.toUpperCase());
//            System.out.println(result.toUpperCase());
//            assertEquals(expResult.toUpperCase(), result.toUpperCase());
//        }

        btn=findViewById(R.id.btn_send)
        btn!!.setOnClickListener {
            val result = QRCoder.make("860b", "D44CD5DC", "B1B3A9E81BA2CFDB34EF7647788F29EECBD7DFEBE77084D0AFC03A76F96FA013");
            Log.d("Gosthash", result)
        }
    }

}
