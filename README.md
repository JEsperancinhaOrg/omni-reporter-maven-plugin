# omni-coveragereporter-maven-plugin

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=%20Checkout%20this%20%40github%20repo%20by%20%40joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB%3A%20https%3A//github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=omni-coveragereporter-maven-plugin&color=informational)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![GitHub release](https://img.shields.io/github/release-pre/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![Maven Central](https://img.shields.io/maven-central/v/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)](https://search.maven.org/search?q=org.jesperancinha.plugins:omni-coveragereporter-maven-plugin)
[![javadoc](https://javadoc.io/badge2/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin/javadoc.svg)](https://javadoc.io/doc/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

[![omni-reporter-maven-plugin](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin/actions/workflows/omni-reporter-maven-plugin.yml/badge.svg)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin/actions/workflows/omni-reporter-maven-plugin.yml)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e99bdfc311eb48a899c13d3554b9b640)](https://www.codacy.com/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JEsperancinhaOrg/omni-reporter-maven-plugin&amp;utm_campaign=Badge_Grade)

[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/e99bdfc311eb48a899c13d3554b9b640)](https://www.codacy.com/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/dashboard?utm_source=github.com&utm_medium=referral&utm_content=JEsperancinhaOrg/omni-reporter-maven-plugin&utm_campaign=Badge_Coverage)
[![Coverage Status](https://coveralls.io/repos/github/JEsperancinhaOrg/omni-reporter-maven-plugin/badge.svg?branch=main)](https://coveralls.io/github/JEsperancinhaOrg/omni-reporter-maven-plugin?branch=main)
[![codecov](https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/branch/main/graph/badge.svg?token=KKCLdubOii)](https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin)

[![GitHub language count](https://img.shields.io/github/languages/count/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/top/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/code-size/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)

A plugin intended to keep the pace of technology and be able to use the Coveralls platform as Java/Scala/Kotlin updates move onwwards

## Features

#### 1.  Reporting file supported

| Type       | Status | Notes                                                                                                                                                                                                                                           | Available from Release |
|------------|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| Jacoco XML | ✅      | Jacoco reports seem to report on nonexistent classes in some cases. This seems to happen with Kotlin. This breaks down the functionality of some plugins. In this version we are allowed to ignore this, since it does not affect most reports. | 0.0.0                  |

#### 2.  Online API's supported

| Type       | Status | Notes | Environment Variables                                                                                                                                                                                     | Available from Release |
|------------|--------|-------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| Coveralls  | ✅     |       | `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN`                                                                                                                                                               | 0.0.0                  |
| Codacy     | ✅      |       | `CODACY_PROJECT_TOKEN`                                                                                                                                                                                    | 0.0.7                  |
| Codacy API | ✅      |       | All</u> of these: [`CODACY_API_TOKEN`, `CODACY_ORGANIZATION_PROVIDER`, `CODACY_USERNAME`, `CODACY_PROJECT_NAME`](https://docs.codacy.com/coverage-reporter/). It has Priority over `CODACY_PROJECT_TOKEN` | 0.0.9                  |
| CodeCov    | ✅      |       | `CODECOV_TOKEN`                                                                                                                                                                                           | 0.0.9                  |

#### 3.  Pipelines Supported

| Type     | Status | Notes | Available from Release | Example project                                                                                                                                                                                  |
|----------|--------|-------|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Local    | ✅      |       | 0.0.0                  | N/A                                                                                                                                                                                              |
| Git Hub  | ✅      |       | 0.0.1                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=City%20Library%20Management%20🏢&color=informational)](https://github.com/jesperancinha/advanced-library-management) |
| Git Lab  | ✅      |       | 0.0.1                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Favourite%20Lyrics%20App&color=informational)](https://gitlab.com/jesperancinha/favourite-lyrics-app)                |
| CircleCI | 🚧     |       | ?                      | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=image-sizer&color=informational)](https://github.com/jesperancinha/image-sizer)                                      |


#### 4.  Configuration options

| Property                        | Function                                                                                                                                                                                                                                                                                                                                            | Available from Release |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| failOnUnknown                   | If an unknown file is found, it will ignore current file and proceed with reporting the rest. It is `false` by default                                                                                                                                                                                                                              | 0.0.0                  |
| failOnNoEncoding                | If an explicit encoding is not found, the reporting process will continue. It will fail only should an invalid character be found. Active this if you want the plugin to fail if configuration is not found. It is `false` by default                                                                                                               | ?                      |
| failOnReportNotFound            | If a particular report is not generated, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                         | 0.0.7                  |
| failOnReportSendingError        | If a particular report has failed to be sent, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                    | 0.0.7                  |
| failOnXmlParsingError           | If the process fails to parse a Jacoco file, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                     | 0.0.8                  |
| disableCoveralls                | By default it is set to `false`. If variables `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN` are available, `Omni` reporter will try to send the report to Coveralls. However, you can also prevent this behaviour if you set this to true                                                                                                             | 0.0.8                  |
| disableCodacy                   | By default it is set to `false`. If variable `CODACY_PROJECT_TOKEN` is available, `Omni` reporter will try to send the report to Codacy. However, you can also prevent this behaviour if you set this to true                                                                                                                                       | 0.0.8                  |
| disableCodecov                  | By default it is set to `false`. If variable `CODECOV_TOKEN` is available, `Omni` reporter will try to send the report to Codecov. However, you can also prevent this behaviour if you set this to true                                                                                                                                             | 0.0.9                  |
| ignoreTestBuildDirectory        | By default it is set to `true`. There is normally no reason to include test reporting files. If you do, however, you can set this flag to `false`.                                                                                                                                                                                                  | 0.0.2                  |
| branchCoverage                  | By default it is set to `false`. If you want include branch coverage in your reporting please activate this flag.                                                                                                                                                                                                                                   | ?                      |
| useCoverallsCount               | By default it is set to `true`. If you want to let the pipeline determine the numbering for your Job Id and run, then set this to `false`. It will then search those values via environment variables.                                                                                                                                              | 0.0.3                  |
| extraSourceFolders              | You may want to include extra `Source` folders for `Omni` to find. It is better to make sure that in your plugin, you can define and tell maven where are your extra source folders. This parameter is here available as a last resort , should you find a problem where the plugins just don't work the way you expect them to.                    | 0.0.7                  |
| coverallsUrl                    | Should Coveralls ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                             | 0.0.0                  |
| codacyUrl                       | Should Codacy ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                                | 0.0.7                  |
| codecovUrl                      | Should Codecov ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                               | 0.0.7                  |
| coverallsToken                  | Sets the `coverallsToken` manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variables `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN` instead     | 0.0.0                  |
| codacyToken                     | Sets the `codacyToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_PROJECT_TOKEN` instead                        | 0.0.7                  |
| (*1) codacyApiToken             | Sets the `codacyApiToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_API_TOKEN` instead                         | 0.0.9                  |
| (*1) codacyOrganizationProvider | Sets the `codacyOrganizationProvider` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_ORGANIZATION_PROVIDER` instead | 0.0.9                  |
| (*1) codacyUserName             | Sets the `codacyUserName` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_USERNAME` instead                          | 0.0.9                  |
| (*1) codacyProjectName          | Sets the `codacyProjectName` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_PROJECT_NAME` instead                   | 0.0.9                  |
| codecovToken                    | Sets the `codecovToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODECOV_TOKEN` instead                              | 0.0.9                  |

> *N - These variables only work when fully configured in the N set.

#### 5.  How to run

```shell
mvn clean install
mvn omni-coveragereporter:report
```

#### 6.  Requirements

Java 11 and above only

## Reading and interpreting report files

#### Jacoco Reports

- `mi` = missed instructions
- `ci` = covered instructions
- `mb` = missed branches
- `cb` = covered branches

## Functionality description

1.  Path Corrections for Codecov Jacoco reports
2.  Codecov support for endpoint V4 version (*1)
3.  API token support for codacy
4.  Disable flags for Coveralls and Codacy to force them out even when environment variables are available
   1.  `disableCoveralls`
   2.  `disableCodacy`
5.  Exception handling for Codacy formatting issue
   3.  `failOnXmlParsingError`, false by default
6.  Codacy update so solve Xerces module error. Manual implementation required
7.  Codacy support (*1)
8.  `failOnReportNotFound`
9.  `failOnUnknown` Bug fix
10. Possibility to add external root sources - useful in cases where projects are using scala, java, kotlin and/or clojure at the same time. The plugin only recognizes one source directory. Parameter name is `extraSourceFolders`
11. `failOnReportSendingError`
12. `useCoverallsCount` to let Coveralls decide Job and run numbers.
13. Ignore test build directory by default. Make `ignoreTestBuildDirectory`, `true` by default.
14. Find files in all sources directories including generated sources
15. Rejection words implemented. Fixes issue with GitHub pipelines build names for Coveralls Report
16. Token log Redacting (even in debug) for Coveralls Report
17. We can ignore unknown class error generated by Jacoco. This happens with some Kotlin code. The option is `failOnUnknown`
18. [Saga](https://timurstrekalov.github.io/saga/) and [Cobertura](https://www.mojohaus.org/cobertura-maven-plugin/) support is not given because of the lack of updates in these plugins for more than 5 years.
19. Plugin will search for all jacoco.xml files located in the build directory.
20. If there are two reports with the same file reported, the result will be a sum.
21. Coveralls support (*1)
23. Line Coverage

>*1 All of these requirements are part of Milestone 10, which comprises support for three reporting frameworks: Codecov, Codacy and Coveralls with focus on Jacoco Reports.

## Release notes - Upcoming version 0.0.12

1. Branch Coverage for Coveralls
2. Source encoding gets automatically chosen unless we configure flag `failOnNoEncoding` to `true`
3. Split to use [common library](https://github.com/JEsperancinhaOrg/omni-reporter-commons)
4. Handover to Library

> For complete release notes logbook please check [ReleaseNotes.md](./ReleaseNotes.md)

## How to use

#### 1.  Omni reporter

Just add the following dependency to your project to get coverage sent to coveralls

```xml

<plugin>
    <groupId>org.jesperancinha.plugins</groupId>
    <artifactId>omni-coveragereporter-maven-plugin</artifactId>
    <version>${omni-coveragereporter-maven-plugin.version}</version>
</plugin>
```

Don't forget to have the variables available in your environment for the API's you want to send your reports to.

If you want to be more specific in your configuration and need an example here is one of a fully configured plugin:

```xml

<plugin>
    <groupId>org.jesperancinha.plugins</groupId>
    <artifactId>omni-coveragereporter-maven-plugin</artifactId>
    <version>${omni-coveragereporter-maven-plugin.version}</version>
    <configuration>
        <failOnUnknown>false</failOnUnknown>
        <failOnNoEncoding>false</failOnNoEncoding>
        <failOnReportNotFound>false</failOnReportNotFound>
        <failOnReportSendingError>false</failOnReportSendingError>
        <failOnXmlParsingError>false</failOnXmlParsingError>
        <disableCoveralls>false</disableCoveralls>
        <disableCodacy>false</disableCodacy>
        <disableCodecov>false</disableCodecov>
        <ignoreTestBuildDirectory>true</ignoreTestBuildDirectory>
        <branchCoverage>false</branchCoverage>
        <useCoverallsCount>false</useCoverallsCount>
        <extraSourceFolders>${project.build.directory}/generated-sources/plugin</extraSourceFolders>
        <coverallsUrl>https://coveralls.io/api/v1/jobs</coverallsUrl>
        <codacyUrl>https://api.codacy.com</codacyUrl>
        <codecovUrl>https://codecov.io/upload</codecovUrl>
        <coverallsToken>AAAAAAAAAAAAAAAAAAA</coverallsToken>
        <codacyToken>AAAAAAAAAAAAAAAAAAA</codacyToken>
        <codacyApiToken>AAAAAAAAAAAAAAAAAAA</codacyApiToken>
        <codacyOrganizationProvider>AAAAAAAAAAAAAAAAAAA</codacyOrganizationProvider>
        <codacyUserName>AAAAAAAAAAAAAAAAAAA</codacyUserName>
        <codacyProjectName>AAAAAAAAAAAAAAAAAAA</codacyProjectName>
        <codecovToken>AAAAAAAAAAAAAAAAAAA</codecovToken>
    </configuration>
</plugin>
```

#### 2.  Jacoco Reports

You'll need Maven surefire plugin and the jacoco plugin. Here is an example:

```xml

<plugins>
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>${jacoco-maven-plugin.version}</version>
        <executions>
            <execution>
                <goals>
                    <goal>prepare-agent</goal>
                </goals>
            </execution>
            <execution>
                <id>report</id>
                <phase>test</phase>
                <goals>
                    <goal>report</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-surefire-plugin.version}</version>
    </plugin>
</plugins>
```

## Troubleshooting

Having issues running this plugin? Please check the [Troubleshooting.md](./Troubleshooting.md) document.

## Buy me a coffee

I hope you enjoyed this repository. If you did, you can optionally please buy me a coffee, which supports me to constantly improve and make new free content regularly for everyone. Thank you so much!

[![Buy me a coffee](https://img.buymeacoffee.com/button-api/?text=Buy%20me%20a%20coffee&emoji=&slug=jesperancinha&button_colour=046c46&font_colour=ffffff&font_family=Cookie&outline_colour=ffffff&coffee_colour=FFDD00 "title")](https://www.buymeacoffee.com/jesperancinha)


## Coverage report Graphs

<div align="center">
<img width="30%" src="https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/branch/main/graphs/sunburst.svg"/>
<img width="30%" src="https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/branch/main/graphs/tree.svg"/>
</div>
<div align="center">
<img width="60%" src="https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/branch/main/graphs/icicle.svg"/>
</div>

## References

-   [Codacy Coverage Reporter](https://github.com/codacy/codacy-coverage-reporter)
-   [Jackson Module](https://medium.com/@foxjstephen/how-to-actually-parse-xml-in-java-kotlin-221a9309e6e8)
-   [XCode Environment Variable Reference](https://developer.apple.com/documentation/xcode/environment-variable-reference)
-   [Cross-CI reference](https://github.com/streamich/cross-ci)
-   [Coveralls API reference](https://docs.coveralls.io/api-reference)
-   [Git Hub Environment Variables](https://docs.github.com/en/actions/learn-github-actions/environment-variables)
-   [Git Lab Environment Variables](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html)
-   [Check Run Reporter](https://github.com/marketplace/check-run-reporter)
-   [Codacy Maven Plugin](https://github.com/halkeye/codacy-maven-plugin)
-   [Coveralls Maven Plugin](https://github.com/trautonen/coveralls-maven-plugin)
-   [Example Java Maven for CodeCov](https://github.com/codecov/example-java-maven)
-   [CodeCov Maven Plugin](https://github.com/alexengrig/codecov-maven-plugin)

## About me 👨🏽‍💻🚀🏳️‍🌈

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/JEOrgLogo-20.png "João Esperancinha Homepage")](http://joaofilipesabinoesperancinha.nl)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/medium-20.png "Medium")](https://medium.com/@jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/credly-20.png "Credly")](https://www.credly.com/users/joao-esperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha.nl&color=6495ED "João Esperancinha Homepage")](https://joaofilipesabinoesperancinha.nl/)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social "GitHub")](https://github.com/jesperancinha)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social "Twitter")](https://twitter.com/joaofse)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=JEsperancinhaOrg&color=yellow "jesperancinha.org dependencies")](https://github.com/JEsperancinhaOrg)   
[![Generic badge](https://img.shields.io/static/v1.svg?label=Articles&message=Across%20The%20Web&color=purple)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Articles.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Webapp&message=Image%20Train%20Filters&color=6495ED)](http://itf.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red "All badges")](https://joaofilipesabinoesperancinha.nl/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red "Project statuses")](https://github.com/jesperancinha/project-signer/blob/master/project-signer-quality/Build.md)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/coursera-20.png "Coursera")](https://www.coursera.org/user/da3ff90299fa9297e283ee8e65364ffb)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/google-apps-20.png "Google Apps")](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)   
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/sonatype-20.png "Sonatype Search Repos")](https://search.maven.org/search?q=org.jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/docker-20.png "Docker Images")](https://hub.docker.com/u/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/stack-overflow-20.png)](https://stackoverflow.com/users/3702839/joao-esperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/reddit-20.png "Reddit")](https://www.reddit.com/user/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/devto-20.png "Dev To")](https://dev.to/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hackernoon-20.jpeg "Hackernoon")](https://hackernoon.com/@jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codeproject-20.png "Code Project")](https://www.codeproject.com/Members/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/github-20.png "GitHub")](https://github.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/bitbucket-20.png "BitBucket")](https://bitbucket.org/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/gitlab-20.png "GitLab")](https://gitlab.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/bintray-20.png "BinTray")](https://bintray.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/free-code-camp-20.jpg "FreeCodeCamp")](https://www.freecodecamp.org/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hackerrank-20.png "HackerRank")](https://www.hackerrank.com/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codeforces-20.png "Code Forces")](https://codeforces.com/profile/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codebyte-20.png "Codebyte")](https://coderbyte.com/profile/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codewars-20.png "CodeWars")](https://www.codewars.com/users/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codepen-20.png "Code Pen")](https://codepen.io/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hacker-news-20.png "Hacker News")](https://news.ycombinator.com/user?id=jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/infoq-20.png "InfoQ")](https://www.infoq.com/profile/Joao-Esperancinha.2/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/linkedin-20.png "LinkedIn")](https://www.linkedin.com/in/joaoesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/xing-20.png "Xing")](https://www.xing.com/profile/Joao_Esperancinha/cv)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/tumblr-20.png "Tumblr")](https://jofisaes.tumblr.com/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/pinterest-20.png "Pinterest")](https://nl.pinterest.com/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/quora-20.png "Quora")](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)

## Achievements

[![VMware Spring Professional 2021](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/vmware-spring-professional-2021.png "VMware Spring Professional 2021")](https://www.credly.com/badges/762fa7a4-9cf4-417d-bd29-7e072d74cdb7)
[![Oracle Certified Professional, JEE 7 Developer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/oracle-certified-professional-java-ee-7-application-developer-100.png "Oracle Certified Professional, JEE7 Developer")](https://www.credly.com/badges/27a14e06-f591-4105-91ca-8c3215ef39a2)
[![Oracle Certified Professional, Java SE 11 Programmer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/oracle-certified-professional-java-se-11-developer-100.png "Oracle Certified Professional, Java SE 11 Programmer")](https://www.credly.com/badges/87609d8e-27c5-45c9-9e42-60a5e9283280)
[![IBM Cybersecurity Analyst Professional](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/ibm-cybersecurity-analyst-professional-certificate-100.png "IBM Cybersecurity Analyst Professional")](https://www.credly.com/badges/ad1f4abe-3dfa-4a8c-b3c7-bae4669ad8ce)
[![Certified Advanced JavaScript Developer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/cancanit-badge-1462-100.png "Certified Advanced JavaScript Developer")](https://cancanit.com/certified/1462/)
[![Certified Neo4j Professional](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/professional_neo4j_developer-100.png "Certified Neo4j Professional")](https://graphacademy.neo4j.com/certificates/c279afd7c3988bd727f8b3acb44b87f7504f940aac952495ff827dbfcac024fb.pdf)
[![Deep Learning](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/deep-learning-100.png "Deep Learning")](https://www.credly.com/badges/8d27e38c-869d-4815-8df3-13762c642d64)
