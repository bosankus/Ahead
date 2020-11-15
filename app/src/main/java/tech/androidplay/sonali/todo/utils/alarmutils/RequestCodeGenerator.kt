package tech.androidplay.sonali.todo.utils.alarmutils

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Nov/2020
 * Email: ankush@androidplay.in
 */

@Suppress("UNUSED_CHANGED_VALUE")
fun String.generateRequestCode(): Int {
    var ascii: Int
    var code = 0
    for (i in this.indices - 1) {
        ascii = this[i].toInt()
        code += ascii
    }
    return code
}