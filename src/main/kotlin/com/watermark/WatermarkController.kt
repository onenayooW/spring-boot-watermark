package com.watermark

import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@RestController()
@RequestMapping("/api")
class WatermarkController {

    @PostMapping(
        path = ["/add/watermark"],
        consumes = ["multipart/form-data"],
        produces = ["image/jpeg"]
    )
    fun addWatermark(@RequestPart("file") file: MultipartFile): ResponseEntity<ByteArray> {
        val originalImage = ImageIO.read(file.inputStream)

        val watermarkResource = ClassPathResource("templates/logo/truck2hand-logo.png")
        val watermarkImage = ImageIO.read(watermarkResource.inputStream)

        val watermarkedImage = addWatermarkToImage(originalImage, watermarkImage)

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(watermarkedImage, "jpg", outputStream)

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(outputStream.toByteArray())
    }

    private fun addWatermarkToImage(image: BufferedImage, watermark: BufferedImage): BufferedImage {
        val graphics = image.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)

        val watermarkWidth = image.width / 2 // Adjust watermark size
        val watermarkHeight = watermarkWidth * watermark.height / watermark.width
//        center
        val watermarkX = (image.width - watermarkWidth) / 2
        val watermarkY = (image.height - watermarkHeight) / 2

//        right down
//        val watermarkX = (image.width - watermarkWidth)
//        val watermarkY = (image.height - watermarkHeight)
//        ------------------------------------------------------------------------------

        graphics.drawImage(watermark, watermarkX, watermarkY, watermarkWidth, watermarkHeight, null)

        graphics.dispose()

        return image
    }

}