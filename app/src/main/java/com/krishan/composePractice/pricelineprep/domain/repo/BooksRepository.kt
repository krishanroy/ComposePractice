package com.krishan.composePractice.pricelineprep.domain.repo

import com.krishan.composePractice.pricelineprep.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun fetchBooks(): Flow<List<Book>>
    suspend fun searchABook(query: String): Flow<List<Book>>
}