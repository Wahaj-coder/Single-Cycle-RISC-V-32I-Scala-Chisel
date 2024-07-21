package SingleCycle
import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile//
import scala.io.Source//
class IMEM(initFile:String) extends Module{//
  
  val io = IO(new Bundle {
    //val data_in=Input(UInt(32.W))
    val data_out=Output(UInt(32.W))
    //val wen=Input(Bool())
    val address=Input(UInt(32.W))
  })
    //val memory = Mem(1024, UInt(32.W))

    //when ( io . wen ) {
        //memory.write (io.address,io.data_in)
    //}
    //io.data_out := memory.read(io.address-1.U)
    val imem = Mem(1024, UInt(32.W))
    loadMemoryFromFile(imem, initFile)
     io.data_out:= imem((io.address/4.U)-1.U)
    //io.data_out:= imem(io.address/4.U)
}
