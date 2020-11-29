package chip8

import scala.io.Source
import java.nio.file.{Files, Paths}



import javafx.embed.swing.JFXPanel
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas

import scalafx.scene.paint.Color
import scalafx.scene.control.Button
import scalafx.scene.layout.{ Pane, BorderPane }
import scalafx.scene.layout.HBox

import scalafx.scene.layout.Pane

import scalafx.stage.Stage
import scalafx.animation.AnimationTimer 
import scalafx.scene.input.KeyEvent 
import scalafx.Includes._
class Chip8(programPath: String){
    val rawProgram :Array[Byte] = Files.readAllBytes(Paths.get(programPath))
    val memory: Memory = Memory(rawProgram)
    val display = ConcreteDisplay(64, 32,10)
    val cpu: CPU = CPU(memory, display)
    val CLOCK_GHZ = 500
    val FPS = 60 
    println("Chip8 built.")
    var prevTime : Long = 0

    def run() = {
        new JFXPanel()
        // Create a dialog stage and display it on JavaFX Application Thread
        Platform.runLater {

            // Create dialog
            val dialogStage = new Stage {
            outer =>
            var counter = 0
            title = "Scachip-8"
                scene = new Scene(640,320) {
                    root = new BorderPane{
                        center = (display.imageView)
                    }

                    onKeyPressed = (e:KeyEvent) => {
                        cpu.handlePress(e.code)
                    }

                    
                    onKeyReleased = (e:KeyEvent) =>     {
                        cpu.handleRelease(e.code)
                    }
                    val timer = AnimationTimer(t => {
                        if( (t - prevTime)> 1e9 / FPS ){
                            for(i <- 0 until CLOCK_GHZ / FPS ){
                                cpu.cycle()
                            }
                            if(cpu.delay > 0){
                                cpu.delay -= 1
                            }
                            prevTime = t
                            if(display.shouldDraw()){
                                display.updateImage()
                            }
                        }
                    })
                    timer.start()
                }
            }
            // Show dialog and wait till it is closed
            dialogStage.showAndWait()
            // Force application exit
            Platform.exit()
        }
    }
}

object Chip8{
    //def apply(programPath: String = "resources/roms/test_opcode.ch8"): Chip8 = new Chip8(programPath)
    //def apply(programPath: String = "resources/roms/Space Invaders [David Winter].ch8"): Chip8 = new Chip8(programPath)
    //def apply(programPath: String = "resources/roms/pong.rom"): Chip8 = new Chip8(programPath)
    def apply(programPath: String = "resources/roms/flightrunner.ch8"): Chip8 = new Chip8(programPath)
    
    //def apply(programPath: String = "resources/roms/octojam1title.ch8"): Chip8 = new Chip8(programPath)

}