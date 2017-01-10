require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=65f0a57ca6928710b418c094b3570bb0"

SRC_URI = "\
  http://www.sqlite.org/2017/sqlite-autoconf-${SQLITE_PV}.tar.gz \
  "
SRC_URI[md5sum] = "5a153ef1fd2fa5845ada74deabc68e32"
SRC_URI[sha256sum] = "65cc0c3e9366f50c0679c5ccd31432cea894bc4a3e8947dabab88c8693263615"

