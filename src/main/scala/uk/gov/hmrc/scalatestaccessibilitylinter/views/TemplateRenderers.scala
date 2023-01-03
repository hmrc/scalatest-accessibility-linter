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

package uk.gov.hmrc.scalatestaccessibilitylinter.views

import org.scalacheck.Gen.Parameters
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen}
import play.twirl.api._

trait TemplateRenderers {
  this: AutomaticAccessibilitySpec =>

  // scalacheck configuration
  override implicit def parameters: Gen.Parameters = Parameters.default

  def render[Result](template: Template0[Result]): Html =
    template.render().asInstanceOf[Html]

  def render[A, Result](template: Template1[A, Result])(implicit a: Arbitrary[A]): Html = {
    val maybeHtml = for {
      av <- a.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, Result](template: Template2[A, B, Result])(implicit a: Arbitrary[A], b: Arbitrary[B]): Html = {
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
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F]
  ): Html = {
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
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G]
  ): Html = {
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
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H]
  ): Html = {
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
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I]
  ): Html = {
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

  def render[A, B, C, D, E, F, G, H, I, J, Result](
    template: Template10[A, B, C, D, E, F, G, H, I, J, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, Result](
    template: Template11[A, B, C, D, E, F, G, H, I, J, K, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, Result](
    template: Template12[A, B, C, D, E, F, G, H, I, J, K, L, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, Result](
    template: Template13[A, B, C, D, E, F, G, H, I, J, K, L, M, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Result](
    template: Template14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Result](
    template: Template15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Result](
    template: Template16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Result](
    template: Template17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Result](
    template: Template18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q],
    r: Arbitrary[R]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
      rv <- r.arbitrary.apply(parameters, Seed.random)
    } yield template.render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv, rv).asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Result](
    template: Template19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q],
    r: Arbitrary[R],
    s: Arbitrary[S]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
      rv <- r.arbitrary.apply(parameters, Seed.random)
      sv <- s.arbitrary.apply(parameters, Seed.random)
    } yield template
      .render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv, rv, sv)
      .asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Result](
    template: Template20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q],
    r: Arbitrary[R],
    s: Arbitrary[S],
    t: Arbitrary[T]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
      rv <- r.arbitrary.apply(parameters, Seed.random)
      sv <- s.arbitrary.apply(parameters, Seed.random)
      tv <- t.arbitrary.apply(parameters, Seed.random)
    } yield template
      .render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv, rv, sv, tv)
      .asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Result](
    template: Template21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q],
    r: Arbitrary[R],
    s: Arbitrary[S],
    t: Arbitrary[T],
    u: Arbitrary[U]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
      rv <- r.arbitrary.apply(parameters, Seed.random)
      sv <- s.arbitrary.apply(parameters, Seed.random)
      tv <- t.arbitrary.apply(parameters, Seed.random)
      uv <- u.arbitrary.apply(parameters, Seed.random)
    } yield template
      .render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv, rv, sv, tv, uv)
      .asInstanceOf[Html]
    maybeHtml.get
  }

  def render[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Result](
    template: Template22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Result]
  )(implicit
    a: Arbitrary[A],
    b: Arbitrary[B],
    c: Arbitrary[C],
    d: Arbitrary[D],
    e: Arbitrary[E],
    f: Arbitrary[F],
    g: Arbitrary[G],
    h: Arbitrary[H],
    i: Arbitrary[I],
    j: Arbitrary[J],
    k: Arbitrary[K],
    l: Arbitrary[L],
    m: Arbitrary[M],
    n: Arbitrary[N],
    o: Arbitrary[O],
    p: Arbitrary[P],
    q: Arbitrary[Q],
    r: Arbitrary[R],
    s: Arbitrary[S],
    t: Arbitrary[T],
    u: Arbitrary[U],
    v: Arbitrary[V]
  ): Html = {
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
      jv <- j.arbitrary.apply(parameters, Seed.random)
      kv <- k.arbitrary.apply(parameters, Seed.random)
      lv <- l.arbitrary.apply(parameters, Seed.random)
      mv <- m.arbitrary.apply(parameters, Seed.random)
      nv <- n.arbitrary.apply(parameters, Seed.random)
      ov <- o.arbitrary.apply(parameters, Seed.random)
      pv <- p.arbitrary.apply(parameters, Seed.random)
      qv <- q.arbitrary.apply(parameters, Seed.random)
      rv <- r.arbitrary.apply(parameters, Seed.random)
      sv <- s.arbitrary.apply(parameters, Seed.random)
      tv <- t.arbitrary.apply(parameters, Seed.random)
      uv <- u.arbitrary.apply(parameters, Seed.random)
      vv <- v.arbitrary.apply(parameters, Seed.random)
    } yield template
      .render(av, bv, cv, dv, ev, fv, gv, hv, iv, jv, kv, lv, mv, nv, ov, pv, qv, rv, sv, tv, uv, vv)
      .asInstanceOf[Html]
    maybeHtml.get
  }
}
