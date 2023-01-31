# omni-coveragereporter-maven-plugin

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=%20Checkout%20this%20%40github%20repo%20by%20%40joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB%3A%20https%3A//github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=omni-coveragereporter-maven-plugin&color=informational)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)

[![GitHub release](https://img.shields.io/github/release/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![Maven Central](https://img.shields.io/maven-central/v/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)](https://mvnrepository.com/artifact/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)
[![Sonatype Nexus](https://img.shields.io/nexus/r/https/oss.sonatype.org/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin.svg)](https://search.maven.org/artifact/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)

[![javadoc](https://javadoc.io/badge2/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin/javadoc.svg)](https://javadoc.io/doc/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

[![Snyk Score](https://snyk-widget.herokuapp.com/badge/mvn/org.jesperancinha.plugins/omni-coveragereporter-maven-plugin/badge.svg)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin)
[![Known Vulnerabilities](https://snyk.io/test/github/JEsperancinhaOrg/omni-reporter-maven-plugin/badge.svg)](https://snyk.io/test/github/JEsperancinhaOrg/omni-reporter-maven-plugin)

[![omni-reporter-maven-plugin](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin/actions/workflows/omni-reporter-maven-plugin.yml/badge.svg)](https://github.com/JEsperancinhaOrg/omni-reporter-maven-plugin/actions/workflows/omni-reporter-maven-plugin.yml)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/cdb0d01e148246a6a505b8beb86c3c5d)](https://www.codacy.com/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JEsperancinhaOrg/omni-reporter-maven-plugin&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/9457e027-2170-442e-8a7c-384db578b582)](https://codebeat.co/projects/github-com-jesperancinhaorg-omni-reporter-maven-plugin-main)
[![BCH compliance](https://bettercodehub.com/edge/badge/JEsperancinhaOrg/omni-reporter-maven-plugin?branch=main)](https://bettercodehub.com/results/JEsperancinhaOrg/omni-reporter-maven-plugin)

[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/cdb0d01e148246a6a505b8beb86c3c5d)](https://www.codacy.com/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/dashboard?utm_source=github.com&utm_medium=referral&utm_content=JEsperancinhaOrg/omni-reporter-maven-plugin&utm_campaign=Badge_Coverage)
[![Coverage Status](https://coveralls.io/repos/github/JEsperancinhaOrg/omni-reporter-maven-plugin/badge.svg?branch=main)](https://coveralls.io/github/JEsperancinhaOrg/omni-reporter-maven-plugin?branch=main)
[![codecov](https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin/branch/main/graph/badge.svg?token=KKCLdubOii)](https://codecov.io/gh/JEsperancinhaOrg/omni-reporter-maven-plugin)

[![GitHub language count](https://img.shields.io/github/languages/count/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/top/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/code-size/JEsperancinhaOrg/omni-reporter-maven-plugin.svg)](#)

A plugin intended to keep the pace of technology and be able to use the Coveralls platform as Java/Scala/Kotlin updates move onwwards

## Features

#### 1.  Reporting file supported

| Type             | Status | Notes                                                                                                                                                                                                                                             | Available from Release | Example Project                                                                                                                                                                      |
|------------------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Jacoco XML       | ✅      | Jacoco reports seem to report on nonexistent classes in some cases. This seems to happen with Kotlin. This breaks down the functionality of some plugins. In this version we are allowed to ignore this, since it does not affect most reports.   | 0.0.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |
| Clover XML       | ✅      |                                                                                                                                                                                                                                                   | 0.1.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |
| LCov Info        | ✅      |                                                                                                                                                                                                                                                   | 0.1.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |
| Coverage Py JSON | ✅      |                                                                                                                                                                                                                                                   | 0.1.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |
| Jacoco Exec      | ✅      |                                                                                                                                                                                                                                                   | 0.1.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |
| Coverage Codecov | ✅      | For unsupported formats, Omni makes the conversion to the generic [supported Codecov format](https://docs.codecov.com/docs/supported-report-formats) (only from known formats to Omni)                                                            | 0.1.0                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics) |

> NOTE - Only reports generated within maven build folders will be taken automatically in the report parsing, generating and sending. If you want to consider external reports then you need to configure external folders with options `extraSourceFolders` and `extraReportFolders`. See below for the generic configuration details or check a real example on project [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Bridge%20Management%20Logistics&color=informational)](https://gitlab.com/jesperancinha/bridge-logistics)

#### 2.  Online API's supported

| Type       | Status | Notes | Environment Variables                                                                                                                                                                                     | Available from Release |
|------------|--------|-------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| Coveralls  | ✅     |       | `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN`                                                                                                                                                               | 0.0.0                  |
| Codacy     | ✅      |       | `CODACY_PROJECT_TOKEN`                                                                                                                                                                                    | 0.0.7                  |
| Codacy API | ✅      |       | All</u> of these: [`CODACY_API_TOKEN`, `CODACY_ORGANIZATION_PROVIDER`, `CODACY_USERNAME`, `CODACY_PROJECT_NAME`](https://docs.codacy.com/coverage-reporter/). It has Priority over `CODACY_PROJECT_TOKEN` | 0.0.9                  |
| CodeCov    | ✅      |       | `CODECOV_TOKEN`                                                                                                                                                                                           | 0.0.9                  |

#### 3.  Pipelines Supported

| Type      | Status | Notes | Available from Release | Example project                                                                                                                                                                                          |
|-----------|--------|-------|------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Local     | ✅      |       | 0.0.0                  | N/A                                                                                                                                                                                                      |
| Git Hub   | ✅      |       | 0.0.1                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=City%20Library%20Management%20🏢&color=informational)](https://github.com/jesperancinha/advanced-library-management)         |
| Git Lab   | ✅      |       | 0.0.1                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Favourite%20Lyrics%20App&color=informational)](https://gitlab.com/jesperancinha/favourite-lyrics-app)                        |
| CircleCI  | ✅      |       | 0.1.3                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=image-sizer&color=informational)](https://github.com/jesperancinha/image-sizer)                                              |
| BitBucket | ✅      |       | 0.1.3                  | [![Generic badge](https://img.shields.io/static/v1.svg?label=BitBucket&message=International%20Airports✈️&color=informational)](https://bitbucket.org/jesperancinha/international-airports-service-root) |



#### 4.  Configuration options

| Property                        | Function                                                                                                                                                                                                                                                                                                                                                                                 | Available from Release |
|---------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| failOnUnknown                   | If an unknown file is found, it will ignore current file and proceed with reporting the rest. It is `false` by default                                                                                                                                                                                                                                                                   | 0.0.0                  |
| failOnNoEncoding                | If an explicit encoding is not found, the reporting process will continue. It will fail only should an invalid character be found. Active this if you want the plugin to fail if configuration is not found. It is `false` by default                                                                                                                                                    | ?                      |
| failOnReportNotFound            | If a particular report is not generated, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                                                              | 0.0.7                  |
| failOnReportSendingError        | If a particular report has failed to be sent, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                                                         | 0.0.7                  |
| failOnXmlParsingError           | If the process fails to parse a Jacoco file, we may not want our process to continue. For the most cases though we may just want a warning about this. It is `false` by default                                                                                                                                                                                                          | 0.0.8                  |
| disableCoveralls                | By default it is set to `false`. If variables `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN` are available, `Omni` reporter will try to send the report to Coveralls. However, you can also prevent this behaviour if you set this to true                                                                                                                                                  | 0.0.8                  |
| disableCodacy                   | By default it is set to `false`. If variable `CODACY_PROJECT_TOKEN` is available, `Omni` reporter will try to send the report to Codacy. However, you can also prevent this behaviour if you set this to true                                                                                                                                                                            | 0.0.8                  |
| disableCodecov                  | By default it is set to `false`. If variable `CODECOV_TOKEN` is available, `Omni` reporter will try to send the report to Codecov. However, you can also prevent this behaviour if you set this to true                                                                                                                                                                                  | 0.0.9                  |
| ignoreTestBuildDirectory        | By default it is set to `true`. There is normally no reason to include test reporting files. If you do, however, you can set this flag to `false`.                                                                                                                                                                                                                                       | 0.0.2                  |
| branchCoverage                  | By default it is set to `false`. If you want include branch coverage in your reporting please activate this flag.                                                                                                                                                                                                                                                                        | ?                      |
| useCoverallsCount               | By default it is set to `true`. If you want to let the pipeline determine the numbering for your Job Id and run, then set this to `false`. It will then search those values via environment variables.                                                                                                                                                                                   | 0.0.3                  |
| fetchBranchNameFromEnv          | Some Coverage frameworks may present the commit hash instead of the branch name under different circumstances. It can vary if you run your tests under Docker or it can vary under certain variable combinations. This flag is set to fals by default. If set to true, it will get the branch name from the pipeline environment variables.                                              | 0.0.3                  |
| extraSourceFolders              | You may want to include extra `Source` folders for `Omni` to find. It is better to make sure that in your plugin, you can define and tell maven where are your extra source folders. This parameter is here available as a last resort , should you find a problem where the plugins just don't work the way you expect them to.                                                         | 0.0.7                  |
| extraReportFolders              | This is a list of extra report folders you can add for external reports unrelated to Maven. Sources wil be searched across the `extraSourceFolders`. The search algorithm is based on source file distances to the report. By default all report frameworks generate reports as close as possible to the source code. So keep in mind not to change that default configuration too much. | 0.0.7                  |
| reportRejectList                | This is a string list of reporting filenames you possibly want to reject in some cases (i.e. two different report types of different brands for the same files for the same cases)                                                                                                                                                                                                       | 0.1.3                  |
| coverallsUrl                    | Should Coveralls ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                                                                  | 0.0.0                  |
| codacyUrl                       | Should Codacy ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                                                                     | 0.0.7                  |
| codecovUrl                      | Should Codecov ever change the API endpoint, you can change that here                                                                                                                                                                                                                                                                                                                    | 0.0.7                  |
| coverallsToken                  | Sets the `coverallsToken` manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variables `COVERALLS_REPO_TOKEN` or `COVERALLS_TOKEN` instead                                          | 0.0.0                  |
| codacyToken                     | Sets the `codacyToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_PROJECT_TOKEN` instead                                                             | 0.0.7                  |
| (*1) codacyApiToken             | Sets the `codacyApiToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_API_TOKEN` instead                                                              | 0.0.9                  |
| (*1) codacyOrganizationProvider | Sets the `codacyOrganizationProvider` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_ORGANIZATION_PROVIDER` instead                                      | 0.0.9                  |
| (*1) codacyUserName             | Sets the `codacyUserName` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_USERNAME` instead                                                               | 0.0.9                  |
| (*1) codacyProjectName          | Sets the `codacyProjectName` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODACY_PROJECT_NAME` instead                                                        | 0.0.9                  |
| codecovToken                    | Sets the `codecovToken` token manually. Use this for local tests only or if you have a globally variable not declared in versioned files. Using tokens explicitly in the maven pom.xml file is unsafe. Do NOT place your tokens in the clear. For production purposes use environment variable `CODECOV_TOKEN` instead                                                                   | 0.0.9                  |
| parallelization                 | Parallelization - Indicates how big is the pool of threads available to process individual report files. Default value is `4`                                                                                                                                                                                                                                                            | 0.4.0                  |

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

## Release notes 

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
        <fetchBranchNameFromEnv>true</fetchBranchNameFromEnv>
        <parallelization>4</parallelization>
        <extraSourceFolders>
            <param>${project.basedir}/source1</param>
            <param>${project.basedir}/source2</param>
            <param>${project.basedir}/source3</param>
            <param>${project.basedir}</param>
        </extraSourceFolders>
        <extraReportFolders>
            <param>${project.basedir}/folder1/coverage</param>
            <param>${project.basedir}/folder2/target</param>
            <param>${project.basedir}/folder3/build</param>
            <param>${project.basedir}/folder4/dist</param>
            <param>${project.basedir}/folder5/bin</param>
            <param>${project.basedir}</param>
        </extraReportFolders>
        <reportRejectList>
            <param>filename.extension</param>
        </reportRejectList>
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

--- 

## Test files

In all Omni projects, there are test files scattered all over the place. This is needed to see how the application behaves in different test scenarios. To keep things organized, this is the list of the files that are here specifically for test purposes:

-   [test](./test)
-   [test2](./test2)
-   [test3](./test3)
-   [test4](./test4)

---

## Repository `~/.m2/settings.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.executable>gpg</gpg.executable>
                <gpg.passphrase>{{password}}</gpg.passphrase>
            </properties>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>ossrh</activeProfile>
    </activeProfiles>
    <servers>
        <server>
            <id>ossrh</id>
            <username>{{username}}</username>
            <password>{{password}}</password>
        </server>
    </servers>
</settings>
```

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
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=Jesperancinha&style=social "GitHub")](https://github.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/mastodon-20.png "Mastodon")](https://masto.ai/@jesperancinha)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social "Twitter")](https://twitter.com/joaofse)
| [Sessionize](https://sessionize.com/joao-esperancinha/)
| [Spotify](https://open.spotify.com/user/jlnozkcomrxgsaip7yvffpqqm?si=b54b89eae8894960)
| [Medium](https://medium.com/@jofisaes)
| [Buy me a coffee](https://www.buymeacoffee.com/jesperancinha)
| [Credly Badges](https://www.credly.com/users/joao-esperancinha)
| [Google Apps](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
| [Sonatype Search Repos](https://search.maven.org/search?q=org.jesperancinha)
| [Docker Images](https://hub.docker.com/u/jesperancinha)
| [Stack Overflow Profile](https://stackoverflow.com/users/3702839/joao-esperancinha)
| [Reddit](https://www.reddit.com/user/jesperancinha/)
| [Dev.TO](https://dev.to/jofisaes)
| [Hackernoon](https://hackernoon.com/@jesperancinha)
| [Code Project](https://www.codeproject.com/Members/jesperancinha)
| [BitBucket](https://bitbucket.org/jesperancinha)
| [GitLab](https://gitlab.com/jesperancinha)
| [Coursera](https://www.coursera.org/user/da3ff90299fa9297e283ee8e65364ffb)
| [FreeCodeCamp](https://www.freecodecamp.org/jofisaes)
| [HackerRank](https://www.hackerrank.com/jofisaes)
| [LeetCode](https://leetcode.com/jofisaes)
| [Codebyte](https://coderbyte.com/profile/jesperancinha)
| [CodeWars](https://www.codewars.com/users/jesperancinha)
| [Code Pen](https://codepen.io/jesperancinha)
| [Hacker Earth](https://www.hackerearth.com/@jofisaes)
| [Khan Academy](https://www.khanacademy.org/profile/jofisaes)
| [Hacker News](https://news.ycombinator.com/user?id=jesperancinha)
| [InfoQ](https://www.infoq.com/profile/Joao-Esperancinha.2/)
| [LinkedIn](https://www.linkedin.com/in/joaoesperancinha/)
| [Xing](https://www.xing.com/profile/Joao_Esperancinha/cv)
| [Tumblr](https://jofisaes.tumblr.com/)
| [Pinterest](https://nl.pinterest.com/jesperancinha/)
| [Quora](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)
| [VMware Spring Professional 2021](https://www.credly.com/badges/762fa7a4-9cf4-417d-bd29-7e072d74cdb7)
| [Oracle Certified Professional, Java SE 11 Programmer](https://www.credly.com/badges/87609d8e-27c5-45c9-9e42-60a5e9283280)
| [Oracle Certified Professional, JEE7 Developer](https://www.credly.com/badges/27a14e06-f591-4105-91ca-8c3215ef39a2)
| [IBM Cybersecurity Analyst Professional](https://www.credly.com/badges/ad1f4abe-3dfa-4a8c-b3c7-bae4669ad8ce)
| [Certified Advanced JavaScript Developer](https://cancanit.com/certified/1462/)
| [Certified Neo4j Professional](https://graphacademy.neo4j.com/certificates/c279afd7c3988bd727f8b3acb44b87f7504f940aac952495ff827dbfcac024fb.pdf)
| [Deep Learning](https://www.credly.com/badges/8d27e38c-869d-4815-8df3-13762c642d64)
| [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=JEsperancinhaOrg&color=yellow "jesperancinha.org dependencies")](https://github.com/JEsperancinhaOrg)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red "All badges")](https://joaofilipesabinoesperancinha.nl/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red "Project statuses")](https://github.com/jesperancinha/project-signer/blob/master/project-signer-quality/Build.md)
