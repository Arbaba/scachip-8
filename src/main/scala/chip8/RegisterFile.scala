package chip8

class RegisterFile{
    //16 8-bits registers
    private val registers: Array[Byte] = Array.ofDim(16)

    //Memory addresses storage. Only 12 lowest bits are used
    private var I: Short = 0

    private var delay: Byte = 0

    private var sound: Byte  = 0


    def getVx(idx: Int): Byte = registers(idx)

    def setVx(idx: Int, v: Byte): Unit = registers.update(idx, v)

    def getI(): Short = I

    def setI(v: Byte):Unit  = I = v

    
}

object RegisterFile{
    def apply() = new RegisterFile()
}