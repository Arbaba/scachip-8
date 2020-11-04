package chip8

class Memory(program: Array[Byte],   programStart: Int = 0x200){

    private val memory: Array[Byte] = Array.ofDim(4096)
    
    val maxProgramSize  = 3584
    
    val sprites: Array[Byte] = 
        Array(
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
        ).map(_.toByte)

    def loadProgram() = program.zipWithIndex.foreach{case (byte, idx) => set(idx + programStart, byte)}
    def loadSprites() = sprites.zipWithIndex.foreach{case (byte, idx) => set(idx , byte)}

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
        if(program.length > 3584){
            throw new IllegalArgumentException(s"Invalid progam of size ${program.length} Bytes but the maximal size is 3584 Bytes")
        }
        val m = new Memory(program)
        m.loadProgram()
        m.loadSprites()
        m
    }
}