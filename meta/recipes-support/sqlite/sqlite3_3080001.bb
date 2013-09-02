require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "http://www.sqlite.org/2013/sqlite-autoconf-${PV}.tar.gz"

SRC_URI[md5sum] = "ee32c96e5db6c6d651c7c9b71082cf7c"
SRC_URI[sha256sum] = "1d92ccfca5629701b207e57e86fdf0a01d8dca61c60e1246f92ec8d87408cf36"

S = "${WORKDIR}/sqlite-autoconf-${PV}"
