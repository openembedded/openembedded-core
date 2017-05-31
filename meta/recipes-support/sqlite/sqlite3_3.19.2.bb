require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2017/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "9f006b16de2cd81f6bae9b40e91daabf"
SRC_URI[sha256sum] = "ca5361fb01cc3ad63d6fd4eb2cb0b6398e629595896d3558f7e121d37dac2ffc"
