package SingleCycle
import chisel3._
import chisel3.util._

class MainModule extends Module { 
  val io = IO(new Bundle {//PC
    val WB=Output(SInt(32.W))
  })   
    val IMEMm = Module(new IMEM("/home/wahaj/Downloads/Scala-Chisel-Learning-Journey-main/src/main/scala/gcd/SingleCycle/Mem.txt"))
    val DMEMm = Module(new DMEM())
    val CUu = Module(new CU())
    val RegF = Module(new RegFile())
    val ALUu = Module(new ALU())

    CUu.io.data_in:=IMEMm.io.data_out
    RegF.io.addrd:=CUu.io.rd
    RegF.io.addrs1:=CUu.io.rs1
    RegF.io.addrs2:=CUu.io.rs2
    ALUu.io.aluOp:=CUu.io.aluOp
    ALUu.io.X:=RegF.io.datars1

    when(CUu.io.opcode==="b0100011".U || CUu.io.opcode==="b1100011".U){//Reg wen pin
        RegF.io.wen:=0.B  
    }.otherwise{
        RegF.io.wen:=1.B  
    }

    val PC = RegInit(0.U(32.W))//PC 
    when(CUu.io.opcode==="b1100011".U){
        when(ALUu.io.out===1.S){
            PC:=(PC+CUu.io.imm.asUInt())
        }.otherwise{
            PC:=PC+4.U
        }     
    }.elsewhen(CUu.io.opcode==="b1101111".U || CUu.io.opcode==="b1100111".U){//jal,jalr
            PC:=(ALUu.io.out.asUInt())
    }.otherwise{
        PC:=PC+4.U
    }  
    IMEMm.io.address:=PC                
    when(CUu.io.opcode==="b1101111".U){//jal
        ALUu.io.X:=(PC.asSInt).asSInt
    }.elsewhen(CUu.io.opcode==="b0010111".U ){//AUIPC
        ALUu.io.X:=(PC.asSInt-4.S).asSInt
    }.otherwise{
        ALUu.io.X:=RegF.io.datars1
    }
     //src2 of alu   
    when(CUu.io.opcode==="b0010011".U || CUu.io.opcode==="b0000011".U || CUu.io.opcode==="b0100011".U || CUu.io.opcode==="b1101111".U || CUu.io.opcode==="b0010111".U ||CUu.io.opcode==="b1100111".U){
        ALUu.io.Y:=CUu.io.imm
    }.otherwise{
        ALUu.io.Y:=RegF.io.datars2
    }
    DMEMm.io.address:=ALUu.io.out(11,2).asUInt()    //Address
    when(CUu.io.opcode==="b0100011".U){//Store

    when(CUu.io.func3===0.U){
    DMEMm.io.data_in(0):=RegF.io.datars2(7,0)
    DMEMm.io.data_in(1):= RegF.io.datars2(7,0)
    DMEMm.io.data_in(2):= RegF.io.datars2(7,0)
    DMEMm.io.data_in(3):= RegF.io.datars2(7,0)
      when(ALUu.io.out(1,0)===0.U){
          DMEMm.io.mask(0):=1.B
          DMEMm.io.mask(1):=0.B
          DMEMm.io.mask(2):=0.B
          DMEMm.io.mask(3):=0.B
      }.elsewhen(ALUu.io.out(1,0)===1.U){
          DMEMm.io.mask(0):=0.B
          DMEMm.io.mask(1):=1.B
          DMEMm.io.mask(2):=0.B
          DMEMm.io.mask(3):=0.B
      }.elsewhen(ALUu.io.out(1,0)===2.U){ 
          DMEMm.io.mask(0):=0.B
          DMEMm.io.mask(1):=0.B
          DMEMm.io.mask(2):=1.B
          DMEMm.io.mask(3):=0.B
      }.otherwise{
          DMEMm.io.mask(0):=0.B
          DMEMm.io.mask(1):=0.B
          DMEMm.io.mask(2):=0.B//here is interrup, when storing half word, 
          DMEMm.io.mask(3):=1.B
      }
    }.elsewhen(CUu.io.func3===1.U){
      when(ALUu.io.out(1,0)===0.U){
          DMEMm.io.data_in(0):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(1):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(2):=0.U
          DMEMm.io.data_in(3):=0.U
          DMEMm.io.mask(0):=1.B
          DMEMm.io.mask(1):=1.B
          DMEMm.io.mask(2):=0.B
          DMEMm.io.mask(3):=0.B
      }.elsewhen(ALUu.io.out(1,0)===1.U){
          DMEMm.io.data_in(1):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(2):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(0):=0.U
          DMEMm.io.data_in(3):=0.U
          DMEMm.io.mask(0):=0.B
          DMEMm.io.mask(1):=1.B
          DMEMm.io.mask(2):=1.B
          DMEMm.io.mask(3):=0.B
      }.elsewhen(ALUu.io.out(1,0)===2.U){
          DMEMm.io.data_in(2):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(3):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(0):=0.U
          DMEMm.io.data_in(1):=0.U
          DMEMm.io.mask(0):=0.B
          DMEMm.io.mask(1):=0.B
          DMEMm.io.mask(2):=1.B
          DMEMm.io.mask(3):=1.B
      }.otherwise{
          DMEMm.io.data_in(3):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(0):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(2):=0.U
          DMEMm.io.data_in(1):=0.U
          DMEMm.io.mask(0):=1.B
          DMEMm.io.mask(1):=0.B
          DMEMm.io.mask(2):=0.B//here is interrup, when storing half word, 
          DMEMm.io.mask(3):=1.B
      }
  }.elsewhen(CUu.io.func3===2.U){
     when(ALUu.io.out(1,0)===0.U){
          DMEMm.io.data_in(0):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(1):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(2):=RegF.io.datars2(23,16)
          DMEMm.io.data_in(3):=RegF.io.datars2(31,24)
          DMEMm.io.mask(0):=1.B
          DMEMm.io.mask(1):=1.B
          DMEMm.io.mask(2):=1.B
          DMEMm.io.mask(3):=1.B
      }.otherwise{
        DMEMm.io.data_in(0):=RegF.io.datars2(7,0)
          DMEMm.io.data_in(1):=RegF.io.datars2(15,8)
          DMEMm.io.data_in(2):=RegF.io.datars2(23,16)
          DMEMm.io.data_in(3):=RegF.io.datars2(31,24)
          DMEMm.io.mask(0):=1.B
          DMEMm.io.mask(1):=1.B
          DMEMm.io.mask(2):=1.B
          DMEMm.io.mask(3):=1.B
      }
  }.otherwise{
      DMEMm.io.data_in(0):=0.U
      DMEMm.io.data_in(1):=0.U
      DMEMm.io.data_in(2):=0.U
      DMEMm.io.data_in(3):=0.U
      DMEMm.io.mask(0):=0.B
      DMEMm.io.mask(1):=0.B
      DMEMm.io.mask(2):=0.B
      DMEMm.io.mask(3):=0.B
  }
    }otherwise{
      DMEMm.io.data_in(0):=0.U
      DMEMm.io.data_in(1):=0.U
      DMEMm.io.data_in(2):=0.U
      DMEMm.io.data_in(3):=0.U
      DMEMm.io.mask(0):=0.B
      DMEMm.io.mask(1):=0.B
      DMEMm.io.mask(2):=0.B
      DMEMm.io.mask(3):=0.B
    }
    when(CUu.io.opcode==="b0000011".U){
    when(CUu.io.func3===0.U){//Load
      when(ALUu.io.out(1,0)===0.U){
        RegF.io.datard:=Cat(Fill(24,DMEMm.io.data_out(0)(7)),(DMEMm.io.data_out(0))).asSInt
      }.elsewhen(ALUu.io.out(1,0)===1.U){
        RegF.io.datard:=Cat(Fill(24,DMEMm.io.data_out(1)(7)),(DMEMm.io.data_out(1))).asSInt
      }.elsewhen(ALUu.io.out(1,0)===2.U){
        RegF.io.datard:=Cat(Fill(24,DMEMm.io.data_out(2)(7)),(DMEMm.io.data_out(2))).asSInt
      }.otherwise{
        RegF.io.datard:=Cat(Fill(24,DMEMm.io.data_out(3)(7)),(DMEMm.io.data_out(3))).asSInt
      }
    }.elsewhen(CUu.io.func3===1.U){
      when(ALUu.io.out(1,0)===0.U){
          RegF.io.datard:=Cat(Fill(16,DMEMm.io.data_out(1)(7)),(Cat(DMEMm.io.data_out(1),DMEMm.io.data_out(0)))).asSInt
      }.elsewhen(ALUu.io.out(1,0)===1.U){
          RegF.io.datard:=Cat(Fill(16,DMEMm.io.data_out(2)(7)),(Cat(DMEMm.io.data_out(2),DMEMm.io.data_out(1)))).asSInt
      }.elsewhen(ALUu.io.out(1,0)===2.U){
          RegF.io.datard:=Cat(Fill(16,DMEMm.io.data_out(3)(7)),(Cat(DMEMm.io.data_out(3),DMEMm.io.data_out(2)))).asSInt
      }.otherwise{
          RegF.io.datard:=Cat(Fill(16,DMEMm.io.data_out(0)(7)),(Cat(DMEMm.io.data_out(0),DMEMm.io.data_out(3)))).asSInt
      }
  }.elsewhen(CUu.io.func3===2.U){
     when(ALUu.io.out(1,0)===0.U){
        RegF.io.datard:=Cat((Cat(DMEMm.io.data_out(3),DMEMm.io.data_out(2))),(Cat(DMEMm.io.data_out(1),DMEMm.io.data_out(0)))).asSInt
      }.otherwise{
        RegF.io.datard:=Cat((Cat(DMEMm.io.data_out(3),DMEMm.io.data_out(2))),(Cat(DMEMm.io.data_out(1),DMEMm.io.data_out(0)))).asSInt
      }
  }.elsewhen(CUu.io.func3===4.U){
     when(ALUu.io.out(1,0)===0.U){
        RegF.io.datard:=Cat(Fill(24,0.U),(DMEMm.io.data_out(0))).asSInt
    }.elsewhen(ALUu.io.out(1,0)===1.U){
        RegF.io.datard:=Cat(Fill(24,0.U),(DMEMm.io.data_out(1))).asSInt
    }.elsewhen(ALUu.io.out(1,0)===2.U){
        RegF.io.datard:=Cat(Fill(24,0.U),(DMEMm.io.data_out(2))).asSInt
    }.otherwise{
        RegF.io.datard:=Cat(Fill(24,0.U),(DMEMm.io.data_out(3))).asSInt
    }
  }.elsewhen(CUu.io.func3===5.U){
     when(ALUu.io.out(1,0)===0.U){
        RegF.io.datard:=Cat(Fill(16,0.U),(Cat(DMEMm.io.data_out(1),DMEMm.io.data_out(0)))).asSInt
    }.elsewhen(ALUu.io.out(1,0)===1.U){
        RegF.io.datard:=Cat(Fill(16,0.U),(Cat(DMEMm.io.data_out(2),DMEMm.io.data_out(1)))).asSInt
    }.elsewhen(ALUu.io.out(1,0)===2.U){
        RegF.io.datard:=Cat(Fill(16,0.U),(Cat(DMEMm.io.data_out(3),DMEMm.io.data_out(2)))).asSInt
    }.otherwise{
        RegF.io.datard:=Cat(Fill(16,0.U),(Cat(DMEMm.io.data_out(0),DMEMm.io.data_out(3)))).asSInt
    }
  }.otherwise{
      RegF.io.datard:=0.S
  }
    }.elsewhen(CUu.io.opcode==="b0110111".U){ //lui
        RegF.io.datard:=CUu.io.imm
    }.elsewhen(CUu.io.opcode==="b1100111".U || CUu.io.opcode==="b1101111".U){ //jal,jalr
        RegF.io.datard:=(PC.asSInt)
    }.otherwise{
        RegF.io.datard:=ALUu.io.out
    }
    when(CUu.io.opcode==="b0100011".U){//DEMEM wen
        DMEMm.io.wen:=1.B
    }.otherwise{
        DMEMm.io.wen:=0.B
    }
    // io.WB:=ALUu.io.out
    io.WB := RegF.io.datard
    
}