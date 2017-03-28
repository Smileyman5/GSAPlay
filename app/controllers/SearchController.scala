package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}
import play.db.Database
import play.twirl.api.Html

/**
  * Created by alex on 3/26/2017.
  */
@Singleton
class SearchController @Inject() (db : Database) extends Controller {
  def displayPage = Action { implicit request =>
    val name = request.getQueryString("name")
    var htmlCode: Html = Html("")
    if (name.isDefined) {
      val stmt = db.getConnection().createStatement()
      val result = stmt.executeQuery("Select username from users")
      val builder = StringBuilder.newBuilder
      builder.append("<ul>")
      while (result.next()) {
        if (result.getString("username").toLowerCase.contains(name.get.toLowerCase) || name.get.contains(result.getString("username").toLowerCase()))
          builder.append("<li>").append(result.getString("username")).append("</li>")
      }
      builder.append("</ul>")
      htmlCode = Html(builder.toString())
    }
    Ok(views.html.search(htmlCode))
  }
}
