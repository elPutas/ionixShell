package com.ionix.shelltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.JsonObject
import com.ionix.shelltest.model.DataUser
import com.ionix.shelltest.model.User
import com.ionix.shelltest.services.ServicesJSONPH
import com.ionix.shelltest.services.ServicesIONIX
import com.ionix.shelltest.utils.Encript
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private var disposableJSON: Disposable? = null
    private val jsonServe by lazy {
        ServicesJSONPH.create()
    }

    private var disposableIonix: Disposable? = null
    private val ionixServe by lazy {
        ServicesIONIX.create()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress.visibility = View.GONE

        //listeners
        pagar_btn.setOnClickListener {
            authAlert()
        }

        billetera_btn.setOnClickListener {
            if(checkIfUserCreated(User.name))
            {
                createUser()
            }
        }
    }

    fun checkIfUserCreated(user:String):Boolean
    {
        return if (user.isEmpty()){
            true
        }else{
            showAlert(getString(R.string.err_title), getString(R.string.no_user_msg))
            false
        }
    }


    private fun createUser()
    {
        progress.visibility = View.VISIBLE

        val userInfo = DataUser.UserInfo(
            userName = User.name,
            userEmail = User.email,
            userPhone = User.phone
        )

        disposableJSON = jsonServe.addMe(userInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        result -> userCreated(result)
                },
                {
                        error ->
                    error.message?.let { errorData(it) }
                }
            )
    }

    fun userCreated(data: DataUser.UserObj)
    {
        progress.visibility = View.GONE

        if(User.id.isEmpty())
        {
            User.id = data.id
            showAlert(getString(R.string.succes_title_user_created), "id: ${data.id}")
        }else{
            showAlert(getString(R.string.err_title), getString(R.string.errro_msg_user_created))
        }
    }



    private fun encryptFirst(RUT:String):String
    {
        val encrypt = Encript()
        return encrypt.encriptRut(RUT).trim()
    }

    private fun logWithCredentials(credential: String)  {
        progress.visibility = View.VISIBLE

        disposableIonix = ionixServe.logUser(credential)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        result -> getData(result)
                },
                {
                        error ->
                    error.message?.let { errorData(it) }
                }
            )

    }

    private fun errorData(msg:String)
    {
        progress.visibility = View.GONE
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getData(data: DataUser.Info)
    {
        progress.visibility = View.GONE

        val numContact = 1 //Num contact to show
        val jsonData = data.result.items[numContact] as JsonObject

        val details = jsonData.get("detail") as JsonObject

        val name = jsonData.get("name").toString()
        val email = details.get("email").toString()
        val phoneNumber = details.get("phone_number").toString()

        User.email = email
        User.phone = phoneNumber
        User.name = name

        val title = getString(R.string.user_reg)
        val msg = "Email: $email \nTeléfono: $phoneNumber"

        showAlert(title, msg)
    }


    //Alert
    private fun showAlert(title: String, msg:String)
    {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .show()
    }

    //custom Alert
    private fun authAlert()
    {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(getString(R.string.input_rut))
        val dialogLayout = inflater.inflate(R.layout.dialog_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton(getString(R.string.ok_btn)) {
                _, _ -> this.logWithCredentials(encryptFirst(editText.text.toString()))
        }
        builder.show()
    }

    override fun onPause() {
        super.onPause()
        disposableIonix?.dispose()
        disposableJSON?.dispose()
    }



}