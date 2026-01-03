package com.krishan.composePractice.pricelineprep.data.repo.books

import android.content.Context
import com.krishan.composePractice.R
import com.krishan.composePractice.pricelineprep.data.remote.dto.books.BookDto
import com.krishan.composePractice.pricelineprep.data.remote.dto.books.toDomain
import com.krishan.composePractice.pricelineprep.domain.model.Book
import com.krishan.composePractice.pricelineprep.domain.repo.BooksRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalStdlibApi::class)
class BooksRepositoryImpl(private val context: Context) : BooksRepository {
    private val books by lazy { parseBookLists() }

    override suspend fun fetchBooks(): Flow<List<Book>> = flow {
        delay(1500)
        emit(books?.map { it.toDomain() } ?: emptyList())
    }

    override suspend fun searchABook(query: String): Flow<List<Book>> = flow {
        emit(
            books
            ?.asSequence()
            ?.filter { it.name.contains(query, ignoreCase = true) }
            ?.map { it.toDomain() }
            ?.toList()
            ?: emptyList())
    }

    fun parseBookLists(): List<BookDto>? {
        val inputString: String = context
            .resources
            .openRawResource(R.raw.books_information)
            .bufferedReader()
            .use { it.readText() }
        return Moshi
            .Builder()
            .build()
            .adapter<List<BookDto>>()
            .fromJson(inputString)
    }
}