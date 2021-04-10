package tech.androidplay.sonali.todo.data.repository

import tech.androidplay.sonali.todo.data.network.ApiService
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 16/Jan/2021
 * Email: ankush@androidplay.in
 */

/** This is a repository class for all the network calls made to RapidAPI server for qoute */

class QuoteRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchQuote() = apiService.getQuote()
}