package com.example.myapplication
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.model.DeepLinkResponse
import com.example.myapplication.model.StatusModel
import com.example.myapplication.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel() : ViewModel() {

     val deepLinkResponse : MutableLiveData<Resource<DeepLinkResponse>> = MutableLiveData()
     val checkStatusResponse : MutableLiveData<Resource<StatusModel>> = MutableLiveData()


    fun getDeepLink(url: String) = viewModelScope.launch {
         deepLinkResponse.postValue(Resource.Loading())
         val response = ApiClient.api.getDeepLink(url)
         deepLinkResponse.postValue(handleDeepLinkResponse(response))
    }

    fun checkStatus(documentId: String, url: String) = viewModelScope.launch {
        checkStatusResponse.postValue(Resource.Loading())
        val response = ApiClient.api.checkStatus(documentId, url)
        checkStatusResponse.postValue(handleCheckStatus(response))
    }

    private fun handleDeepLinkResponse(response: Response<DeepLinkResponse>) : Resource<DeepLinkResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleCheckStatus(response: Response<StatusModel>) : Resource<StatusModel> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}