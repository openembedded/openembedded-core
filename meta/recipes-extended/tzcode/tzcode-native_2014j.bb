# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014j.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "970119e9765bc5a9320368851c91ecb6"
SRC_URI[tzcode.sha256sum] = "7fd46125464856309fc81fe85a67a61de862b8ab884ce8ca82051f5fa308ede2"
SRC_URI[tzdata.md5sum] = "2d7ea9c309f0d4e162e426e568290ca3"
SRC_URI[tzdata.sha256sum] = "a2d870320694d40535df822ac8074dc629a90e92abafa5d3373314f78ddc0e0d"
require tzcode-native.inc
