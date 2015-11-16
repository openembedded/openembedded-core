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

SRC_URI[md5sum] = "bc4eb5b3fc5cfcb6e059794306cac1ca"
SRC_URI[sha256sum] = "064c0abe9c9177534d4c770bca7a5902f9924b629ac886b4c08956be6dfbc36b"

UPSTREAM_CHECK_URI = "http://www.sqlite.org/"
UPSTREAM_CHECK_REGEX = "releaselog/(?P<pver>(\d+[\.\-_]*)+)\.html"

S = "${WORKDIR}/sqlite-autoconf-${SQLITE_PV}"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
