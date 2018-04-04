require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "\
  http://www.sqlite.org/2018/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "6a26a122a18bcd1a6a1c18b4bad64498"
SRC_URI[sha256sum] = "b7711a1800a071674c2bf76898ae8584fc6c9643cfe933cfc1bc54361e3a6e49"
