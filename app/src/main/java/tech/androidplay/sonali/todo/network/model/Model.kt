package tech.androidplay.sonali.todo.network.model

import tech.androidplay.sonali.todo.network.model.Customer

data class Model(
    val customer: Customer,
    val message: String,
    val status: Int
)