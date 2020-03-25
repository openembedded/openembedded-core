require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "http://www.sqlite.org/2020/sqlite-autoconf-${SQLITE_PV}.tar.gz \
           file://CVE-2020-9327.patch \
           "
SRC_URI[md5sum] = "2d0a553534c521504e3ac3ad3b90f125"
SRC_URI[sha256sum] = "62284efebc05a76f909c580ffa5c008a7d22a1287285d68b7825a2b6b51949ae"

# -19242 is only an issue in specific development branch commits
CVE_CHECK_WHITELIST += "CVE-2019-19242"
