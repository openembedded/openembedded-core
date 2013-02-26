require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

PR = "r0"

SRC_URI = "http://www.sqlite.org/sqlite-autoconf-${PV}.tar.gz"
S = "${WORKDIR}/sqlite-autoconf-${PV}"

SRC_URI[md5sum] = "bcb0ab0b5b30116b2531cfeef3c861b4"
SRC_URI[sha256sum] = "782d16b797f6ca879f6f679ba3fb6ceb54bcb0cab65feef332058bf04b36ba8c"
