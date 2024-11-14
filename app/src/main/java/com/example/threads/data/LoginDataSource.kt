package com.example.threads.data

import com.example.threads.data.model.LoggedInUser
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class ResponseResult (authToken: String){
    private var authToken: String = ""

    init {
        this.authToken = authToken
    }
    public fun getAuthToken(): String {
        return authToken
    }
    fun setAuthToken(authToken: String) {
        this.authToken = authToken
    }
}
class LoginDataSource {

    var token: String? = null
    var tokenType: String? = null
    var client = OkHttpClient();

    fun login(username: String, password: String): Result<LoggedInUser> {

        try {
            // TODO: handle loggedInUser authentication

            val user =
                LoggedInUser(username, username, connectToApi(username, password).getAuthToken())
            return Result.Success(user)
        } catch (e: Throwable) {
            println(e)
            return Result.Error(IOException("Error logging in", e))
        }

    }

    fun logout() {
        // TODO: revoke authentication
    }

    fun connectToApi(username: String, password: String): ResponseResult {
        val apiUrl = "https://android-messaging.branch.co/api/login/"
        val formBody =
            FormBody.Builder().add("username", username).add("password", password).build()
        var request = Request.Builder().url(apiUrl).post(formBody).build()
        var responseResult:ResponseResult = ResponseResult("1243")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
                throw IOException("Unexpected code")
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
                if (!response.isSuccessful) {
                    println(response)
                    throw IOException("Unexpected code $response")
                } else {
                    val gson = Gson()
                     responseResult =
                        gson.fromJson(response.body!!.string(), ResponseResult::class.java)
                    println(responseResult)

                }
            }
        })
        return responseResult
    }
}
