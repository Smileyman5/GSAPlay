package controllers

import javax.inject._

import play.api.mvc.{Action, Controller}
import play.db.Database

/**
  * Created by alex on 3/26/2017.
  */
@Singleton
class RegisterController @Inject() (db : Database) extends Controller {

  def displayPage = Action {
    Ok(views.html.register(""))
  }

  def register = Action {
    Ok(views.html.register(""))
  }
}