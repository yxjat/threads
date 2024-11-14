package com.example.threads.data

import com.example.threads.data.model.LoggedInUser
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class ResponseResult{
    private var authToken: String = ""

    public fun getAuthToken(): String {
        return authToken
    }
}
class LoginDataSource {
    var token: String? = null
    var tokenType: String? = null
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication

            val user = LoggedInUser(username, username, connectToApi(username, password).getAuthToken())
            return Result.Success(user)
        } catch (e: Throwable) {
            println(e)
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    private fun connectToApi(username: String, password: String): ResponseResult {
        val apiUrl = "https://android-messaging.branch.co/login"
        val formBody = FormBody.Builder().add("username", username).add("password", password).build()
        var request  = Request.Builder().url(apiUrl).post(formBody).build()
        var client = OkHttpClient();
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")


            else {
                val gson = Gson()
                val responseResult: ResponseResult =
                    gson.fromJson(response.body.string(), ResponseResult::class.java)
                println(responseResult)
                return responseResult
            }
        }
    }
}