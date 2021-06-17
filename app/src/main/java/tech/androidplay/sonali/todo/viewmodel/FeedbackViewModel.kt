package tech.androidplay.sonali.todo.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.BR
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.model.Feedback
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@HiltViewModel
class FeedbackViewModel @Inject constructor(private val dataSource: TodoRepository) :
    ViewModel(), Observable {

    private val registry = PropertyChangeRegistry()

    private var _feedbackLiveResponse = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val feedbackLiveResponse: LiveData<ResultData<*>> get() = _feedbackLiveResponse

    @get: Bindable
    var feedback = Feedback()
        set(value) {
            if (value != field) field = value
            registry.notifyChange(this, BR.feedback)
        }


    fun sendFeedback() {
        _feedbackLiveResponse.postValue(ResultData.Loading)
        val topic = feedback.topic.toString()
        val description = feedback.description.toString()
        if (topic.isNotEmpty() && description.isNotEmpty()) {
            viewModelScope.launch {
                val response = dataSource.provideFeedback(topic, description)
                _feedbackLiveResponse.postValue(response)
            }
        } else _feedbackLiveResponse.postValue(ResultData.Failed("Field can not be empty!"))
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}