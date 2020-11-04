package chip8

class Stack{
    private var SP: Int = -1

    private val stack: Array[Short] = Array.ofDim(16)

    def pushStack(v: Int): Unit = {
            if(SP >= 15){
                throw new StackOverflowError("StackOverflow")
            }
            SP += 1

            stack.update(SP, v.toShort)
    }
        
    def popStack(): Int = {
        if(SP < 0){
            throw new IllegalStateException("StackUnderflow")
        }
        val v = stack(SP)
        SP -= 1
        v
    }  
}

object Stack{
    def apply() = new Stack()
}