package chip8

object Decoder{
    def unknownOpcode(v: Int) =  {
        println("====")
        throw new IllegalAccessException(s"Unknown opcode ${v.toHexString}")
    }
    //Bottom line: use Int to avoid issues...
    def decode(i: Int): Instruction  = i & 0xFFFF match {
        case 0x00E0 => CLS()
        case 0x00EE => RET()
        case v => 
            val vx = getX(v)
            val vy = getY(v)
            val nnn = getNNN(v)
            val kk = getKK(v)
            val n  = getN(v)
            (v >> 12) & 0xFFFF match {
                case 0 => SYS(nnn)
                case 1 => JP(nnn)
                case 2 => CALL(nnn)
                case 3 => SkipIfEq(vx, kk)
                case 4 => SkipIfNeq(vx, kk)
                case 5 => SkipEQREG(vx, vy)
                case 6 => LDVx(vx, kk)
                case 7 => ADDVx(vx, kk)
                case  8 =>
                    v & 0x000F match {
                        case 0 => LDVxVy(vx, vy)
                        case 1 => ORVxVy(vx,vy)
                        case 2 => ANDVxVy(vx, vy)
                        case 3 => XORVxVy(vx,vy)
                        case 4 => ADDVxVy(vx,vy)
                        case 5 => SUBVxVy(vx, vy)
                        case 6 => SHRVx(vx)
                        case 7 => SUBNVxVy(vx,vy)
                        case 0xE => SHLVx(vx)
                        case _ => unknownOpcode(v)
                    }
                case  9 => SNEVxVy(vx,vy)        
                case  0xA => LDI(nnn)
                case  0xB => JPV0(nnn)
                case  0xC => RNDVx(vx, kk)
                case  0xD => DRWVxVy(vx,vy, n)
                case  0xE => v & 0x00FF match {
                    case 0x9E => SKPVx(vx)
                    case 0xA1 => SKNPVx(vx)
                    case _ => unknownOpcode(v)
                }
                case  0xF => v & 0x00FF match {
                    case 0x0A => LDVxK(vx)
                    case 0x07 => LDVxDT(vx)
                    case 0x15 => LDDTVx(vx)
                    case 0x18 => LDSTVx(vx)
                    case 0x1E => ADDIVx(vx)
                    case 0x29 => LDFVx(vx)
                    case 0x33 => LDBVx(vx)
                    case 0x55 => LDIVx(vx)
                    case 0x65 => LDVxI(vx)
                    case _ => unknownOpcode(v) 
                }
                case _ => 
                unknownOpcode(v)
        }
    }
    
    def getNNN(i: Int): Int = i & 0xFFF
    
    def getX(i:Int): Byte = (((i & 0x0F00) >> 8) & 0xFF).toByte
    
    def getY(i: Int): Byte = (((i & 0x00F0) >> 4) & 0xFF).toByte

    def getKK(i: Int): Byte= (i & 0x00FF).toByte 

    def getN(i: Int): Byte = (i & 0x000F).toByte
}