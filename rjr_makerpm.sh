#!/bin/bash

function exists() {
    which "$1" >/dev/null 2>&1
}

function use_git() {
    exists git && test -d "${TOPDIR}/.git"
}

function calcMinor()
{
    if use_git; then
        git log --pretty='format:%cd' --date=short -1 | head -n 1 | sed -e 's,^Date: *,,' -e 's,-,,g'
    else
        date '+%Y%m%d'
    fi
}

MINOR=$(calcMinor)
micro=1
if [ -f target/.properties ]; then
    micro=$(grep "^OpenNMS_BUILD_${MINOR}_micro=" target/.properties |sed -e 's,^.*=,,')
    micro=$((micro + 1))
fi
echo "OpenNMS_BUILD_${MINOR}_micro=$micro" > target/.properties

set -ex
rm -fr target/rpm/BUILD/* target/rpm/BUILDROOT/opennms* target/rpm/tmp target/rpm/RPMS/noarch
./clean_maven_files.pl
find /itch/maven/repository/org/opennms -depth |xargs rmdir 2>/dev/null || true
DATESTART=$(date)
./makerpm.sh -d -u ${micro} 2>&1 | tee makerpm.log
DATEEND=$(date)
createrepo ${PWD}/target/rpm/RPMS/noarch

echo "Start: $DATESTART"
echo "  End: $DATEEND"
