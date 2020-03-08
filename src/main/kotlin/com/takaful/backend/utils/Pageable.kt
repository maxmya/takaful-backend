package com.takaful.backend.utils

class Pageable<T> {
    var pageAbleList: List<Any> = mutableListOf()
    var pagination: Pagination? = null
    var next = false
}