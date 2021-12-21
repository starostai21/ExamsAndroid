package com.example.cinemafilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cinemafilm.common.MyApp
import com.example.cinemafilm.films.MainActivity
import org.json.JSONObject
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    lateinit var eMail: EditText
    lateinit var password: EditText
    lateinit var name: EditText
    lateinit var surName: EditText
    lateinit var app: MyApp
    lateinit var math: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        app = applicationContext as MyApp
        eMail = findViewById(R.id.eMail)
        password = findViewById(R.id.password)
        name = findViewById(R.id.name)
        surName = findViewById(R.id.surName)
        val re = Regex("""[a-z0-9]+@[a-z0-9]\.[a-z]{1,3}$""")
        math = re.find(eMail.text.toString()).toString()
    }

    fun haveAcount(view: android.view.View) {
        startActivity(Intent(this, SignInActivity:: class.java))
    }

    fun logIn(view: android.view.View) {
        if (eMail.text.isEmpty() && password.text.isEmpty() && math != null && name.text.isEmpty() && surName.text.isEmpty()) {
            HttpHelper.requestPOST(
                "http://s4a.kolei.ru/login",
                JSONObject().put("username", eMail.text)
                    .put("password", password.text)
                .put("name", name.text)
                    .put("surName", surName.text),
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