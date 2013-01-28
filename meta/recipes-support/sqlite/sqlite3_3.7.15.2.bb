require sqlite3.inc

SQLITE_VERSTR = "3071502"

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

PR = "r0"

SRC_URI = "http://www.sqlite.org/sqlite-autoconf-${SQLITE_VERSTR}.tar.gz"
S = "${WORKDIR}/sqlite-autoconf-${SQLITE_VERSTR}"

SRC_URI[md5sum] = "bcb0ab0b5b30116b2531cfeef3c861b4"
SRC_URI[sha256sum] = "782d16b797f6ca879f6f679ba3fb6ceb54bcb0cab65feef332058bf04b36ba8c"
