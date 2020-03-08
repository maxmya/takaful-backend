package com.takaful.backend.service.freamwork
import com.takaful.backend.service.implementation.*
interface PaginationCalcService {
    fun getListAfterPaging(listToBePaged: List<Any>, startPage: Int, size: Int): Pageable<*>

    }