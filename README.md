
# web-crawler

This crawls any given url and then saves the data to a csv file.


## Authors

- [@delrotf](https://github.com/delrotf)


## Usage/Examples

```
usage: java -jar web-crawler.jar [-d <arg>] [-f <arg>] [-h] [-ho <arg>]
       [-l] [-o <arg>] [-p <arg>] [-r <arg>] -u <arg> [-v]
This crawls any given url and then saves the data to a csv file in output
directory. Logs can be found under logs directory.
 -d,--max.depth <arg>            Crawling depth limit. 1 means only the
                                 supplied url will be crawled, not the
                                 embedded urls. Default is 0, with no
                                 limit.
 -f,--path.to.file <arg>         This overrides the default output path
 -h,--help                       Display help information.
 -ho,--initial.host.only <arg>   Crawl on the supplied host only. Default
                                 is true.
 -l,--logger                     Show logs from logger.
 -o,--output.directory <arg>     Output directory for the output csv file.
                                 Default is out.
 -p,--filename.prefix <arg>      The prefix for output filename. Default
                                 is output.
 -r,--max.retries <arg>          Number of retries when crawling a url
                                 failed. Default is 1.
 -u,--url <arg>                  The url to initiate crawling with.
                                 Required.
 -v,--verbose                    Shows logs to the console.
e.g. java -jar web-crawler.jar -u http://google.com -d 1



```

To see logs in the console, please use -v argument. Logs are also saved in logs/ directory.

Output files are saved in out/ directory.
## Installation

Download the jar file and see the usage. 
Packaged using Java 8.

```
https://github.com/delrotf/web-crawler/raw/master/dist/web-crawler.jar
```
    
## 🚀 About Me
I'm a full stack developer with 25 years of experience.


## Support

For support, email delrotf02@gmail.com.

