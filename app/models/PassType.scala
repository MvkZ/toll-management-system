package models

case class PassType(passType: String)

object PassType {
  val SINGLE = "SINGLE"
  val RETURN = "RETURN"
  val WEEKLY = "WEEKLY"
}
