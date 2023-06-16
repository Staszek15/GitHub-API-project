import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun getBranches(url: String): List<BranchItem> = suspendCoroutine { continuation ->
    val branchRetrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val branchApiService = branchRetrofit.create(BranchApiInterface::class.java)
    val branchRequestCall = branchApiService.getData(url)

    branchRequestCall.enqueue(object : Callback<List<BranchItem>> {
        override fun onResponse(call: Call<List<BranchItem>>, response: Response<List<BranchItem>>) {
            if (response.isSuccessful) {
                val branchResponse = response.body()
                branchResponse?.let {
                    for (branch in branchResponse) {
                        println("Branch name: ${branch.name}")
                        println("Last commit SHA: ${branch.commit["sha"]}")
                    }
                    continuation.resume(it)
                } ?: run {
                    continuation.resumeWithException(NullPointerException("Branch response is null"))
                }
            } else {
                continuation.resumeWithException(Exception("Error retrieving branches"))
            }
        }

        override fun onFailure(call: Call<List<BranchItem>>, t: Throwable) {
            continuation.resumeWithException(t)
        }
    })
}
