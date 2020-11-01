import utest._
import chip8._

object StackTests extends TestSuite{
    val tests = Tests{
        test("StackOverflow throws exception"){
            val stack = Stack()
            intercept[StackOverflowError]{
                for(i<- 0 to 16){
                stack.pushStack(i.toByte)
                }
            }
        }

        test("StackUnderflow throws exception"){
            val stack = Stack()
            intercept[Exception]{
                for(i<- 0 to 16){
                stack.popStack()
                }
            }
        }

    }
}