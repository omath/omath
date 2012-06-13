#!/bin/sh
if test -f ~/.sbtconfig; then
  . ~/.sbtconfig
fi
java -Xmx1000M -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxPermSize=256m ${SBT_OPTS} -jar sbt-launch-0.11.2.jar "$@"
