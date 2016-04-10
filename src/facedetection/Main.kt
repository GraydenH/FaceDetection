package facedetection

import facerecog.FaceImage
import facerecog.FaceRecognition
import gui.AverageFace
import gui.EigenFaces
import gui.FaceLibrary
import gui.menu.MainMenu
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.activation.MimetypesFileTypeMap
import javax.imageio.ImageIO
import javax.swing.GrayFilter
import javax.swing.JFrame
import javax.swing.JMenuBar
import javax.swing.JTabbedPane

/**
 * Created by Grayden on 3/27/2016.
 */

/**
 * starting point of the jvm.
 * @param args
 */
fun main(args: Array<String>) {
    val faceRecog = FaceRecog()
    faceRecog.isVisible = true
}

/**
 * Get the image files in a given folder.
 * @param folder Given folder
 * *
 * @return all the images in a given folder
 */
fun getImages(folder: String): ArrayList<BufferedImage> {
    val dir = File(folder)
    val result = arrayListOf<BufferedImage>()

    if (dir.exists() && dir.isDirectory)
        dir.listFiles()
                .filter { isImage(it) }
                .drop(22)
                .take(4)
                .forEach { result.add(getImage(it)) }

    return result
}

fun getImage(file: File): BufferedImage =
        ImageIO.read(file)

fun isImage(file: File): Boolean {
    val mimetype = MimetypesFileTypeMap().getContentType(file)
    val type = mimetype
            .split("/".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[0]

    return if (type == "image")
        true
    else
        false
}

fun scaleImage(img: BufferedImage, w: Int, h: Int): BufferedImage {
    val result = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    var x = 0
    var y: Int

    while (x < w) {
        y = 0
        while (y < h) {
            val col = img.getRGB(x * img.width / w, y * img.height / h)
            result.setRGB(x, y, col)
            y++
        }
        x++
    }

    return result
}

class FaceRecog : JFrame() {
    val WIDTH = 600
    val HEIGHT = 450

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(WIDTH, HEIGHT)
        setLocationRelativeTo(null)

        val pane = JTabbedPane()
        val images = getImages("./images/frames")

        val detectFaces = DetectFace(images)

        //val faceProcessor = FaceRecognition()
        //val library = FaceLibrary(faceProcessor, WIDTH)
        //val averageFace = AverageFace(faceProcessor)
        //9val eigenFaces = EigenFaces(faceProcessor)

        // pane.add("Face Library", library)
        // pane.add("Average face", averageFace)
        // pane.add("Eigen Faces", eigenFaces)
        pane.add("Find Faces", detectFaces)

        val menu = JMenuBar()//MainMenu(faceProcessor, pane)
        menu.add(pane)
        jMenuBar = menu
        contentPane = pane
    }
}