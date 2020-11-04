package chip8

import scala.util.Random

class CPU(memory: Memory, display: Display){
    val registerFile: RegisterFile = RegisterFile()
    val stack: Stack = Stack()
    private var pc: Int = 0x200
    private val pressed: Array[Boolean] = Array.ofDim(16)
    private var delay: Int = 0

    private var sound: Int  = 0
    val V = registerFile
    def run() = {
     for(i <- 0 to 200){
        //println(decode(fetch))
        execute(decode(fetch))
      }
    }

    def fetch: Int =  {
      val instruction = memory.getWord(pc)
      pc = (pc + 2) 
      //println(s"hex: ${(instruction & 0xFFFF).toHexString}")
      instruction
    }
    def incrementPc() = pc = (pc + 2).toShort
    def decode(binInstruction: Int): Instruction = {
      val d = Decoder.decode(binInstruction)
      //println(d)
      d
    }

    def execute(instruction: Instruction): Unit = {
      def carry(b: Boolean) = if(b) registerFile.setVx(15.toByte, 1) else registerFile.setVx(15.toByte,0)
      instruction match {
        case CLS() =>
          display.clear()
        //The interpreter sets the program counter to the address at the top of the stack, then subtracts 1 from the stack pointer.
        case RET() =>
          pc = stack.popStack()

        case JP(address: Int) =>
          pc = address.toShort
        case  SYS(address: Int) =>
          pc = address.toShort
        case  CALL(address: Int)  => 
          //incrementPc()
          stack.pushStack(pc)
          pc = address.toShort
        case  SkipIfEq(regIdx: Int, value: Int)  => 
          if(registerFile.getVx(regIdx) == value){
            incrementPc()
          }

        case  SkipIfNeq(regIdx: Int, value: Int)  =>
          if(registerFile.getVx(regIdx) != value){
            incrementPc()
          }
        case  SkipEQREG(reg1Idx: Int, reg2Idx: Int)  => 
          if(registerFile.getVx(reg1Idx) == registerFile.getVx(reg2Idx)){
            incrementPc()
          }
        case  LDVx(vx: Int, value: Int)  => 
          registerFile.setVx(vx, value)
        case  ADDVx(vx: Int, value: Int)  => 
          registerFile.setVx(vx, (registerFile.getVx(vx) + value).toInt)
        case  LDVxVy(vx: Int, vy: Int)  => 
          registerFile.setVx(vx, registerFile.getVx(vy))
        //8xy1  
        //Set Vx = Vx OR Vy.
        case  ORVxVy(vx: Int, vy:Int)  => 
          registerFile.setVx(vx, registerFile.getVx(vx) | registerFile.getVx(vy))
        //8xy2 - 
        //Set Vx = Vx AND Vy.
        case  ANDVxVy(vx: Int, vy: Int)  => 
          registerFile.setVx(vx, registerFile.getVx(vx) & registerFile.getVx(vy))

        //8xy3 - 
        //Set Vx = Vx XOR Vy.
        case  XORVxVy(vx: Int, vy: Int)  => 
          registerFile.setVx(vx, registerFile.getVx(vx) ^ registerFile.getVx(vy))

        //8xy4 - 
        //Set Vx = Vx + Vy, set VF = carry.
        //The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255,) VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
        case  ADDVxVy(vx: Int, vy: Int)  => 
          val n = (registerFile.getVx(vx) & 0xFF) + (registerFile.getVx(vy) & 0xFF)  
          registerFile.setVx(vx, n)
          carry(n > 255)

        //8xy5 - 
        //Set Vx = Vx - Vy, set VF = NOT borrow.
        //If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx, and the results stored in Vx.
        case  SUBVxVy(vx: Int, vy: Int)  =>
          val n = (registerFile.getVx(vx) & 0xFF) - (registerFile.getVx(vy) & 0xFF)
          registerFile.setVx(vx, n)
          carry(n > 0)
        //8xy6 - 
        //Set Vx = Vx SHR 1.
        //If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
        case  SHRVx(vx: Int)  => 
          carry((registerFile.getVx(vx) & 1) == 1)
          val n = (registerFile.getVx(vx) & 0xFF) >> 1
          registerFile.setVx(vx, n)

        //8xy7 - 
        //Set Vx = Vy - Vx, set VF = NOT borrow.
        //If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy, and the results stored in Vx.
        case  SUBNVxVy(vx: Int, vy: Int)  => 
          carry(registerFile.getVx(vy) > registerFile.getVx(vx))
          registerFile.setVx(vx, registerFile.getVx(vx) - registerFile.getVx(vy))
        //8xyE - 
        //Set Vx = Vx SHL 1.
        //If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0. Then Vx is multiplied by 2.
        case  SHLVx(vx:Int)  => 
          carry(registerFile.getVx(vx) >> 7 == 1)
          registerFile.setVx(vx, registerFile.getVx(vx) << 1)



        //9xy0 - 
        //Skip next instruction if Vx != Vy.
        //The values of Vx and Vy are compared, and if they are not equal, the program counter is increased by 2.
        case  SNEVxVy(vx: Int, vy: Int)  => 
          if(registerFile.getVx(vx) != registerFile.getVx(vy)) incrementPc()

        //Annn - 
        //Set I = nnn.
        //The value of register I is set to nnn.
        case  LDI(addr : Int)  => 
            registerFile.setI(addr.toShort)

        //Bnnn - 
        //Jump to location nnn + V0.
        //The program counter is set to nnn plus the value of V0.
        case  JPV0(addr : Int)  => 
          pc = (addr + registerFile.getVx(0))
        /*
        Cxkk - 
        Set Vx = random Int AND kk.

        The interpreter generates a random number from 0 to 255, which is then ANDed with the value kk. The results are stored in Vx. See instruction 8xy2 for more information on AND.
        */
        case  RNDVx(vx: Int, kk: Int)  => 
          registerFile.setVx(vx, Random.nextInt(255) & kk)
        /*
        Dxyn - 
        Display n-Int sprite starting at memory location I at (Vx, Vy), set VF = collision.

        The interpreter reads n Ints from memory, starting at the address stored in I. These Ints are then displayed as sprites on screen at coordinates (Vx, Vy). Sprites are XORed onto the existing screen. If this causes any pixels to be erased, VF is set to 1, otherwise it is set to 0. If the sprite is positioned so part of it is outside the coordinates of the display, it wraps around to the opposite side of the screen. See instruction 8xy3 for more information on XOR, and section 2.4, Display, for more information on the Chip-8 screen and sprites.
        */
        case  DRWVxVy(vx: Int, vy: Int, nibble : Int)  =>
          val location = registerFile.getI()
          println(location)
          val collision = display.draw(registerFile.getVx(vx), registerFile.getVx(vy), for(i <- location until location + nibble) yield memory.get(i))
          carry(collision)
          /*
        Ex9E -
        Skip next instruction if key with the value of Vx is pressed.

        Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
        */
        case  SKPVx(vx: Int)  =>
          if(pressed(vx)) incrementPc()

        /*
        ExA1 - 
        Skip next instruction if key with the value of Vx is not pressed.

        Checks the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2.
        */
        case  SKNPVx(vx: Int)  =>
          if(!pressed(vx)) incrementPc()

        /*
        Fx07 - 
        Set Vx = delay timer value.

        The value of DT is placed into Vx.*/
        case  LDVxDT(vx: Int)  => 
          registerFile.setVx(vx, delay)

        /*
        Fx0A - 
        Wait for a key press, store the value of the key in Vx.

        All execution stops until a key is pressed, then the value of that key is stored in Vx.
        */
        case  LDVxK(vx: Int)  => 
          registerFile.setVx(vx, display.waitKey())
          

        /*
        Fx15 - 
        Set delay timer = Vx.
        DT is set equal to the value of Vx.
        */
        case  LDDTVx(vx: Int)  => 
          delay = registerFile.getVx(vx)
        /*
        Fx18 - 
        Set sound timer = Vx.

        ST is set equal to the value of Vx.*/
        case  LDSTVx(vx: Int)  => 
          sound = registerFile.getVx(vx)


        /*
        Fx1E - 
        Set I = I + Vx.

        The values of I and Vx are added, and the results are stored in I.*/
        case  ADDIVx(vx: Int)  => 
          registerFile.setI(registerFile.getVx(vx) + registerFile.getI())

        /*
        Fx29 - 
        Set I = location of sprite for digit Vx.

        The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx. See section 2.4, Display, for more information on the Chip-8 hexadecimal font.
        */
        case  LDFVx(vx: Int)  => 
          registerFile.setI(vx * 5)
        /*
        Fx33 - 
        Store BCD representation of Vx in memory locations I, I+1, and I+2.

        The interpreter takes the decimal value of Vx, and places the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.

        */
        case  LDBVx(vx: Int)  => 
          memory.set(registerFile.getI(), (registerFile.getVx(vx) / 100).toByte)
          memory.set(registerFile.getI() + 1, ((registerFile.getVx(vx) / 10) % 10).toByte)
          memory.set(registerFile.getI() + 2, (registerFile.getVx(vx) % 10).toByte)


        /*
        Fx55 - 
        Store registers V0 through Vx in memory starting at location I.

        The interpreter copies the values of registers V0 through Vx into memory, starting at the address in I.
        */
        case  LDIVx(vx: Int)  => 
          for(i <-0  to  vx){
            memory.set(registerFile.getI() + i, registerFile.getVx(i).toByte)
          }
        /*

        Fx65 - 
        Read registers V0 through Vx from memory starting at location I.

        The interpreter reads values from memory starting at location I into registers V0 through Vx.
        */
        case  LDVxI(vx: Int)  =>
          for(i <-0  to  vx){
            registerFile.setVx(i, memory.get(registerFile.getI() + i))
          }
      }
  }
}

object CPU{
  def apply(memory: Memory, display: Display) = new CPU(memory, display)
}