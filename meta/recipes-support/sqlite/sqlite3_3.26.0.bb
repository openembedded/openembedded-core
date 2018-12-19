require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "\
  http://www.sqlite.org/2018/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "ac2b3b8cd3a97600e36fb8e756e8dda1"
SRC_URI[sha256sum] = "5daa6a3fb7d1e8c767cd59c4ded8da6e4b00c61d3b466d0685e35c4dd6d7bf5d"
