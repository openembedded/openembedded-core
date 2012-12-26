require sqlite3.inc

SQLITE_VERSTR = "3071501"

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

PR = "r0"

SRC_URI = "http://www.sqlite.org/sqlite-autoconf-${SQLITE_VERSTR}.tar.gz"
S = "${WORKDIR}/sqlite-autoconf-${SQLITE_VERSTR}"

SRC_URI[md5sum] = "a67c25afa199a11f0a37aff7ed9d2c14"
SRC_URI[sha256sum] = "e8809bd9a292be777f76c97c7e77ab41f7d1020776ded5a631da3985b4b26afd"
