// package lab7
// import chisel3._
// import chisel3.tester._
// import org.scalatest.FreeSpec
// import chisel3.experimental.BundleLiterals._

// class CUTest extends FreeSpec with ChiselScalatestTester {
//   "CU" in {
//     test(new CU){ a =>   
//     a.io.data_in.poke(0x00800093.U)
//     a.clock.step(1)
//     a.io.rd.expect(1.U)
//     a.io.rs1.expect(0.U)
//     a.io.imm.expect(8.U)
//     a.io.opcode.expect("b0010011".U)
//     a.io.aluOp.expect(0.U)
//     a.io.RegWen.expect(1.B)


     
//     }
//   }
// }