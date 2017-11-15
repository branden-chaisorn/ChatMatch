package brandenc.com.chatmatch.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.EditText
import android.view.View
import android.widget.Button
import android.widget.TextView
import brandenc.com.chatmatch.JsonCreator
import brandenc.com.chatmatch.MainApplication
import brandenc.com.chatmatch.R
import org.json.JSONObject
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var jsonCreator: JsonCreator
    private val JSON_INDENT = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainApplication.applicationComponent.inject(this)

        val submit = findViewById<View>(R.id.submit) as Button
        val input = findViewById<View>(R.id.chatText) as EditText
        val output = findViewById<View>(R.id.jsonOutput) as TextView

        submit.setOnClickListener {
            Log.v("EditText", input.text.toString())
            jsonCreator.createJson(input.text.toString(),
                                   object: JsonCreator.JsonConstructionCallback {
                                       override fun onJsonCreatedSuccessful(json: JSONObject) {
                                           runOnUiThread {
                                               output.text = json.toString(JSON_INDENT)
                                               Log.d("Test", json.toString(JSON_INDENT))
                                           }
                                       }
                                   })
        }
    }
}
