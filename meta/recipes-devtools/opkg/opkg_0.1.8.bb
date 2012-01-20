require opkg.inc

SRC_URI = "http://opkg.googlecode.com/files/opkg-${PV}.tar.gz \
           file://add_vercmp.patch \
           file://headerfix.patch \
          "

PR = "${INC_PR}.0"
