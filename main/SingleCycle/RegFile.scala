package SingleCycle
import chisel3._
import chisel3.util._

class RegFile extends Module {
  
  val io = IO(new Bundle {
    val addrs1=Input(UInt(5.W))
    val addrs2=Input(UInt(5.W))
    val addrd=Input(UInt(5.W))
    val datard=Input(SInt(32.W))
    val datars1=Output(SInt(32.W))
    val datars2=Output(SInt(32.W))
    val wen=Input(Bool())
  })

  val reg=Reg(Vec(32,SInt(32.W)))
  io.datars1:=Mux((io.addrs1.orR),reg(io.addrs1),0.S)
  io.datars2:=Mux((io.addrs2.orR),reg(io.addrs2),0.S)

  when (io.wen & io.addrd.orR) {
    reg (io.addrd) := io.datard
  }

}


