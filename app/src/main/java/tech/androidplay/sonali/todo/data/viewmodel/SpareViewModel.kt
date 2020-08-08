package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.ViewModel
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/17/2020, 12:44 AM
 */
class SpareViewModel(fname: String) : ViewModel() {

    private val _name: String = fname
    val name: String
        get() = _name

    init {
        logMessage(_name)
    }
}