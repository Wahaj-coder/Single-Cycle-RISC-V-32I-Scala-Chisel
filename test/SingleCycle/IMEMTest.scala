// package lab7
// import chisel3._
// import chisel3.tester._
// import org.scalatest.FreeSpec
// import chisel3.experimental.BundleLiterals._

// class IMEMTest extends FreeSpec with ChiselScalatestTester {
//   "IMEM" in {
//     test(new IMEM){ a =>   

//     a.io.wen.poke(true.B)
//     a.io.address.poke(0.U)
//     a.io.data_in.poke(3211443.U)
//     a.clock.step(1)

//     // Read the value from the memory
//     a.io.wen.poke(false.B)
//     a.clock.step(1)

//     // Check the output
//     a.io.data_out.expect(3211443.U)
     
//     }
//   }
// }
