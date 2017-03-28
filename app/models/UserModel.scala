package models

import play.api.libs.json.Json

/**
  * Created by alex on 3/26/2017.
  */
case class UserModel(username : String, password : String)

object UserModel
{
  implicit val userFormat = Json.format[UserModel]
}
