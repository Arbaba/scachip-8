import utest._
import chip8._

object RegisterFileTest extends TestSuite{
  val tests = Tests{
    test("Invalid get index throws exception"){
        val registers = RegisterFile()
        intercept[IllegalArgumentException]{
            registers.getVx(16)
        }
        intercept[IllegalArgumentException]{
            registers.getVx(-1)
        }
    }

    test("Invalid set index throws exception"){
        val registers = RegisterFile()
        intercept[IllegalArgumentException]{
            registers.setVx(-1, 1)
        }
        intercept[IllegalArgumentException]{
            registers.setVx(16, 1)
        }
    }

    test("RegisterFile getter/setter works"){
        val registers = RegisterFile()
        for(i<- 0 to 15){
            registers.setVx(i, i.toByte)
            assertMatch(registers.getVx(i) & 0xFF){
                case i=>
            }
        }
    }

    test("Instruction register getter/setter works"){
        val registers = RegisterFile()
        registers.setI(256.toShort)
        assert(registers.getI() ==256)

    }

  }
}
