require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "http://www.sqlite.org/2013/sqlite-autoconf-${PV}.tar.gz"

SRC_URI[md5sum] = "6d6cc639a4da04fbbdda7b1a1a01b386"
SRC_URI[sha256sum] = "fca3a0a12f94dc92a6d9e700c3f6cab6cd4e79214bd7b6f13717a10f4bcfddb2"

S = "${WORKDIR}/sqlite-autoconf-${PV}"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"

