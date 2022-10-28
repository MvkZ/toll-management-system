package core

import org.scalatest.FunSuite

import java.time.LocalDate

class TicketHelperTester extends FunSuite {

  test("Check For Vehicle... ") {
    val test = TicketHelpers.checkForVehicle("ABCD")
    assert(test._2 == -1)
  }

  test("Check if Ticket Valid... ") {
    val test = TicketHelpers.checkIfValid(1, LocalDate.now, 1)
    assert(test == "EXPIRED")
  }
}
