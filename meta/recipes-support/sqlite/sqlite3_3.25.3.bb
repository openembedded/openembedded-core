require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "\
  http://www.sqlite.org/2018/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "06db8206bc8febf07141b78ad58595ea"
SRC_URI[sha256sum] = "00ebf97be13928941940cc71de3d67e9f852698233cd98ce2d178fd08092f3dd"
