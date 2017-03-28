package controllers

import javax.inject._

import play.libs.Json
import play.api.mvc._
import play.db.Database
import play.twirl.api.Html

import scala.collection.mutable.ListBuffer

/**
  * Created by alex on 3/26/2017.
  */
@Singleton
class FriendRecController @Inject() (db : Database) extends Controller {

  def getHtml(name : String) = Action { implicit request =>
    var htmlCode: Html = Html("")
    if (name != "") {
      val stmt = db.getConnection().createStatement()
      val result = stmt.executeQuery("Select * from friends where username='" + name + "'")
      val builder = StringBuilder.newBuilder

      val friends = new ListBuffer[String]()
      while (result.next)
        friends += result.getString("friend")

      builder.append("<ul>")
      for (friend <- friends)
      {
        val res = stmt.executeQuery("Select * from friends where username='" + friend + "'")
        while (res.next)
        {
           val rec = res.getString("friend")
          if (rec != name)
             builder.append("<li>").append(rec).append("</li>")
        }
      }
      builder.append("</ul>")
      htmlCode = Html(builder.toString())
    }
    Ok(views.html.search(htmlCode))
  }

  def getJson(name : String) = Action { implicit request =>
    val jsonArray = Json.newArray()
    val jsonCode = Json.newObject()

    if (name != "") {
      val stmt = db.getConnection().createStatement()
      val result = stmt.executeQuery("Select * from friends where username='" + name + "'")

      val friends = new ListBuffer[String]()
      while (result.next)
        friends += result.getString("friend")

      for (friend <- friends)
      {
        val res = stmt.executeQuery("Select * from friends where username='" + friend + "'")
        while (res.next)
        {
          val rec = res.getString("friend")
          if (rec != name)
            jsonArray.add(rec)
        }
      }
      jsonCode.put(name, jsonArray)
    }
    Ok(jsonCode.toString)
  }
}