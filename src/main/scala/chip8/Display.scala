package chip8

import javafx.embed.swing.JFXPanel
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas

import scalafx.scene.paint.Color
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.stage.Stage


trait Display {
    def draw(x: Int, y: Int, sprite: IndexedSeq[Byte]): Boolean
    def clear(): Unit
    def waitKey(): Int
}

class ConcreteDisplay extends Display{
    val screenBuffer = Array.ofDim[Boolean](32, 64)
    for(line <- 0 to 31; col <- 0 to 63) screenBuffer(line)(col) = false
    def draw(x: Int, y: Int, sprite: IndexedSeq[Byte]): Boolean ={
        //canvas.graphicsContext2D.setFill(Color.WHITE)

        //println(s"draw $x $y")
        val string = new StringBuilder()
        var collision = false
        //we need to flip the screen pixel when the corresponding bit is one
        //A sprite is of dim 8 x n where n is sprite.size
        sprite.zipWithIndex.foreach{case(b: Byte, idx: Int) => {
            (0 to 7).zipWithIndex.foreach{case(i, col) => {
                if(((b >> i) & 1) == 1){
                    val pixel =  screenBuffer(y + idx)(x + 7 - col)
                    screenBuffer(y + idx)(x + 7 - col) = !pixel
                    collision = true
                }else{
                }
            }}        
        }}
        //println(string)
        val s2 = new StringBuilder()
        
        for(i <- 0 to 63; j <- 0 to 31){
            if(screenBuffer(j)(i)){
                canvas.graphicsContext2D.setFill(Color.BLACK)
            }else {
                canvas.graphicsContext2D.setFill(Color.WHITE)
            }
            canvas.graphicsContext2D.fillRect(i * scale, j * scale , scale, scale )
        }
        collision
    } 
    def clear: Unit = {
        for(i <- 0 to _width; j <- 0 to _height){
            canvas.graphicsContext2D.pixelWriter.setColor(i,j , Color.WHITE )
        }
    }
    def waitKey(): Int ={
        println("=======WAITKEY======="); 1
    } 
    val scale = 10
    val _width =  64 * scale 
    val _height = 32 * scale
    val canvas =  new Canvas{
        width = _width 
        height = _height
    }


    def start() = {
        new JFXPanel()
        // Create a dialog stage and display it on JavaFX Application Thread
        Platform.runLater {

            // Create dialog
            val dialogStage = new Stage {
            outer =>
            title = "Stand-Alone Dialog"
                scene = new Scene {
                    root = new BorderPane {
                        bottom = canvas
                    }
                }
            }

            // Show dialog and wait till it is closed
            dialogStage.showAndWait()

            // Force application exit
            Platform.exit()
        }
    }

}
object ConcreteDisplay{
    def apply() ={
        val d = new ConcreteDisplay()
        d.start()
        d
    } 
}