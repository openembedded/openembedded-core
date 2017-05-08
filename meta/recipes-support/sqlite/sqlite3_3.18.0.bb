require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2017/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "a6687a8ae1f66abc8df739aeadecfd0c"
SRC_URI[sha256sum] = "3757612463976e7d08c5e9f0af3021613fc24bbcfe1c51197d6776b9ece9ac5c"
