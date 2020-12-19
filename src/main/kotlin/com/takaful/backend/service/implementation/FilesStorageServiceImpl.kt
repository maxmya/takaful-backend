package com.takaful.backend.service.implementation

import com.takaful.backend.exceptions.files.FileStorageException
import com.takaful.backend.exceptions.files.MyFileNotFoundException
import com.takaful.backend.service.freamwork.FilesStorageService
import com.takaful.backend.utils.FileStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FilesStorageServiceImpl @Autowired constructor(fileStorageProperties: FileStorageProperties) : FilesStorageService {

    private final val fileStorageLocation: Path = Paths.get(fileStorageProperties.directory)
            .toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (ex: Exception) {
            throw FileStorageException("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }


    override fun storeFile(file: MultipartFile): String {
        // Normalize file name
        val fileName = StringUtils.cleanPath(file.originalFilename.toString())
        return try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
            }
            // Copy file to the target location (Replacing existing file with the same name)
            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (ex: IOException) {
            throw FileStorageException("Could not store file $fileName. Please try again!", ex)
        }
    }


    override fun storeFile(file: InputStream, filename: String): String {
        return try {
            // Check if the file's name contains invalid characters
            if (filename.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $filename")
            }
            // Copy file to the target location (Replacing existing file with the same name)
            val targetLocation = fileStorageLocation.resolve(filename)
            Files.copy(file, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            filename
        } catch (ex: IOException) {
            throw FileStorageException("Could not store file $filename. Please try again!", ex)
        }
    }

    override fun loadFileAsResource(filePath: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(filePath).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw MyFileNotFoundException("File not found $filePath")
            }
        } catch (ex: MalformedURLException) {
            throw MyFileNotFoundException("File not found $filePath", ex)
        }
    }
}