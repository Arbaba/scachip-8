package chip8

class CPU(memory: Memory){
    val registerFile: RegisterFile = RegisterFile()
    val stack: Stack = Stack()
    private var pc: Short = 0x200

    def run() = {
      for(i <-1 to 200){
        println(decode(fetch))
        //execute(decode(fetch))
      }
    }

    def fetch: Short =  {
      val instruction = memory.getWord(pc)
      pc = (pc + 2).toShort 
      println(s"hex: ${(instruction & 0xFFFF).toHexString}")
      instruction
    }

    def decode(binInstruction: Short): Instruction = Decoder.decode(binInstruction)

    def execute(instruction: Instruction): Unit = instruction match {
        case  CALL(address: Int)  => ???
        case  SkipIfEq(regIdx: Byte, value: Int)  => ???
        case  SkipIfNeq(regIdx: Byte, value: Int)  => ???
        case  SkipEQREG(reg1Idx: Byte, reg2Idx: Int)  => ???
        case  LDVx(vx: Byte, value: Byte)  => ???
        case  ADDVx(vx: Byte, value: Byte)  => ???
        case  LDVxVy(vx: Byte, vy: Byte)  => ???
        //8xy1  
        //Set Vx = Vx OR Vy.
        case  ORVxVy(vx: Byte, vy:Byte)  => ???
        //8xy2 - 
        //Set Vx = Vx AND Vy.
        case  ANDVxVy(vx: Byte, vy: Byte)  => ???
        //8xy3 - 
        //Set Vx = Vx XOR Vy.
        case  XORVxVy(vx: Byte, vy: Byte)  => ???
        //8xy4 - 
        //Set Vx = Vx + Vy, set VF = carry.
        //The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255,) VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
        case  ADDVxVy(vx: Byte, vy: Byte)  => ???
        //8xy5 - 
        //Set Vx = Vx - Vy, set VF = NOT borrow.
        //If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx, and the results stored in Vx.
        case  SUBVxVy(vx: Byte, vy: Byte)  => ???
        //8xy6 - 
        //Set Vx = Vx SHR 1.
        //If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
        case  SHRVx(vx: Byte)  => ???
        //8xy7 - 
        //Set Vx = Vy - Vx, set VF = NOT borrow.
        //If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy, and the results stored in Vx.
        case  SUBNVxVy(vx: Byte, vy: Byte)  => ???
        //8xyE - 
        //Set Vx = Vx SHL 1.
        //If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0. Then Vx is multiplied by 2.
        case  SHLVx(vx:Byte)  => ???


        //9xy0 - 
        //Skip next instruction if Vx != Vy.
        //The values of Vx and Vy are compared, and if they are not equal, the program counter is increased by 2.
        case  SNEVxVy(vx: Byte, vy: Byte)  => ???



        //Annn - 
        //Set I = nnn.
        //The value of register I is set to nnn.
        case  LDI(addr : Int)  => ???



        //Bnnn - 
        //Jump to location nnn + V0.
        //The program counter is set to nnn plus the value of V0.
        case  JPV0(addr : Int)  => ???

        /*
        Cxkk - 
        Set Vx = random byte AND kk.

        The interpreter generates a random number from 0 to 255, which is then ANDed with the value kk. The results are stored in Vx. See instruction 8xy2 for more information on AND.
        */
        case  RNDVx(vx: Byte, kk: Byte)  => ???
        /*
        Dxyn - 
        Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.

        The interpreter reads n bytes from memory, starting at the address stored in I. These bytes are then displayed as sprites on screen at coordinates (Vx, Vy). Sprites are XORed onto the existing screen. If this causes any pixels to be erased, VF is set to 1, otherwise it is set to 0. If the sprite is positioned so part of it is outside the coordinates of the display, it wraps around to the opposite side of the screen. See instruction 8xy3 for more information on XOR, and section 2.4, Display, for more information on the Chip-8 screen and sprites.
        */
        case  DRWVxVy(vx: Byte, vy: Byte, nibble : Byte)  => ???
        /*
        Ex9E -
        Skip next instruction if key with the value of Vx is pressed.

        Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
        */
        case  SKPVx(vx: Byte)  => ???

        /*
        ExA1 - 
        Skip next instruction if key with the value of Vx is not pressed.

        Checks the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2.
        */
        case  SKNPVx(vx: Byte)  => ???

        /*
        Fx07 - 
        Set Vx = delay timer value.

        The value of DT is placed into Vx.*/
        case  LDVxDT(vx: Byte)  => ???

        /*
        Fx0A - 
        Wait for a key press, store the value of the key in Vx.

        All execution stops until a key is pressed, then the value of that key is stored in Vx.
        */
        case  LDVxK(vx: Byte)  => ???

        /*
        Fx15 - 
        Set delay timer = Vx.
        DT is set equal to the value of Vx.
        */
        case  LDDTVx(vx: Byte)  => ???

        /*
        Fx18 - 
        Set sound timer = Vx.

        ST is set equal to the value of Vx.*/
        case  LDSTVx(vx: Byte)  => ???

        /*
        Fx1E - 
        Set I = I + Vx.

        The values of I and Vx are added, and the results are stored in I.*/
        case  ADDIVx(vx: Byte)  => ???

        /*
        Fx29 - 
        Set I = location of sprite for digit Vx.

        The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx. See section 2.4, Display, for more information on the Chip-8 hexadecimal font.
        */
        case  LDFVx(vx: Byte)  => ???
        /*
        Fx33 - 
        Store BCD representation of Vx in memory locations I, I+1, and I+2.

        The interpreter takes the decimal value of Vx, and places the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.

        */
        case  LDBVx(vx: Byte)  => ???

        /*
        Fx55 - 
        Store registers V0 through Vx in memory starting at location I.

        The interpreter copies the values of registers V0 through Vx into memory, starting at the address in I.
        */
        case  LDIVx(vx: Byte)  => ???
        /*

        Fx65 - 
        Read registers V0 through Vx from memory starting at location I.

        The interpreter reads values from memory starting at location I into registers V0 through Vx.
        */
        case  LDVxI(vx: Byte)  => ???
  }
}

object CPU{
  def apply(memory: Memory) = new CPU(memory)
}