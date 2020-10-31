import utest._
import chip8._
import scala.language.implicitConversions
object MemoryTests extends TestSuite{
    val tests = Tests{
        val memory = Memory(Array(0x12, 0xEA, 0xCE ,0xAA).map(_.toByte))
       
        test("Memory get"){
            val v = memory.get(0x200)
            val v2 = memory.get(0x201)
            assert(memory.get(0x200)== 0x12.toByte)
            assert(v2== 0xEA.toByte)
            assert(memory.get(0x202)== 0xCE.toByte)
            assert(memory.get(0x203)== 0xAA.toByte)

        }
        test("Memory getWord"){
            val v = memory.getWord(0x200)
            val v2  = memory.getWord(0x202)
            assert(v ==0x12EA.toShort)
            assert(v2== 0xCEAA.toShort)
        }
    }
}