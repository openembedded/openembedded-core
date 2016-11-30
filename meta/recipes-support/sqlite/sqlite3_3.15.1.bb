require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2016/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  file://0001-revert-ad601c7962-that-brings-2-increase-of-build-ti.patch \
  "

SRC_URI[md5sum] = "0259d52be88f085d104c6d2aaa8349ac"
SRC_URI[sha256sum] = "5dfa89b7697ee3c2ac7b44e8e157e7f204bf999c866afcaa8bb1c7ff656ae2c5"
