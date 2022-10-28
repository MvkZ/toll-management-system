package models

import java.time.LocalDate

case class TollPass(passId: Int, vehicleType: String, tollId: Int, rate: Int, passDate: LocalDate, passType: String)
