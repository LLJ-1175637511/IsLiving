package com.llj.living.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.llj.living.R
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val str = """<html>
<body>

<h1>My First Heading</h1>

<p>My first paragraph.</p>

</body>
</html>"""
        webView.loadData(str, "text/html", null)

    }
}