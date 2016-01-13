require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

def sqlite_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    if len(pvsplit) < 4:
        pvsplit.append('0')
    return pvsplit[0] + ''.join([part.rjust(2,'0') for part in pvsplit[1:]])

PE = "3"
SQLITE_PV = "${@sqlite_download_version(d)}"
SRC_URI = "http://www.sqlite.org/2016/sqlite-autoconf-${SQLITE_PV}.tar.gz \
           file://parallel.patch \
          "

SRC_URI[md5sum] = "274364e6ca5c1104d42912f11e61ed26"
SRC_URI[sha256sum] = "43cc292d70711fa7580250c8a1cd7c64813a4a0a479dbd502cce5f10b5d91042"

UPSTREAM_CHECK_URI = "http://www.sqlite.org/"
UPSTREAM_CHECK_REGEX = "releaselog/(?P<pver>(\d+[\.\-_]*)+)\.html"

S = "${WORKDIR}/sqlite-autoconf-${SQLITE_PV}"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
