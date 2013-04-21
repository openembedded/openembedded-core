require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "http://www.sqlite.org/2013/sqlite-autoconf-${PV}.tar.gz"

SRC_URI[md5sum] = "ce7d2bc0d9b8dd18995b888c6b0b220f"
SRC_URI[sha256sum] = "1d7e6937c19fc5de1c0cdb392638296e4a6d8b158001dbb421e257dfb6f088db"

S = "${WORKDIR}/sqlite-autoconf-${PV}"
