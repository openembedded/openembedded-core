require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "http://www.sqlite.org/2019/sqlite-autoconf-${SQLITE_PV}.tar.gz \
           file://CVE-2019-19244.patch \
           file://CVE-2019-19880.patch \
           file://CVE-2019-19923.patch \
           file://CVE-2019-19924.patch \
           file://CVE-2019-19925.patch \
           file://CVE-2019-19926.patch \
           file://CVE-2019-19959.patch \
           file://CVE-2019-20218.patch \
           "
SRC_URI[md5sum] = "51252dc6bc9094ba11ab151ba650ff3c"
SRC_URI[sha256sum] = "8c5a50db089bd2a1b08dbc5b00d2027602ca7ff238ba7658fabca454d4298e60"

# -19242 is only an issue in specific development branch commits
CVE_CHECK_WHITELIST += "CVE-2019-19242"
