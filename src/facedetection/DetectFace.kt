package facedetection

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.image.BufferedImage

/**
 * Created by Grayden on 3/27/2016.
 */

class DetectFace(val images: ArrayList<BufferedImage>): JScrollPane() {
    init {
        val vectors = detectFaces(images.mapTo(ArrayList()) { i -> toLuma(toARGB(i)) })
        val panel = FacesPanel(vectors)

        panel.preferredSize = Dimension(vectors.size * vectors[0].width, vectors[0].height)
        setViewportView(panel)
        getVerticalScrollBar().unitIncrement = 16
    }

    private class FacesPanel(val images: ArrayList<BufferedImage>): JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            var off = 0

            images.forEach { g.drawImage(it, off, 0, null); off += it.width }
        }
    }
}

fun detectFaces(imgs: ArrayList<Array<IntArray>>): ArrayList<BufferedImage> {
    val result = ArrayList<BufferedImage>()
    for (i in 0..imgs.size - 2) {
        val img1 = imgs[i]
        val img2 = imgs[i + 1]

        val filter = spatioTemporalFilter(img1, img2)
        val horizontal = findHorizontal(filter)

        val sixth = horizontal.first.y + (horizontal.second.y - horizontal.first.y) / 6
        val vertical = findVertical(filter, sixth)

        val boundary = Pair(
                vertical.second.x - vertical.first.x,
                vertical.first.y - horizontal.first.y)

        val x1 =(vertical.first.x - boundary.first * 0.2).toInt();
        val y1 =(horizontal.first.y - boundary.second * 0.2).toInt();
        val w1 = (boundary.first * 1.44).toInt();
        val h1 = (boundary.second * 2.44).toInt();
        val r1 = Array(w1) { IntArray(h1) }
        val r2 = Array(w1) { IntArray(h1) }
/*
        for (x in x1..x1 + w1 - 1) {
            for (y in y1..y1 + h1 - 1) {
                r1[x - x1][y - y1] = img1[x][y]
                r2[x - x1][y - y1] = img2[x][y]
            }
        }

        result.add(scaleImage(toImage(r1), 256, 256))
        result.add(scaleImage(toImage(r2), 256, 256))
*/
        val r = toImage(filter)
        val g = r.graphics
        g.color = Color.RED
        g.drawRect(
                (vertical.first.x - boundary.first * 0.2).toInt(),
                (horizontal.first.y - boundary.second * 0.2).toInt(),
                (boundary.first * 1.44).toInt(),
                (boundary.second * 2.44).toInt())
        g.drawOval(horizontal.first.x - 5, horizontal.first.y - 5, 10, 10)
        g.drawOval(horizontal.second.x - 5, horizontal.second.y - 5, 10, 10)
        g.drawLine(horizontal.first.x, horizontal.first.y, horizontal.second.x, horizontal.second.y)
        g.drawOval(vertical.first.x - 5, vertical.first.y - 5, 10, 10)
        g.drawOval(vertical.second.x - 5, vertical.second.y - 5, 10, 10)
        g.drawLine(vertical.first.x, vertical.first.y, vertical.second.x, vertical.second.y)

        result.add(r)
    }

    return result
}

fun findHorizontal(filter: Array<IntArray>): Pair<Point, Point> {
    val top = Point()
    val bot = Point()
    var dist = -1

    for (i in 0..filter.size - 1) {
        var found = false
        val begin = Point()
        val end = Point()

        for (j in 0..filter[0].size - 1) {
            if (filter[i][j] > 0) {
                if (!found) {
                    found = true
                    begin.x = i
                    begin.y = j
                } else {
                    end.x = i
                    end.y = j

                    val diff = end.y - begin.y
                    if (diff > dist) {
                        dist = diff
                        top.x = begin.x
                        top.y = begin.y
                        bot.x = end.x
                        bot.y = end.y
                    }
                }
            }
        }
    }

    return Pair(top, bot)
}

fun findVertical(filter: Array<IntArray>, j: Int): Pair<Point, Point> {
    val left = Point()
    val right = Point()
    var dist = -1
    var found = false

    for (i in 0..filter.size - 1) {
        if (filter[i][j] > 0) {
            if (!found) {
                found = true
                left.x = i
                left.y = j
            } else {
                right.x = i
                right.y = j
            }
        }
    }

    return Pair(left, right)
}

fun spatioTemporalFilter(img1: Array<IntArray>, img2: Array<IntArray>): Array<IntArray> {
    val result = Array(img1.size) { IntArray(img1[0].size) }

    for (x in 0..img1.size - 1) {
        for (y in 0..img1[0].size - 1) {
            val c = Math.abs(img1[x][y] - img2[x][y])
            result[x][y] = if (c > 25) 255 else 0
        }
    }

    return result
}

fun toARGB(img: BufferedImage): BufferedImage {
    val new = BufferedImage(img.width, img.height,
            BufferedImage.TYPE_INT_ARGB)

    val g = new.createGraphics()
    g.drawImage(img, 0, 0, null)
    g.dispose()

    return new
}

fun toLuma(img: BufferedImage): Array<IntArray> {
    val result = Array(img.width) { IntArray(img.height) }

    for (x in 0..img.width - 1) {
        for (y in 0..img.height - 1) {
            val c = Color(img.getRGB(x, y))
            result[x][y] = (16.0 + 0.257 * c.red + 0.504 * c.green + 0.0988 * c.blue).toInt()
        }
    }

    return result
}

fun toImage(img: Array<IntArray>): BufferedImage {
    val result = BufferedImage(img.size, img[0].size,
            BufferedImage.TYPE_INT_ARGB)

    for (x in 0..result.width - 1) {
        for (y in 0..result.height - 1) {
            val c = img[x][y]
            result.setRGB(x, y, Color(c, c, c).rgb)
        }
    }

    return result
}