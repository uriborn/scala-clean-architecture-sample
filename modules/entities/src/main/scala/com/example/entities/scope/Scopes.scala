package com.example.entities.scope

import com.example.entities.{EntitiesError, EntitiesValidationResult}
import com.example.shared.util.EitherSyntax._
import com.example.shared.util.OptionSyntax._

case class Scopes(value: Seq[Scope]) {
  val toStringList = value.map(_.entryName)
  val toSeparatedSpacesString = toStringList.mkString(" ")
}

object Scopes {

  def fromOptSeqString(value: Option[Seq[String]]): EntitiesValidationResult[Option[Scopes]] = {
    value match {
      case Some(v) => fromSeqString(v).map(_.some)
      case None    => None.asRight
    }
  }

  def fromSeqString(value: Seq[String]): EntitiesValidationResult[Scopes] = {
    value.map(Scope.withNameValidation).foldLeft[Either[EntitiesError, Seq[Scope]]](Right(Nil)) {
      case (Right(acc), Right(v)) => (acc :+ v).asRight
      case (invalid @ Left(_), _) => invalid
      case (Right(_), Left(a)) => a.asLeft
    }.map(Scopes(_))
  }

}
