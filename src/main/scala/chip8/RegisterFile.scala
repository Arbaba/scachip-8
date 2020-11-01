package chip8

class RegisterFile{
    //16 8-bits registers
    private val registers: Array[Byte] = Array.ofDim(16)

    //Memory addresses storage. Only 12 lowest bits are used
    private var I: Short = 0

    private var delay: Byte = 0

    private var sound: Byte  = 0

    def validIndex(i: Int) = i >= 0 && i <= 15
    def getVx(idx: Int): Int = {
        require(validIndex(idx))
        registers(idx) & 0xFF
    }
    def setVx(idx: Int, v: Byte): Unit = {
        require(validIndex(idx))
        registers.update(idx, v)
    }
    def getI(): Int = I & 0xFFFF

    def setI(v: Short):Unit  = {
        I = v
    }

    
}

object RegisterFile{
    def apply() = new RegisterFile()
}