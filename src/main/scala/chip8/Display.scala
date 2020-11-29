package chip8

import javafx.embed.swing.JFXPanel
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.animation.AnimationTimer 
import scalafx.scene.image.ImageView 



import scalafx.scene.paint.Color
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.stage.Stage
import scalafx.scene.image.Image;
import scalafx.scene.image.PixelWriter;
import scalafx.scene.image.WritableImage;

trait Display {
    def bufferDrawing(x: Int, y: Int, sprite: IndexedSeq[Byte]): Boolean
    def shouldDraw(): Boolean
    def updateImage(): Unit
    def clear(): Unit
}

/**
  * 
  *
  * @param width
  * @param height
  * @param scale
  */ 
class ConcreteDisplay(width: Int, height: Int, scale : Int = 10) extends Display{
    //Buffered screen representation
    val screenBuffer = Array.ofDim[Boolean](32, 64)
    for(line <- 0 to 31; col <- 0 to 63) screenBuffer(line)(col) = false

    val scaledWidth =  width * scale

    val scaledHeight = height * scale

    //UI image container
    val imageView = new ImageView()
    
    val wImage = new WritableImage(scaledWidth, scaledHeight)
    
    var isUpdated = false
    
    /**
      * Draws sprites in the screen buffer. Changes will not be visible until updateImage is called.
      * @param x horizontal coordinate of the sprite
      * @param y vertical coordinate of the sprite
      * @param sprite sprite represented by a grid of bits. Each bit will be xored with the screen value at the same position.  
      * @return true if a 
      */ 
    def bufferDrawing(x: Int, y: Int, sprite: IndexedSeq[Byte]): Boolean ={

        isUpdated = true
        var collision = false
        //we need to flip the screen pixel when the corresponding bit is one
        //A sprite is of dim 8 x n where n is sprite.size
        sprite.zipWithIndex.foreach{case(b: Byte, idx: Int) => {
            (0 to 7).zipWithIndex.foreach{case(i, col) => {
                if(((b >> i) & 1) == 1){
                    val pline = (y + idx) % height
                    val pcol = (x + 7 - col) % width
                    val pixel =  screenBuffer(pline)(pcol)
                    screenBuffer(pline)(pcol) = !pixel
                    collision = true
                }
            }}        
        }}
        collision
    } 
    
    /**
      * 
      *
      * @return
      */ 
    def shouldDraw(): Boolean = isUpdated

    /**
      * Flushes the screen updates registered in the buffer.
      */ 
    def updateImage() = {
        isUpdated = false
                val pixelWriter = wImage.getPixelWriter()
        for(i <- 0 until width; j <- 0 until height){
            for(col <- 0 until scale; line <-0 until scale){
                if(screenBuffer(j)(i)){
                   
                    pixelWriter.setArgb(i * scale + col, j * scale + line, (255 << 24) | (255 << 16) | (255 << 8) | 255 )
                }else {
                    pixelWriter.setArgb(i * scale + col, j * scale + line, (255 << 24) )
                }

            }
        }
        imageView.setImage(wImage)
        imageView.setFitWidth(scaledWidth)
    }

    /**
      * Clears the screen buffer.
      */ 
    def clear(): Unit = {
        isUpdated = true
        
        for(i <- 0 until width; j <- 0 until height){
            screenBuffer(j)(i) = false
        }
    }
}

object ConcreteDisplay{
    def apply(w: Int, h: Int, scale: Int) ={
        new ConcreteDisplay(w, h, scale)
    } 
}