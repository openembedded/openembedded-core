require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

def sqlite_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + ''.join([part.rjust(2,'0') for part in pvsplit[1:]])

PE = "2"
SQLITE_PV = "${@sqlite_download_version(d)}"
SRC_URI = "http://www.sqlite.org/2014/sqlite-autoconf-${PV}.tar.gz"

S = "${WORKDIR}/sqlite-autoconf-${SQLITE_PV}"

SRC_URI[md5sum] = "0f5459cde43cb269e5120ecd2c671ced"
SRC_URI[sha256sum] = "e0e995e23a324a5d6ae95d8a836240382a4d7475d09707fc469c8cafcbd48d65"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"

