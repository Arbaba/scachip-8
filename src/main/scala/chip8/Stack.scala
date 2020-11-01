package chip8

class Stack{
    private var SP: Int = -1

    private val stack: Array[Byte] = Array.ofDim(16)

    def pushStack(v: Byte): Unit = {
            if(SP >= 15){
                throw new StackOverflowError("StackOverflow")
            }
            SP += 1

            stack.update(SP, v)
    }
        
    def popStack(): Byte = {
        if(SP <= 0){
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