package chip8

trait Instruction  
// Clear the display.
case class CLS() extends Instruction
//The interpreter sets the program counter to the address at the top of the stack, then subtracts 1 from the stack pointer.
case class RET() extends Instruction
case class JP(address: Int) extends Instruction

/*
2nnn - 
Call subroutine at nnn.

The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn.*/
case class SYS(address: Int) extends Instruction
case class CALL(address: Int) extends Instruction
case class SkipIfEq(regIdx: Int, value: Int) extends Instruction
case class SkipIfNeq(regIdx: Int, value: Int) extends Instruction
case class SkipEQREG(reg1Idx: Int, reg2Idx: Int) extends Instruction
case class LDVx(vx: Int, value: Int) extends Instruction
case class ADDVx(vx: Int, value: Int) extends Instruction
case class LDVxVy(vx: Int, vy: Int) extends Instruction
//8xy1  
//Set Vx = Vx OR Vy.
case class ORVxVy(vx: Int, vy:Int) extends Instruction
//8xy2 - 
//Set Vx = Vx AND Vy.
case class ANDVxVy(vx: Int, vy: Int) extends Instruction



//8xy3 - 
//Set Vx = Vx XOR Vy.
case class XORVxVy(vx: Int, vy: Int) extends Instruction



//8xy4 - 
//Set Vx = Vx + Vy, set VF = carry.
//The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255,) VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
case class ADDVxVy(vx: Int, vy: Int) extends Instruction



//8xy5 - 
//Set Vx = Vx - Vy, set VF = NOT borrow.
//If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx, and the results stored in Vx.
case class SUBVxVy(vx: Int, vy: Int) extends Instruction



//8xy6 - 
//Set Vx = Vx SHR 1.
//If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
case class SHRVx(vx: Int) extends Instruction



//8xy7 - 
//Set Vx = Vy - Vx, set VF = NOT borrow.
//If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy, and the results stored in Vx.
case class SUBNVxVy(vx: Int, vy: Int) extends Instruction



//8xyE - 
//Set Vx = Vx SHL 1.
//If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0. Then Vx is multiplied by 2.
case class SHLVx(vx:Int) extends Instruction


//9xy0 - 
//Skip next instruction if Vx != Vy.
//The values of Vx and Vy are compared, and if they are not equal, the program counter is increased by 2.
case class SNEVxVy(vx: Int, vy: Int) extends Instruction



//Annn - 
//Set I = nnn.
//The value of register I is set to nnn.
case class LDI(addr : Int) extends Instruction



//Bnnn - 
//Jump to location nnn + V0.
//The program counter is set to nnn plus the value of V0.
case class JPV0(addr : Int) extends Instruction

/*
Cxkk - 
Set Vx = random Int AND kk.

The interpreter generates a random number from 0 to 255, which is then ANDed with the value kk. The results are stored in Vx. See instruction 8xy2 for more information on AND.
*/
case class RNDVx(vx: Int, kk: Int) extends Instruction
/*
Dxyn - 
Display n-Int sprite starting at memory location I at (Vx, Vy), set VF = collision.

The interpreter reads n Ints from memory, starting at the address stored in I. These Ints are then displayed as sprites on screen at coordinates (Vx, Vy). Sprites are XORed onto the existing screen. If this causes any pixels to be erased, VF is set to 1, otherwise it is set to 0. If the sprite is positioned so part of it is outside the coordinates of the display, it wraps around to the opposite side of the screen. See instruction 8xy3 for more information on XOR, and section 2.4, Display, for more information on the Chip-8 screen and sprites.
*/
case class DRWVxVy(vx:Int, vy:Int, nibble : Int) extends Instruction
/*
Ex9E -
Skip next instruction if key with the value of Vx is pressed.

Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
*/
case class SKPVx(vx: Int) extends Instruction

/*
ExA1 - 
Skip next instruction if key with the value of Vx is not pressed.

Checks the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2.
*/
case class SKNPVx(vx: Int) extends Instruction

/*
Fx07 - 
Set Vx = delay timer value.

The value of DT is placed into Vx.*/
case class LDVxDT(vx: Int) extends Instruction

/*
Fx0A - 
Wait for a key press, store the value of the key in Vx.

All execution stops until a key is pressed, then the value of that key is stored in Vx.
*/
case class LDVxK(vx: Int) extends Instruction

/*
Fx15 - 
Set delay timer = Vx.
DT is set equal to the value of Vx.
*/
case class LDDTVx(vx: Int) extends Instruction

/*
Fx18 - 
Set sound timer = Vx.

ST is set equal to the value of Vx.*/
case class LDSTVx(vx: Int) extends Instruction

/*
Fx1E - 
Set I = I + Vx.

The values of I and Vx are added, and the results are stored in I.*/
case class ADDIVx(vx: Int) extends Instruction

/*
Fx29 - 
Set I = location of sprite for digit Vx.

The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx. See section 2.4, Display, for more information on the Chip-8 hexadecimal font.
*/
case class LDFVx(vx: Int) extends Instruction
/*
Fx33 - 
Store BCD representation of Vx in memory locations I, I+1, and I+2.

The interpreter takes the decimal value of Vx, and places the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.

*/
case class LDBVx(vx: Int) extends Instruction

/*
Fx55 - 
Store registers V0 through Vx in memory starting at location I.

The interpreter copies the values of registers V0 through Vx into memory, starting at the address in I.
*/
case class LDIVx(vx: Int) extends Instruction
/*
Fx65 - 
Read registers V0 through Vx from memory starting at location I.
The interpreter reads values from memory starting at location I into registers V0 through Vx.
*/
case class LDVxI(vx: Int) extends Instruction