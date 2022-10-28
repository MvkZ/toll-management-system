package core

import repository.TableRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

object DB {

  val repo = Init()

  def Init() = {
    lazy val db = Database.forConfig("database.postgre")
    lazy val profile = new JdbcProfile {}
    TableRepository(db, profile)
  }


}
