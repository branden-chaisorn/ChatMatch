package brandenc.com.chatmatch.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import brandenc.com.chatmatch.JsonCreator
import brandenc.com.chatmatch.MainApplication
import brandenc.com.chatmatch.R
import kotlinx.android.synthetic.main.activity_main.*
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

        submit.setOnClickListener {
            Log.v("EditText", chatText.text.toString())
            jsonCreator.createJson(chatText.text.toString(),
                                   object: JsonCreator.JsonConstructionCallback {
                                       override fun onJsonCreatedSuccessful(json: JSONObject) {
                                           runOnUiThread {
                                               jsonOutput.text =
                                                       json.toString(JSON_INDENT)
                                               Log.d("Test", json.toString(JSON_INDENT))
                                           }
                                       }
                                   })
        }
    }
}
