# Omni Reporter Maven Plugin

## FAQ

#### 1. Why am I getting a strange Hash Id in the Job Id column in Coveralls API framework instead of a numeric one.
Coveralls is an amazing API and I try to make this plugin to keep up with standards accross many platforms.
However, I ran into an issue with the GitLab pipelines and how they work. I noticed that using [Eclipse JGit](https://www.eclipse.org/jgit/), I would not get the branch name. Instead, I would just get the hash of the commit. GitLab was the only case I found where I could not get the correct branch name in a standard way.
I also noticed that this apparently influences the way the Job ID is calculated. 
If I exceptionally send `null` records regarding the service id's, then everything seems to work fine for all pipelines except for GitLab. Either I'm missing something or there could be some other underlying issue. What I can't do is break the standards specific to GitLab and Coveralls at the same time.
What I did is to allow you to decide on how you want this configuration to be. Please see below the minimalistic most used ways to use `Omni` in different pipelines.


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

---
 
Back to [Readme.md](./README.md)