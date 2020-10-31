
import utest._
import chip8._
import java.nio.file.{Files}
import scala.io.Source
import scala.util.Random
object DecoderTests extends TestSuite{
  def parser(filePath: String) = {
    def rand(n: Int) = Random.nextInt(Math.pow(16, n).toInt).toHexString
    val data = Source.fromFile(filePath).getLines.toList
    data.map(_.split("-"))
        .map(_.map(_.trim()))
        .map{case str@Array(opcode, instruction) => {
            val needles = List("nnn", "xyn", "xkk", "xy", "x")
            needles.find(opcode.substring(2).contains) match  {
              case Some(needle@"nnn") => 
                val newCodes =rand(needle.size)
                val newInstruction = instruction.replace("(",  s"(${Integer.parseInt(newCodes,16)}")
                val newOpCode = opcode.replace(needle, newCodes)
                (newOpCode, newInstruction)
              case Some(needle@"xyn") => 
                val (x, y, n )= (rand(1),rand(1) ,rand(1))
                val newInstruction = instruction.replace("(", "("+ s"${Integer.parseInt(x,16)},${Integer.parseInt(y,16)},${Integer.parseInt(n,16)}")
                val newOpCode = opcode.replace(needle, (x + y + n))
                (newOpCode, newInstruction)
              case Some(needle@"xkk") => 
                val (x, kk )= (rand(1), rand(1))
                val newInstruction = instruction.replace("(", s"(${Integer.parseInt(x,16)},${Integer.parseInt(kk,16)}")
                val newOpCode = opcode.replace(needle, x + kk)
                (newOpCode, newInstruction)
              case Some(needle@"xy") => 
                val (x, y )= (rand(1), rand(1))
                val newInstruction = instruction.replace("(",  s"(${Integer.parseInt(x,16)},${Integer.parseInt(y,16)}")
                val newOpCode = opcode.replace(needle, x + y)
                (newOpCode, newInstruction)
              case Some(needle@"x") => 
                val (x, kk )= (rand(1), rand(1))
                val newInstruction = instruction.replace("(",  s"(${Integer.parseInt(x,16)}")
                val newOpCode = opcode.replace(needle, x)
                (newOpCode, newInstruction)
              case _ => 
                (opcode, instruction)  
            }
          }
        }      
  }
  def checkInstruction(x: Short, i: Instruction) =  Decoder.decode(x) match{
        case `i` => 
        case x  => throw new Exception(s"Wrong Instruction. Found $x")
      }
  val tests = Tests{

    test("SYS decoded"){
     checkInstruction(0x0000, SYS(0))
    }
    test("CLS decoded"){
     checkInstruction(0x00E0, CLS())
    }
    test("RET decoded"){
      checkInstruction(0x00EE, RET())
    }

    test("LDI decoded"){
      checkInstruction(0xA2EA.toShort, LDI(0x2EA))
    }
    test("All opcodes correctly decoded with random operands"){
      parser("resources/test/opcodes_test.txt").foreach{case(opcode, instruction) =>
                                            assertMatch(Decoder.decode(Integer.parseInt(opcode.substring(2),16)).toString())
                                              {case instruction =>}
                                         }
    }
  }
}