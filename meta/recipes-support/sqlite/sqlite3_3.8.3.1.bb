require sqlite3.inc

LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

def sqlite_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + ''.join([part.rjust(2,'0') for part in pvsplit[1:]])

PE = "1"
SQLITE_PV = "${@sqlite_download_version(d)}"
SRC_URI = "http://www.sqlite.org/2014/sqlite-autoconf-${SQLITE_PV}.tar.gz"

SRC_URI[md5sum] = "509ff98d8dc9729b618b7e96612079c6"
SRC_URI[sha256sum] = "de5dc216e9289fabf027f78dbbface32ffc8c6341b7d841d0814b1a452ffdb8c"

S = "${WORKDIR}/sqlite-autoconf-${SQLITE_PV}"

# Provide column meta-data API
BUILD_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"
TARGET_CFLAGS += "-DSQLITE_ENABLE_COLUMN_METADATA"

