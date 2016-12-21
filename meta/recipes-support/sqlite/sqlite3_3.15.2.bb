require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2016/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  file://0001-revert-ad601c7962-that-brings-2-increase-of-build-ti.patch \
  "

SRC_URI[md5sum] = "6b4fc0d8f7f02dd56bbde10a7c497a05"
SRC_URI[sha256sum] = "07b35063b9386865b78226cdaca9a299d938a87aaa8fdc4d73edb0cef30f3149"
