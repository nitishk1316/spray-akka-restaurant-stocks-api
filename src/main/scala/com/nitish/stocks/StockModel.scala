package com.nitish.stocks

object StockModel {
  
  import spray.json._

  case class Stock(id: String, name: String, price: String, quantity: String)

  case class StockUpdate(name: String, price: String, quantity: String)

  /* json (un)marshalling */
  
  object Stock extends DefaultJsonProtocol {
    implicit val format = jsonFormat4(Stock.apply)
  }

  object StockUpdate extends DefaultJsonProtocol {
    implicit val format = jsonFormat3(StockUpdate.apply)
  }
}
