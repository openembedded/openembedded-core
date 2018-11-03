require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "\
  http://www.sqlite.org/2018/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "bfade31d59f58badc51aeaa6ae26a5de"
SRC_URI[sha256sum] = "da9a1484423d524d3ac793af518cdf870c8255d209e369bd6a193e9f9d0e3181"
