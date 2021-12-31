# omni-reporter-maven-plugin

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=%20Checkout%20this%20%40github%20repo%20by%20%40joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB%3A%20https%3A//github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=omni-reporter-maven-plugin&color=informational)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![Status badge](https://img.shields.io/static/v1.svg?label=Status&message=Under%20Construction%20🚧&color=informational)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)

A plugin intended to keep the pace of technology and be able to use the Coveralls platform as  Java/Scala/Kotlin updates move onwwards

## Features

#### 1. Reporting file supported

| Type       | Status | Notes                                                                                                                                                                                                                                           | Available from Release |
|------------|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| Jacoco XML | 🚧     | Jacoco reports seem to report on nonexistent classes in some cases. This seems to happen with Kotlin. This breaks down the functionality of some plugins. In this version we are allowed to ignore this, since it does not affect most reports. | 0.0.0                  |

#### 2. Online API's supported

| Type      | Status |Notes| Available from Release | Environment Variables |
|-----------|--------|---|------------------------|---|
| Coveralls | 🚧     | | 0.0.0                  |COVERALLS_REPO_TOKEN or COVERALLS_TOKEN|
| Codacy    | 🚧     | | ?                      |CODACY_PROJECT_TOKEN|
| CodeCov   | 🚧     | | ?                      |CODECOV_TOKEN|

#### 3. Pipelines Supported


| Type     | Status |Notes| Available from Release |
|----------|--------|---|------------------------|
| Local    | 🚧     | | 0.0.0                  |
| Git Hub  | 🚧     | | 0.0.0                  |
| Git Lab  | 🚧     | | 0.0.0                  |
| CircleCI | 🚧     | | ?                      |

#### 4. Configuration options

```shell
mvn clean install omni:report
```

| Property         | Function                                                                                                                                                                                                                                                                                                                                        |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| failOnUnknown    | If an unknown file is found, it will ignore current file and proceed with reporting the rest. It is `false` by default                                                                                                                                                                                                                          |
| failOnNoEncoding | If an explicit encoding is not found, the reporting process will continue. It will fail only should an invalid character be found. Active this if you want the plugin to fail if configuration is not found. It is `false` by default                                                                                                           |
| coverallsToken   | Sets the coveraslls token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variables `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN` instead |
| codecovToken     | Sets the codecovToken token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_PROJECT_TOKEN` instead                     |
| codacyToken      | Sets the codacyToken token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODECOV_TOKEN` instead|

## Reading and interpreting report files

#### Jacoco Reports

- `mi` = missed instructions
- `ci` = covered instructions
- `mb` = missed branches
- `cb` = covered branches

## Release notes - Upcoming version 0.0.0

1. We can ignore unknown class error generated by Jacoco. This happens with some Kotlin code. The option is `failOnUnknown`
2. Source encoding gets automatically chosen unless we configure flag `failOnNoEncoding` to `true`
3. [Saga](https://timurstrekalov.github.io/saga/) and [Cobertura](https://www.mojohaus.org/cobertura-maven-plugin/) support is not given because of the lack of updates in these plugins for more than 5 years.
4. Plugin will search for all jacoco.xml files located in the build directory.
5. If there are two reports with the same file reported, the result will be an average.
6. Coveralls support
7. DOM processing instead of SAX. Using an event parser for XML can be quite sumbersome and if the XML document isn't correctly validated, we run the risk of having misleading or false results. In any case, when making a code report, we usually don't need to worry about performance and if we do, it is probably a sign that the codebase is too big and that our code is becoming a monolith.

## References

- [XCode Environment Variable Reference](https://developer.apple.com/documentation/xcode/environment-variable-reference)
- [Cross-CI reference](https://github.com/streamich/cross-ci)
- [Coveralls API reference](https://docs.coveralls.io/api-reference)
- [Git Hub Environment Variables](https://docs.github.com/en/actions/learn-github-actions/environment-variables)
- [Git Lab Environment Variables](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html)
- [Check Run Reporter](https://github.com/marketplace/check-run-reporter)
- [Codacy Maven Plugin](https://github.com/halkeye/codacy-maven-plugin)
- [Coveralls Maven Plugin](https://github.com/trautonen/coveralls-maven-plugin)
- [Example Java Maven for CodeCov](https://github.com/codecov/example-java-maven)
- [CodeCov Maven Plugin](https://github.com/alexengrig/codecov-maven-plugin)
