/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.scalatestaccessibilitylinter.views

import org.scalacheck.Gen.Parameters
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen}
import play.twirl.api._

trait TemplateRenderers {
  this: AutomaticAccessibilitySpec =>

  // scalacheck configuration
  override implicit def parameters: Gen.Parameters = Parameters.default

  // TODO generate boilerplate for Template0..22[_, ...]

  def render[Result](template: Template0[Result]): Html = {
    template.render().asInstanceOf[Html]
  }

  def render[A, Result](template: Template1[A, Result])
                       (implicit a: Arbitrary[A]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, Result](template: Template2[A, B, Result])
                          (implicit a: Arbitrary[A], b: Arbitrary[B]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, Result](
    template: Template3[A, B, C, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, Result](
    template: Template4[A, B, C, D, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, Result](
    template: Template5[A, B, C, D, E, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D], e: Arbitrary[E]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
      ev <- e.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, Result](
    template: Template6[A, B, C, D, E, F, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D], e: Arbitrary[E], f: Arbitrary[F]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
      ev <- e.arbitrary.apply(parameters, Seed.random)
      fv <- f.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, Result](
    template: Template7[A, B, C, D, E, F, G, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D], e: Arbitrary[E], f: Arbitrary[F], g: Arbitrary[G]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
      ev <- e.arbitrary.apply(parameters, Seed.random)
      fv <- f.arbitrary.apply(parameters, Seed.random)
      gv <- g.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, Result](
    template: Template8[A, B, C, D, E, F, G, H, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D], e: Arbitrary[E], f: Arbitrary[F], g: Arbitrary[G],
    h: Arbitrary[H]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
      ev <- e.arbitrary.apply(parameters, Seed.random)
      fv <- f.arbitrary.apply(parameters, Seed.random)
      gv <- g.arbitrary.apply(parameters, Seed.random)
      hv <- h.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, Result](
    template: Template9[A, B, C, D, E, F, G, H, I, Result]
  )(implicit a: Arbitrary[A], b: Arbitrary[B], c: Arbitrary[C], d: Arbitrary[D], e: Arbitrary[E], f: Arbitrary[F], g: Arbitrary[G],
    h: Arbitrary[H], i: Arbitrary[I]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
      bv <- b.arbitrary.apply(parameters, Seed.random)
      cv <- c.arbitrary.apply(parameters, Seed.random)
      dv <- d.arbitrary.apply(parameters, Seed.random)
      ev <- e.arbitrary.apply(parameters, Seed.random)
      fv <- f.arbitrary.apply(parameters, Seed.random)
      gv <- g.arbitrary.apply(parameters, Seed.random)
      hv <- h.arbitrary.apply(parameters, Seed.random)
      iv <- i.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv).asInstanceOf[Html]
    maybeHtml.get
  }
}
