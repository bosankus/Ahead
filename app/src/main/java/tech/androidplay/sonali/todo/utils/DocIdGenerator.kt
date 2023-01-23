package tech.androidplay.sonali.todo.utils

import kotlin.random.Random

object DocIdGenerator {

    fun generateDocId(): String {
        val stringLength = 21
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..stringLength).map {
            Random.nextInt(0, charPool.size).let {
                charPool[it]
            }
        }.joinToString("")
    }
}