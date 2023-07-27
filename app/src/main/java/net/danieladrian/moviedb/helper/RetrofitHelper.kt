package net.danieladrian.moviedb.helper

import android.content.Context
import com.google.gson.Gson
import net.danieladrian.moviedb.rest.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {
    private var retrofit: Retrofit? = null
    private var apiInterface: ApiInterface? = null
    private var httpClient: OkHttpClient? = null

    fun getDefaultInterface(context: Context): ApiInterface? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        try {
            httpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                //                    .sslSocketFactory(getSSLConfigWithMultipleCertificate(this).getSocketFactory())
                .addNetworkInterceptor(interceptor)
                .addInterceptor { chain ->
                    val original = chain.request()

                    Log.send_debug_log("HELLOBILL OwnerApp", "" + original)
                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                        //                        .header("Authorization", basic)
                        .method(original.method(), original.body())

                    val request = requestBuilder.build()

                    Log.send_debug_log("HELLOBILL OwnerApp", "" + request.body()!!)
                    val response = chain.proceed(request)

                    val responseBody = response.body()
                    val bodyString = responseBody!!.string()
                    //Crashlytics.log("URL :"+request.url().toString()+"\nREQUEST BODY:\n"+request.body()+"\nRESPONSE BODY\n"+bodyString);
                    //Crashlytics.logException(new Exception("logData"));
                    try {
                        val resultDefault = Gson().fromJson<ResultDefault>(bodyString, ResultDefault::class.java!!)

                        if (resultDefault != null) {
                            if (resultDefault!!.Errors != null && resultDefault!!.Errors!!.size > 0) {
                                if (resultDefault!!.Status != null && resultDefault!!.Status === 1) {

                                    val error = resultDefault!!.Errors?.get(0)
                                    if (error?.ID.equals("TOKEN",true)) {

                                        httpClient!!.dispatcher().cancelAll()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {

                    }


                    response.newBuilder().body(ResponseBody.create(responseBody.contentType(), bodyString.toByteArray())).build()
                }.build()
        } catch (e: Exception) {

        }

        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.client(httpClient!!)
        retrofitBuilder.baseUrl(ApiConstant.BASE_URL)
        retrofit = retrofitBuilder.build()
        apiInterface = retrofit!!.create(ApiInterface::class.java)
        return apiInterface
    }
}