// package lab7
// import chisel3._
// import chisel3.tester._
// import org.scalatest.FreeSpec
// import chisel3.experimental.BundleLiterals._

// class RegFileTest extends FreeSpec with ChiselScalatestTester {
//   "RegFile" in {
//     test(new RegFile){ dut =>   

//       dut.io.wen.poke(true.B)
//       dut.io.addrd.poke(1.U) // Address where we want to write
//       dut.io.datard.poke(42.U) // Data to write
//       dut.clock.step(1)

//       // Read values from registers
//       dut.io.addrs1.poke(1.U)
//       dut.io.addrs2.poke(2.U)
//       dut.clock.step(1)

//       // Check the outputs
//       dut.io.datars1.expect(42.U) // Expect the value we wrote
//       dut.io.datars2.expect(0.U)
//     }

//   }
// }
