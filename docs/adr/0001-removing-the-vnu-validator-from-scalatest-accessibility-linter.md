# Removing the VNU validator from scalatest-accessibility-linter

* Status: accepted
* Deciders: PlatUI
* Date: 15 Jan 2024

Technical Story: [PLATUI-2717](https://jira.tools.tax.service.gov.uk/browse/PLATUI-2717)

## Context and Problem Statement

As part of migrating scalatest-accessibility-linter to a multi-project build supporting Play 2.8, Play 2.9, and Play 3.0
(with Scala 2.13 and sbt 1.9.7), we found that the VNU [validator](https://github.com/validator/validator) library has 
not had a new release since 2020, and throws a parsing runtime error when used with newer versions of Scala / sbt / Play.

This documents the steps taken to unblock moving forward with the migration when a key library has not been actively 
maintained.

## Decision Drivers

* Library required compatibility with multiple versions of Play Framework to be usable by service teams
* Library as-is only works with deprecated Play 2.8
* Numerous teams as using scalatest-accessibility-linter via sbt-accessiblity-linter, and removing the plugin entirely
  would require their developer time and effort
* Whilst the tool is used by some teams, it has not been universally adopted, and maintenance time needs to be balanced
  against other PlatUI responsibilities

## Considered Options

* Option 1: Deprecate the `sbt-accessibility-linter` entirely due to VNU validator no longer being available
* Option 2: Remove VNU validation from `scalatest-accessibility-linter` permanently
* Option 3: Disable VNU validation from `scalatest-accessibility-linter` with options left open to re-enable
* Option 4: Investigate other library solutions for VNU validation
* Option 5: Do nothing

## Decision Outcome

Chosen option: "Option 3", because:
* Doing nothing will mean that teams using the `sbt-accessiblity-linter` cannot fully migrate their service away from Play 2.8
* We believe that some teams are getting some value from the linter, and do not have enough research to know whether it 
 should be deprecated
* Removing the linter completely from services is believed to require developer input within service teams
* Whilst VNU could be entirely removed, we believe it still offers value to teams, and would like to be able to investigate
  future ways to re-enable. For example, it might be possible to add VNU validation back in using the npm version of the 
  validator, as npm is already retrieving the Axe tesing dependencies

### Positive Consequences

* Allows the library be be upgraded to support Play 2.9 and Play 3.0 with relevant Scala and sbt versions
* Does not prevent adding in VNU validation back in via another method in the future
* Allows the upgrade migration to continue in time to meet the other Platform deadlines re: Play 3.0 migration

### Negative Consequences

* `sbt-accessibility-linter` loses one of the two accessibility testing mechanisms, so is functionally downgraded

## Pros and Cons of the Options

### Option 1: Deprecate the `sbt-accessibility-linter` entirely due to VNU validator no longer being available

* Good, because it avoids the need for a multi-project build.
* Bad, because PlatUI do not have sufficient data to know what the impact on teams would be.

### Option 2: Remove VNU validation from `scalatest-accessibility-linter` permanently

* Good, because it allows the project to progress as multi-project build.
* Bad, because it requires a significant amount of code rewriting.
* Bad, because it makes it harder to add VNU back in the future.

### Option 4: Investigate other library solutions for VNU validation

* Good, because it would mean that `sbt-accessiblity-linter` does not experience a functional downgrade.
* Bad, because it has an unknown time cost for investigation and may not even be possible.

### Option 5: Do nothing

* Good, because it does not require any developer time from PlatUI.
* Bad, because teams relying on the `sbt-accessiblity-linter` will be unable to cleanly migrate to Play 3.0
