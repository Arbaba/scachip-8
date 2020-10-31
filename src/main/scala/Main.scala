import scala.io.Source
import java.nio.file.{Files, Paths}
import chip8.{Chip8}
object Main extends App {
  
  val chip8 = Chip8()
  chip8.run()
}