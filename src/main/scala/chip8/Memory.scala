package chip8

class Memory(program: Array[Byte],   programStart: Int = 0x200){

    private val memory: Array[Byte] = Array.ofDim(4096)

    
    def loadProgram() = program.zipWithIndex.foreach{case (byte, idx) => set(idx + programStart, byte)}

    //Need to be careful with unsigned integers. JVM does not support them. Since arithmetic operations often return (signed) integers
    //we need to use a maske to make sure that values are not affected.
    def getWord(idx: Int): Short = (memory(idx )   << 8  |  memory(idx + 1) & 0xFF ).toShort

    def get(idx: Int): Byte = memory(idx)

    def setWord(idx: Int, value: Short) = {
        memory.update(idx, (value >> 8 & 0xFF).toByte)
        memory.update(idx + 1, (value & 0xFF).toByte)
    }

    def set(idx:Int, value: Byte) = memory.update(idx, value)

    
}


object Memory {
    def apply(program: Array[Byte]):  Memory = {
        val m = new Memory(program)
        println("load")
        m.loadProgram()
        m
    }
}