require opkg.inc

SRC_URI = "http://opkg.googlecode.com/files/opkg-${PV}.tar.gz \
           file://add_vercmp.patch \
           file://headerfix.patch \
          "

SRC_URI[md5sum] = "c714ce0e4863bf1315e3b6913ffe3299"
SRC_URI[sha256sum] = "ff94bf30bd662d49c4b5057e3a0818d062731adaa555d59abd677ec32a3c1c60"

PR = "${INC_PR}.0"
