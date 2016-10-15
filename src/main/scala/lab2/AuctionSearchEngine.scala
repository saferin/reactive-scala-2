package lab2

import akka.actor.{Terminated, Actor, ActorRef}
import scala.collection.mutable
import scala.util.Random


object AuctionSearchEngine {

  val ACTOR_NAME = "AUCTION_SEARCH_ENGINE"

  case class GetAuctions(reference: String)
  case class AddNewAuction(product: String)
  case class SearchResult(auctions: Iterable[ActorRef])
}

class AuctionSearchEngine extends Actor{
  import AuctionSearchEngine._

  val random: Random = new Random()

  var auctions: mutable.Map[String, ActorRef] = mutable.Map()

  override def receive: Actor.Receive = {
    case AddNewAuction(productName) =>
      println("received")
      auctions += (productName.toLowerCase() -> sender)
      context.watch(sender)

    case GetAuctions(reference) =>
      val found = auctions.filterKeys(_.contains(reference.toLowerCase))
      sender ! new SearchResult(found.values)

    case Terminated(auction) =>
      auctions.retain((name, a) => a.equals(auction))
  }
}
