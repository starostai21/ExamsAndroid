package com.example.cinemafilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cinemafilm.common.MyApp
import org.json.JSONObject
import java.lang.Exception

class SignInActivity : AppCompatActivity() {

    lateinit var eMailEditText: EditText
    lateinit var passwordEditText: EditText
    private lateinit var math:String
    private lateinit var app: MyApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        eMailEditText = findViewById(R.id.eMailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        app = applicationContext as MyApp
        val re = Regex("""[a-z0-9]+@[a-z0-9]\.[a-z]{1,3}$""")
        math = re.find(eMailEditText.text.toString()).toString()
    }

    fun signIn(view: android.view.View) {
        if (eMailEditText.text.isEmpty() && passwordEditText.text.isEmpty() && math != null) {
            HttpHelper.requestPOST(
                "http://s4a.kolei.ru/login",
                JSONObject().put("username", eMailEditText.text)
                    .put("password", passwordEditText.text),
                mapOf(
                    "Content-Type" to "application/json"
                )
            ) { result, error, code ->
                runOnUiThread {
                    if (result != null) {
                        try {
                            val jsonResp = JSONObject(result)

                            if (!jsonResp.has("notice"))
                                throw Exception("Не верный формат ответа, ожидался объект notice")
                            if (jsonResp.getJSONObject("notice").has("answer"))
                                throw Exception(
                                    jsonResp.getJSONObject("notice").getString("answer")
                                )
                            if (jsonResp.getJSONObject("notice").has("token")) {
                                app.token = jsonResp.getJSONObject("notice").getString("token")
                                runOnUiThread {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(
                                        this,
                                        "Success get token: ${app.token}",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            } else
                                throw Exception("Не верный формат ответа, ожидался объект token")
                        } catch (e: Exception) {
                            runOnUiThread {
                                AlertDialog.Builder(this)
                                    .setTitle("Ошибка")
                                    .setMessage(e.message)
                                    .setPositiveButton("OK", null)
                                    .create()
                                    .show()
                            }
                        }
                    } else
                        AlertDialog.Builder(this)
                            .setTitle("Ошибка http-запроса")
                            .setMessage(error)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                }
            }
        }
        else{
            AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("Поля пустые, или заполненые не верно")
                .setPositiveButton("Cancel", null)
                .create()
                .show()
        }
    }
}