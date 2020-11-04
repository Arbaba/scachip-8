package chip8

import scala.io.Source
import java.nio.file.{Files, Paths}

class Chip8(programPath: String){
    println("construct")
    val rawProgram :Array[Byte] = Files.readAllBytes(Paths.get(programPath))
    val memory: Memory = Memory(rawProgram)
    
    val cpu: CPU = CPU(memory, ConcreteDisplay())
    
    println("Chip8 built.")
    def run() = cpu.run()
}

object Chip8{
    //def apply(programPath: String = "resources/roms/test_opcode.ch8"): Chip8 = new Chip8(programPath)
    def apply(programPath: String = "resources/roms/Space Invaders [David Winter].ch8"): Chip8 = new Chip8(programPath)

}