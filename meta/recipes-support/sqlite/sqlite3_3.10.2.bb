require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "http://www.sqlite.org/2016/sqlite-autoconf-${SQLITE_PV}.tar.gz \
           file://parallel.patch \
"

SRC_URI[md5sum] = "adaa31593bb5605ec6d6f34f81b43008"
SRC_URI[sha256sum] = "a2b3b4bd1291ea7d6c8252f7edff36a4362f2f0e5d5370444ba6cbe313ae2971"
