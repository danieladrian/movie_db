package net.danieladrian.moviedb.helper

import android.content.Context
import com.google.gson.Gson
import net.danieladrian.moviedb.BuildConfig
import net.danieladrian.moviedb.rest.ApiConstant
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

                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                        //                        .header("Authorization", basic)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MmExOGUwNGU4NzViNzA4YTAxMTU4NTk1Yzg4ZGZkNyIsInN1YiI6IjU5NTFkOWYwYzNhMzY4MTM4ODAxOWRkNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.X5hArZWwx9bcElQtSrR41giLruyFlIaCMDPprcC6ZNI")
                        .header("accept", "application/json")
                        .method(original.method(), original.body())

                    val request = requestBuilder.build()

                    val response = chain.proceed(request)

                    val responseBody = response.body()
                    val bodyString = responseBody!!.string()
                    //Crashlytics.log("URL :"+request.url().toString()+"\nREQUEST BODY:\n"+request.body()+"\nRESPONSE BODY\n"+bodyString);
                    //Crashlytics.logException(new Exception("logData"));


                    response.newBuilder().body(ResponseBody.create(responseBody.contentType(), bodyString.toByteArray())).build()
                }.build()
        } catch (e: Exception) {

        }

        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.client(httpClient!!)
        retrofitBuilder.baseUrl(ApiConstant.apiBaseUrl)
        retrofit = retrofitBuilder.build()
        apiInterface = retrofit!!.create(ApiInterface::class.java)
        return apiInterface
    }
}