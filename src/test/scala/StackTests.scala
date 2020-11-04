import utest._
import chip8._

object StackTests extends TestSuite{
    val tests = Tests{
        test("StackOverflow throws exception"){
            val stack = Stack()
            val e = intercept[StackOverflowError]{
                for(i<- 0 to 16){
                stack.pushStack(i.toByte)
                }
            }
        }

        test("StackUnderflow throws exception"){
            val stack = Stack()
            val e = intercept[Exception]{
                for(i<- 0 to 16){
                stack.popStack()
                }
            }
        }

        test("Stack push pop"){
            val stack = Stack()

            for(i<- 0 to 15){
                stack.pushStack(i)
            }
            for(i<- (0 to 15).reverse){
                assertMatch(stack.popStack()){
                    case i => 
                }
            }

        }

    }
}