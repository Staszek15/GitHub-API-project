import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main(vararg user: String) {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiInterface::class.java)
    val requestCall = apiService.getData("https://api.github.com/users/Staszek15/repos")


    requestCall.enqueue(object : Callback<List<DataItem>> {

        override fun onResponse(call: Call<List<DataItem>>, repoResponse: Response<List<DataItem>>) {
            val responseCode = repoResponse.code()

            if (repoResponse.isSuccessful) {
                val apiResponse = repoResponse.body()
                apiResponse?.let {
                    for (repo in it) {
                        runBlocking {
                            println("Name: ${repo.name}")
                            println("Owner login: ${repo.owner["login"]}")
                            println("Branches url: ${repo.branches_url.substring(0, repo.branches_url.length - 9)}")
                            getBranches(repo.branches_url.substring(0, repo.branches_url.length - 9))
                            println("---------")
                        }
                    }
                }
            } else {
                val errorBody = repoResponse.errorBody()?.string()
                if (responseCode == 404) {
                    val jsonResponse = Gson().toJson(ErrorResponse(responseCode, errorBody))
                    println(jsonResponse)
                } else {
                    println("Response not successful. Response code: $responseCode")
                }
            }
        }

        override fun onFailure(call: Call<List<DataItem>>, t: Throwable) {
            t.printStackTrace()
        }
    })
}