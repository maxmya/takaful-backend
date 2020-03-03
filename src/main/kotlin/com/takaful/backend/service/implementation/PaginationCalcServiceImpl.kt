package com.takaful.backend.service.implementation

import com.takaful.backend.service.freamwork.PaginationCalcService
import org.springframework.stereotype.Service

@Service
class PaginationCalcServiceImpl : PaginationCalcService {

    override fun getListAfterPaging(listToBePaged: List<Any>, startPage: Int, size: Int): Pageable<*> {
        val pagination = Pagination()
        var next: Boolean
        val lastPage: Int
        var startIndex = 0
        var endIndex = listToBePaged.size
        val pageable: Pageable<*> = Pageable<Any>()
        val tempList: List<Any>

        pagination.currentPage=startPage
        pagination.pageSize=size
        val allListSize = listToBePaged.size
        if (allListSize != 0 && allListSize <= size) {
            next = false
            if (listToBePaged.size % size == 0) {
                lastPage = listToBePaged.size / size
                pagination.lastPage=lastPage
            } else {
                lastPage = listToBePaged.size / size + 1
                pagination.lastPage=lastPage
            }
            pagination.pageSize=listToBePaged.size
            if (startPage > lastPage || startPage <= 0) {
                pageable.next = false
                pageable.pagination = pagination
                pageable.pageAbleList = emptyList()
                return pageable
            }
            pageable.next = next
            pageable.pagination = pagination
            pageable.pageAbleList = listToBePaged
        } else if (allListSize > size) {
            next = true
            startIndex = (startPage - 1) * size
            if (startIndex < 0) {
                startIndex = 0
            }
            endIndex = startIndex + size
            if (endIndex >= listToBePaged.size) {
                endIndex = listToBePaged.size
                next = false
            }
            if (listToBePaged.size % size == 0) {
                lastPage = listToBePaged.size / size
                pagination.lastPage=lastPage
            } else {
                lastPage = listToBePaged.size / size + 1
                pagination.lastPage=lastPage
            }
            if (startPage > lastPage || startPage <= 0) { /* log.debug("hi");
                return null;*/
                pageable.next = false
                pageable.pagination = pagination
                pageable.pageAbleList = emptyList()
                return pageable
            }
            tempList = listToBePaged.subList(startIndex, endIndex)
            pagination.pageSize=tempList.size
            pageable.next = next
            pageable.pagination = pagination
            pageable.pageAbleList = tempList
        }
        return pageable
    }

}