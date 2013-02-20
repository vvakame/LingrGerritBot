package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._
import org.apache.commons.httpclient._
import org.apache.commons.httpclient.methods._
import org.apache.commons.httpclient.auth._

object Application extends Controller {

  val host = Play.configuration.getString("gerrit.host").getOrElse("example.com")
  val port = Play.configuration.getInt("gerrit.port").getOrElse(8989)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def prettyCat = Action {
    Ok("https://dl.dropbox.com/u/6581286/vvakame/neco-box.gif")
  }

  def gerrit = Action {
    val json = getChanges()
    var jsArray = json.asInstanceOf[JsArray]

    val result: Seq[(String, String, String, Int, Boolean)] = jsArray.value.map(value => {
      val project = (value \ "project").asOpt[String].getOrElse("unknown")
      val subject = (value \ "subject").asOpt[String].getOrElse("unknown")
      val owner = (value \ "owner" \ "name").asOpt[String].getOrElse("unknown")
      val number = (value \ "_number").asOpt[Int].getOrElse(0)
      val reviewed = (value \ "reviewed").asOpt[Boolean].getOrElse(false)
      (project, subject, owner, number, reviewed)
    })
    val builder = new StringBuilder()
    result.map(data => {
      val (project, subject, owner, number, reviewed) = data
      builder.append(if (reviewed) "✔" else "□").append(owner).append(" ").append(subject).append(" ")
      builder.append("https://" + host + ":" + port + "/#/c" + number)
      builder.append("\n")
    })
    Ok(builder.toString())
  }

  def getChanges(): JsValue = {
    val user = Play.configuration.getString("gerrit.user").getOrElse("gerrit")
    val password = Play.configuration.getString("gerrit.password").getOrElse("xxxxxx")

    var httpState = new HttpState()
    val scope = new AuthScope(host, port)
    val credentials = new UsernamePasswordCredentials(user, password)
    httpState.setCredentials(scope, credentials)

    val config = new HostConfiguration()
    val method = new GetMethod("https://" + host + ":" + port + "/a/changes/?format=JSON")
    val client = new HttpClient()
    client.executeMethod(config, method, httpState)

    val response = new String(method.getResponseBody())
    // cut start of )]}'
    val json = Json.parse(response.substring(4))
    return json
  }
}