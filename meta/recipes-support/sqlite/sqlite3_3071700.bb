require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "http://www.sqlite.org/2013/sqlite-autoconf-${PV}.tar.gz"

SRC_URI[md5sum] = "18c285053e9562b848209cb0ee16d4ab"
SRC_URI[sha256sum] = "8ff46d0baa9e64c0815544e829e985f1161c096aa6344c8f430791dbeadc2baf"

S = "${WORKDIR}/sqlite-autoconf-${PV}"
