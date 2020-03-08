package com.takaful.backend.utils

data class Pagination (
        var currentPage: Int = 0,
        var lastPage : Int = 0,
        var pageSize : Int = 0
)