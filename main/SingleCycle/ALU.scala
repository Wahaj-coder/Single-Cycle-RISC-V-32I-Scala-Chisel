package SingleCycle
import chisel3._
import chisel3.util._

class IO_I extends Bundle {
    val aluOp = Input ( UInt ( 4. W ) )
    val X = Input ( SInt ( 32. W ) )
    val Y = Input ( SInt ( 32. W ) )
    val out = Output ( SInt ( 32. W ) )}


class ALU extends Module {
    val io = IO ( new IO_I)
    io.out := 0. S
    val index = 5

    when(io.aluOp===0.U){//ADD
            io.out:=io.X + io.Y
    }.elsewhen(io.aluOp===1.U){//SUB
        io.out:=io.X - io.Y
    }.elsewhen(io.aluOp===2.U){//SLL
        io.out:=io.X << io.Y( index -1 , 0).asUInt()
    }.elsewhen(io.aluOp===3.U){//SLT
        io.out:=(io.X < io.Y).asSInt()
    }.elsewhen(io.aluOp===4.U){//SLTU
        io.out:=(io.X.asUInt() < io.Y.asUInt()).asSInt()
    }.elsewhen(io.aluOp===5.U){//XOR
        io.out:=io.X ^ io.Y
    }.elsewhen(io.aluOp===6.U){//SRL    
        io.out:=io.X >>  io.Y(index-1,0).asUInt()
    }.elsewhen(io.aluOp===7.U){//SRA
        io.out:= (io.X >> io.Y(index-1,0).asUInt())
    }.elsewhen(io.aluOp===8.U){//OR
        io.out:=io.X | io.Y
    }.elsewhen(io.aluOp===9.U){//AND
        io.out:=io.X & io.Y
    }.elsewhen(io.aluOp===10.U){//BEQ
    when(io.X === io.Y){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }.elsewhen(io.aluOp===11.U){//BNE
    when(io.X =/= io.Y){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }.elsewhen(io.aluOp===12.U){//BLT
    when(io.X < io.Y){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }.elsewhen(io.aluOp===13.U){//BGE
    when(io.X >=io.Y){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }.elsewhen(io.aluOp===14.U){//BLTU
    when((io.X.asUInt() < io.Y.asUInt())){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }.elsewhen(io.aluOp===15.U){//BGEU
     when((io.X.asUInt() >= io.Y.asUInt())){
        io.out:=1.S
    }.otherwise{
        io.out:=0.S}
    }
}


