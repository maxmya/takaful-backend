package com.takaful.backend.service.freamwork

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

interface FilesStorageService {
    fun storeFile(file: MultipartFile): String
    fun storeFile(file: InputStream, filename: String): String
    fun loadFileAsResource(filePath: String): Resource
}