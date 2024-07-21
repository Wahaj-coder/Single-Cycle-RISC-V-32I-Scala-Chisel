package SingleCycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class MainModuleTest extends FreeSpec with ChiselScalatestTester {
  "MainModule" in {
    test(new MainModule){ a =>   
 
    a.clock.step(500)      
    }
  }
}