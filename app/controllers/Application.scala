package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def prettyCat = Action {
    Ok("https://dl.dropbox.com/u/6581286/vvakame/neco-box.gif")
  }
}