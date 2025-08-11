import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object LogIo {
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private suspend fun responseSuspend(method: String, json: JSONObject): String = suspendCancellableCoroutine { cont ->
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://lednevs.ru/hw_api/$method/")
            .post(body)
            .build()

        val call = client.newCall(request)

        cont.invokeOnCancellation {
            call.cancel()
        }

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!cont.isCancelled) {
                    cont.resumeWithException(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val responseBody = it.body?.string()
                    if (it.isSuccessful && responseBody != null) {
                        cont.resume(responseBody)
                    } else {
                        cont.resumeWithException(IOException("HTTP error ${it.code}: $responseBody"))
                    }
                }
            }
        })
    }

    suspend fun loginUser(email: String, password: String): String {
        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        return responseSuspend("user", json)
    }

    suspend fun registerUser(
        email: String,
        password: String,
        className: String,
        loginDn: String,
        passwordDn: String
    ): String {
        val json = JSONObject().apply {
            put("email", email)
            put("class_name", className)
            put("password", password)
            put("login_dn", loginDn)
            put("password_dn", passwordDn)
        }
        return responseSuspend("user/create", json)
    }

    suspend fun checkDiaries(login: String, password: String): String {
        val json = JSONObject().apply {
            put("login", login)
            put("password", password)
        }
        return responseSuspend("mark/check", json)
    }

    suspend fun getDatas(email: String): String {
        val json = JSONObject().apply {
            put("email", email)
        }
        return responseSuspend("user/get_datas", json)
    }

    suspend fun getMarks(login: String, password: String): String {
        val json = JSONObject().apply {
            put("login", login)
            put("password", password)
        }
        return responseSuspend("mark", json)
    }

    suspend fun getAllMarks(login: String, password: String): String {
        val json = JSONObject().apply {
            put("login", login)
            put("password", password)
        }
        return responseSuspend("mark/all", json)
    }

    suspend fun checkAdm(className: String, email: String): String {
        val json = JSONObject().apply {
            put("class_name", className)
            put("email", email)
        }
        return responseSuspend("class/check_admin", json)
    }

    suspend fun addHw(hw: String, subject: String, date: String, className: String): String {
        val json = JSONObject().apply {
            put("hw", hw)
            put("subject", subject)
            put("date", date)
            put("class_name", className)
        }
        return responseSuspend("class/add_hw", json)
    }

    suspend fun getHw(date: String, className: String): String {
        val json = JSONObject().apply {
            put("date", date)
            put("class_name", className)
        }
        return responseSuspend("class/get_hw", json)
    }

    suspend fun getMembers(className: String): String {
        val json = JSONObject().apply {
            put("class_name", className)
        }
        return responseSuspend("class/get_members", json)
    }

    suspend fun addAdmin(className: String, email: String): String {
        val json = JSONObject().apply {
            put("class_name", className)
            put("email", email)
        }
        return responseSuspend("class/add_admin", json)
    }

    suspend fun getCode(email: String): String {
        val json = JSONObject().apply {
            put("email", email)
        }
        return responseSuspend("register", json)
    }

    suspend fun delAdmin(className: String, email: String): String {
        val json = JSONObject().apply {
            put("class_name", className)
            put("email", email)
        }
        return responseSuspend("class/del_admin", json)
    }
}
