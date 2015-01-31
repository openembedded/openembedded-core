# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015a.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "8f375ede46ae137fbac047ac431bda37"
SRC_URI[tzcode.sha256sum] = "885bab11f286852f34030d7a86ede7d4126319ca74b8ee22be8ca7c17d72dd19"
SRC_URI[tzdata.md5sum] = "4ed11c894a74a5ea64201b1c6dbb8831"
SRC_URI[tzdata.sha256sum] = "c52490917d00a8e7fc9b5f0b1b65ef6ec76d612b5b20c81bf86a04147af18e4c"
require tzcode-native.inc
