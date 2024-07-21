package SingleCycle
import chisel3._
import chisel3.util._
class CU extends Module {  
  val io = IO(new Bundle {
    val data_in=Input(UInt(32.W))
    val rs1=Output(UInt(5.W))
    val rs2=Output(UInt(5.W))
    val rd=Output(UInt(5.W))
    val aluOp=Output(UInt(4.W))
    val opcode=Output(UInt(7.W))
    val imm=Output(SInt(32.W))
    val func3=Output(UInt(3.W))
    val func7=Output(UInt(7.W))
    
    
  })
  io.rd:=io.data_in(11, 7)
  io.rs1:=io.data_in(19,15)
  io.rs2:=io.data_in(24,20)
  io.opcode:=io.data_in(6,0)
  io.func3:=io.data_in(14,12)
  io.func7:=io.data_in(31,25)
  when(io.opcode==="b0100011".U){
    io.imm := (Cat(Fill(20, io.data_in(31)), Cat(io.data_in(31,25),io.rd))).asSInt()
  }.elsewhen(io.opcode==="b1100011".U){
    io.imm := Cat(Cat(Fill(19, io.data_in(31)),(Cat(io.data_in(31),io.data_in(7)))),(Cat(io.data_in(30,25),(Cat(io.data_in(11,8),0.U))))).asSInt()
  }.elsewhen(io.opcode==="b0110111".U || io.opcode==="b0010111".U){ 
     io.imm:= Cat(io.data_in(31,12),Fill(12,0.U)).asSInt
  }.elsewhen(io.opcode==="b1101111".U){ 
     io.imm:= Cat(Fill(12,io.data_in(31)),io.data_in(19,12),io.data_in(20),io.data_in(30,21), 0.U).asSInt
  }.otherwise{
    io.imm := (Cat(Fill(20, io.data_in(31)), io.data_in(31,20))).asSInt()
  }
  when(io.opcode==="b0110011".U){
    when(io.func3===0.U){
      when(io.func7==="b0100000".U){
        io.aluOp:=1.U
      }.otherwise{
        io.aluOp:=0.U
      }
    }.elsewhen(io.func3===1.U){
      io.aluOp:=2.U
    }.elsewhen(io.func3===2.U){
      io.aluOp:=3.U
    }.elsewhen(io.func3===3.U){
      io.aluOp:=4.U
    }.elsewhen(io.func3===4.U){
      io.aluOp:=5.U
    }.elsewhen(io.func3===5.U){
      when(io.func7==="b0100000".U){
        io.aluOp:=7.U
      }.otherwise{
        io.aluOp:=6.U
      }
    }.elsewhen(io.func3===6.U){
      io.aluOp:=8.U
    }.elsewhen(io.func3===7.U){
      io.aluOp:=9.U
    }.otherwise{
      io.aluOp:=0.U     
    }
  }.elsewhen(io.opcode==="b0010011".U){
    when(io.func3===0.U){
        io.aluOp:=0.U
    }.elsewhen(io.func3===1.U){
      io.aluOp:=2.U
    }.elsewhen(io.func3===2.U){
      io.aluOp:=3.U
    }.elsewhen(io.func3===3.U){
      io.aluOp:=4.U
    }.elsewhen(io.func3===4.U){
      io.aluOp:=5.U
    }.elsewhen(io.func3===5.U){
      when(io.func7==="b0100000".U){
        io.aluOp:=7.U
      }.otherwise{
        io.aluOp:=6.U
      }
    }.elsewhen(io.func3===6.U){
      io.aluOp:=8.U
    }.elsewhen(io.func3===7.U){
      io.aluOp:=9.U
    }.otherwise{
      io.aluOp:=0.U
    }
  }.elsewhen(io.opcode==="b1100011".U){//Branch
    when(io.func3===0.U){ 
          io.aluOp:=10.U 
      }.elsewhen(io.func3===1.U){
          io.aluOp:=11.U
      }.elsewhen(io.func3===4.U){
          io.aluOp:=12.U
      }.elsewhen(io.func3===5.U){
          io.aluOp:=13.U
      }.elsewhen(io.func3===6.U){
          io.aluOp:=14.U
      }.elsewhen(io.func3===7.U){
        io.aluOp:=15.U
      }.otherwise{
          io.aluOp:=0.U
      }
  }.otherwise{
    io.aluOp:=0.U
  }  
  
}
