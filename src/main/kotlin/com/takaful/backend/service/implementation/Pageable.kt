package com.takaful.backend.service.implementation

class Pageable<T> {
    var pageAbleList: List<Any> = mutableListOf()
    var pagination: Pagination? = null
    var next = false
}