require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

def sqlite_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    if len(pvsplit) < 4:
        pvsplit.append('0')
    return pvsplit[0] + ''.join([part.rjust(2,'0') for part in pvsplit[1:]])

PE = "3"
SQLITE_PV = "${@sqlite_download_version(d)}"
SRC_URI = "http://www.sqlite.org/2015/sqlite-autoconf-${SQLITE_PV}.tar.gz"

SRC_URI[md5sum] = "cd0f883b2ddfc29e8e1bbbbd8e85f555"
SRC_URI[sha256sum] = "a324143f4cc35cd7e9605a0a8dec9f9e4861d0be8305f3642e7d05008b77e60d"

S = "${WORKDIR}/sqlite-autoconf-${SQLITE_PV}"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"

