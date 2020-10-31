
import utest._
import chip8._

object DecoderTests extends TestSuite{
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
  }
}