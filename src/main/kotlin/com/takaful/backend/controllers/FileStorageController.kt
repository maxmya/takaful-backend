package com.takaful.backend.controllers

import com.takaful.backend.service.freamwork.FilesStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/storage")
class FileStorageController @Autowired constructor(val filesStorageService: FilesStorageService) {


    @GetMapping("/downloadFile/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val resource: Resource = filesStorageService.loadFileAsResource(fileName)
        var contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }

}