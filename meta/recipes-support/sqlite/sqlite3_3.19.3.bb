require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2017/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "c93070d5bf136ce271db23d2dfbc2435"
SRC_URI[sha256sum] = "06129c03dced9f87733a8cba408871bd60673b8f93b920ba8d815efab0a06301"
