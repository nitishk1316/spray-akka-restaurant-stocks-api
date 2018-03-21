package com.nitish.stocks

import akka.actor._
import akka.util.Timeout
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.collection.mutable.ListBuffer

class RestInterface extends HttpServiceActor
  with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging {
  actor: Actor =>

  import com.nitish.stocks.StockModel._

  implicit val timeout = Timeout(10 seconds)

  var stocks = ListBuffer[Stock]()

  def routes: Route =

    pathPrefix("stocks") {
      pathEnd {
        post {
          entity(as[Stock]) { stock => requestContext =>
            createStock(stock) match {
              case true => requestContext.complete(StatusCodes.Created)
              case _ => requestContext.complete(StatusCodes.Conflict)
            }
          }
        } ~
        get { requestContext =>
          requestContext.complete(StatusCodes.OK, getStockList())
        }
      } ~
      path(Segment) { id =>
        put {
          entity(as[StockUpdate]) { stock => requestContext =>
            updateStock(id, stock) match {
              case true => requestContext.complete(StatusCodes.OK)
              case _ => requestContext.complete(StatusCodes.Conflict)
            }
          }
        } ~
        get { requestContext =>
          getStock(id).map(requestContext.complete(StatusCodes.OK, _))
            .getOrElse(requestContext.complete(StatusCodes.NotFound))
        } ~
        delete { requestContext =>
          deleteStock(id)
          requestContext.complete(StatusCodes.OK)
        }
      }
    }

  private def createStock(stock: Stock): Boolean = {
    val doesNotExist = !stocks.exists(_.id == stock.id)
    if (doesNotExist) stocks = stocks :+ stock
    doesNotExist
  }

  private def getStockList(): List[Stock] =  {
    stocks.toList
  }

  private def getStock(id: String): Option[Stock] = {
    stocks.find(_.id == id)
  }

  private def updateStock(id: String, stock: StockUpdate): Boolean = {
    var doesNotExist = false
    val index = stocks.indexWhere(_.id == id)

    val isFound = stocks.lift(index)
    if (isFound != None) {
      val data = Stock(id, stock.name, stock.price, stock.quantity)
      stocks.update(index, data)
      doesNotExist = true
    }
    doesNotExist
  }

  private def deleteStock(id: String): Unit = {
    stocks = stocks.filterNot(_.id == id)
  }
}
