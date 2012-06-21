#!/bin/sh
if test -f ~/.sbtconfig; then
  . ~/.sbtconfig
fi
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar `dirname $0`/sbt-launch-0.11.3.jar "$@"
