package com.takaful.backend.service.implementation

import com.takaful.backend.service.freamwork.FilesStorageService
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import javax.annotation.PostConstruct

@Service
class FilesStorageServiceImpl : FilesStorageService {
    private val root = Paths.get("uploads")
    @PostConstruct
    override fun init() {
        try {
            if (Files.notExists(root)) Files.createDirectory(root)
        } catch (e: IOException) {
            throw RuntimeException("Could not initialize folder for upload!")
        }
    }
// saves and return file path
    override fun save(file: MultipartFile?): String {
        val fileName = StringUtils.cleanPath(file?.originalFilename!!)

        try {
            Files.copy(file.inputStream, this.root.resolve(file.originalFilename), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            throw RuntimeException("Could not store the file. Error: " + e.message)
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileName)
                .toUriString()
    }

    override fun load(filename: String?): Resource? {
        return try {
            val file = root.resolve(filename)
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw RuntimeException("Could not read the file!")
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException("Error: " + e.message)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile())
    }

    override fun loadAll(): Stream<Path?>? {
        return try {
            Files.walk(this.root, 1).filter { path: Path -> path != this.root }.map { other: Path? -> this.root.relativize(other) }
        } catch (e: IOException) {
            throw RuntimeException("Could not load the files!")
        }
    }
}