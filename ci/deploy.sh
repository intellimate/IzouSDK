#!/usr/bin/env bash
if [ "$TRAVIS_PULL_REQUEST" != "false" ] ; then
    echo "Skipping deploy"
    exit 0
fi

./../gradlew -p .. release