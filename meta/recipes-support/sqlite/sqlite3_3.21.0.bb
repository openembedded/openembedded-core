require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "\
  http://www.sqlite.org/2017/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "7913de4c3126ba3c24689cb7a199ea31"
SRC_URI[sha256sum] = "d7dd516775005ad87a57f428b6f86afd206cb341722927f104d3f0cf65fbbbe3"
