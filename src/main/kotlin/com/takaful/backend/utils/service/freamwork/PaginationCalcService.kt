package com.takaful.backend.utils.service.freamwork

import com.takaful.backend.utils.Pageable

interface PaginationCalcService {
    fun getListAfterPaging(listToBePaged: List<Any>, startPage: Int, size: Int): Pageable<*>
}