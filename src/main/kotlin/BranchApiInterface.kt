import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface BranchApiInterface {
    @GET
    fun getData(@Url url: String, @Header("Accept") acceptHeader: String = "application/json"): Call<List<BranchItem>>
}
