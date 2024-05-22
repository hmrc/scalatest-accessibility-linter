/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.scalatestaccessibilitylinter.helpers

import magnolia1.{AutoDerivation, CaseClass, Monadic, SealedTrait}
import org.scalacheck.Gen.Parameters
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen}

import scala.language.experimental.macros

// this is lifted from https://github.com/softwaremill/magnolia
// it generates Arbitrary case class instances
trait ArbDerivation extends AutoDerivation[Arbitrary] {
  def parameters: Parameters

  type Typeclass[T] = Arbitrary[T]

  override def join[T](ctx: CaseClass[Arbitrary, T]): Arbitrary[T] =
    Arbitrary {
      Gen.lzy(ctx.constructMonadic(param => param.typeclass.arbitrary))
    }

  override def split[T](ctx: SealedTrait[Arbitrary, T]): Arbitrary[T] =
    Arbitrary {
      Gen.oneOf(ctx.subtypes.map(_.typeclass.arbitrary)).flatMap(identity)
    }

  implicit private val monadicGen: Monadic[Gen] =
    new Monadic[Gen] {
      override def point[A](value: A): Gen[A] =
        Gen.const(value)

      override def flatMap[A, B](from: Gen[A])(fn: A => Gen[B]): Gen[B] =
        from.flatMap(fn)

      override def map[A, B](from: Gen[A])(fn: A => B): Gen[B] =
        from.map(fn)
    }
}
