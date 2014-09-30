# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014h.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "8e7741fc769ebdd94d95e5f2c3adbb60"
SRC_URI[tzcode.sha256sum] = "a4d9788a1bb0aa314eae4986ee991425b83ecc47da0e84f626735846be1dbf44"
SRC_URI[tzdata.md5sum] = "ed05111948beba8a0f30956baa46b272"
SRC_URI[tzdata.sha256sum] = "e78152f616fb07c1dea124215ffca57d0de66d8897e00896086542e3de30f69e"

require tzcode-native.inc
