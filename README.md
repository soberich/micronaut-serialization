# Micronaut Serialization

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.serde/micronaut-serde-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.micronaut.serde%22%20AND%20a:%22micronaut-serde-api%22)
[![Build Status](https://github.com/micronaut-projects/micronaut-serialization/workflows/Java%20CI/badge.svg)](https://github.com/micronaut-projects/micronaut-serialization/actions)
[![Revved up by Gradle Enterprise](https://img.shields.io/badge/Revved%20up%20by-Gradle%20Enterprise-06A0CE?logo=Gradle&labelColor=02303A)](https://ge.micronaut.io/scans)

Micronaut serialization a module for Micronaut that allows build-time computation of serializers and deserializers for JSON and other formats.

## Documentation

See the [Documentation](https://micronaut-projects.github.io/micronaut-serialization/1.0.x/guide/) for more information. 

See the [Snapshot Documentation](https://micronaut-projects.github.io/micronaut-serialization/snapshot/guide/) for the current development docs.

## Snapshots and Releases

Snapshots are automatically published to [Sonatype Snapshots](https://s01.oss.sonatype.org/content/repositories/snapshots/io/micronaut/) using [Github Actions](https://github.com/micronaut-projects/micronaut-serialization/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-serialization/actions).

Releases are completely automated. To perform a release use the following steps:

* [Publish the draft release](https://github.com/micronaut-projects/micronaut-serialization/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/micronaut-projects/micronaut-serialization/actions?query=workflow%3ARelease) to check it passed successfully.
* If everything went fine, [publish to Maven Central](https://github.com/micronaut-projects/micronaut-serialization/actions?query=workflow%3A"Maven+Central+Sync").
* Celebrate!
