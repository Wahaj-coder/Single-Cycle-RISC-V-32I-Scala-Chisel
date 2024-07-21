package SingleCycle
import chisel3._
import chisel3.util._
class DMEM extends Module {
  val io = IO(new Bundle {
    val data_in=Input(Vec(4,UInt(8.W)))
    val data_out=Output(Vec(4,UInt(8.W)))
    val wen=Input(Bool())
    val mask = Input(Vec(4,Bool()))
    val address=Input(UInt(10.W))
  })           
    val memory = Mem(1024,Vec(4,UInt(8.W)))
    when (io.wen) {
        memory.write (io.address,io.data_in,io.mask)
    }
    io.data_out := memory.read(io.address)
}

