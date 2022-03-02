# Omni Reporter Maven Plugin

## FAQ

#### 1. Why am I getting a strange Hash Id in the Job Id column in Coveralls API framework instead of a numeric one.
Coveralls is an amazing API and I try to make this plugin to keep up with standards accross many platforms.
However, I ran into an issue with the GitLab pipelines and how they work. I noticed that using [Eclipse JGit](https://www.eclipse.org/jgit/), I would not get the branch name. Instead, I would just get the hash of the commit. GitLab was the only case I found where I could not get the correct branch name in a standard way.
I also noticed that this apparently influences the way the Job ID is calculated. 
If I exceptionally send `null` records regarding the service id's, then everything seems to work fine for all pipelines except for GitLab. Either I'm missing something or there could be some other underlying issue. What I can't do is break the standards specific to GitLab and Coveralls at the same time.
What I did is to allow you to decide on how you want this configuration to be. Please see below the minimalistic most used ways to use `Omni` in different pipelines.

#### 2. Why I'm not getting any reports. I see my jars in target and I see all jacoco files being correctly generated
The only time I've seen this happen is for a good reason. It mostly comes from the fact that there are multiple jars in the target folder. This could be from a multitude of reasons but mostly that you have your original jar in the same folder as the runnable jar. This can happen with Spring or any other plugin like shade. A good work around and also a way to make your target folder look nicer is to just change the output directory of the runnable jar you want to generate.

##### Shade plugin

- Build
```xml
<build>
    <finalName>runnable/${project.name}</finalName>
    ...
</build>
```

- Plugin
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>${maven-shade-plugin.version}</version>
    <configuration>
        <transformers>
            <transformer
                    implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                <manifestEntries>
                    <Main-Class>org.jesperancinha.concert.buy.oyc.TicketServiceLauncher</Main-Class>
                </manifestEntries>
            </transformer>
            <transformer
                    implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
        </transformers>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Check full example on: [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Buy%20Odd%20Yucca%20Concert🌴&color=informational)](https://github.com/jesperancinha/buy-odd-yucca-concert)

For further undestanding of this issue, I'm going to try to expllain in a nutshell the intricacies of this issue. Jacoco generates report files in `CSV`, `XML` and `exec` formats. The `exec` seems to be a favourite format, but it is encoded in a way jacoco understands and Omni will try to unpack that. However `exec` does not contain some class information and it needs the original jar in order to translate `exec` to a readable format. If the `jar` is the wrong one, then the generated report will render no files. To find which jar is the correct one is actually somewhat difficult because each framework and packaging systems have different locations for the target folders. I contemplate that for `Gradle` and `Maven`, but it is difficult to do that when having multiple jars because of the processing cost. In general it is just better to isolate the original jar in the source target folder. In this case what I mean by target is the root folder of your generated binaries or release files. This folder is mostly called `target`, but it can also be called `build`, `bin`, `lib` and this purely depends on your packaging system.

What coming versions will have is a way to define which jar is the original one. You won't have to isolate the original jar anymore but it will cost you in organization. 2022/03/02.
## Pipelines

### GitLab

```xml
<plugin>
    <groupId>org.jesperancinha.plugins</groupId>
    <artifactId>omni-coveragereporter-maven-plugin</artifactId>
    <version>${omni-coveragereporter-maven-plugin.version}</version>
    <configuration>
        <useCoverallsCount>false</useCoverallsCount>
    </configuration>
</plugin>
```

##### Example Projects:

[![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Favourite%20Lyrics%20App&color=informational)](https://gitlab.com/jesperancinha/favourite-lyrics-app)

[![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=Video%20Series%20Apps&color=informational)](https://gitlab.com/jesperancinha/video-series-app)


### GitHub
```xml
<plugin>
    <groupId>org.jesperancinha.plugins</groupId>
    <artifactId>omni-coveragereporter-maven-plugin</artifactId>
    <version>${omni-coveragereporter-maven-plugin.version}</version>
</plugin>
```

##### Example projects

[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Concert%20Demos%20🎸%20&color=informational)](https://github.com/jesperancinha/concert-demos-root)

[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=From%20Paris%20to%20Berlin%20🛣&color=informational)](https://github.com/jesperancinha/from-paris-to-berlin-circuit-breaker)


### Circle CI

```xml
<plugin>
    <groupId>org.jesperancinha.plugins</groupId>
    <artifactId>omni-coveragereporter-maven-plugin</artifactId>
    <version>${omni-coveragereporter-maven-plugin.version}</version>
</plugin>
```

Although there is still no dedicated support to Circle CI, `Omni` already works with it.
It will mention `local-ci` because that is the default name for the CI pipeline.

##### Example projects

[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=image-sizer&color=informational)](https://github.com/jesperancinha/image-sizer)


---

 
Back to [Readme.md](./README.md)