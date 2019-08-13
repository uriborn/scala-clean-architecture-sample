package com.example

import enumeratum.EnumEntry

package object entities {

  case class EntitiesError(message: String)

  type EntitiesValidationResult[A] = Either[EntitiesError, A]

  private[entities] trait EnumWithValidation[E <: EnumEntry] {
    self: enumeratum.Enum[E] =>

    import com.example.shared.util.EitherSyntax._

    def withNameValidation(name: String): EntitiesValidationResult[E] = {
      self.withNameOption(name).map(_.asRight).getOrElse(EntitiesError(s"$name is not a member").asLeft)
    }

  }

}
