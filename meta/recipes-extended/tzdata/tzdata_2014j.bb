SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "2d7ea9c309f0d4e162e426e568290ca3"
SRC_URI[tzdata.sha256sum] = "a2d870320694d40535df822ac8074dc629a90e92abafa5d3373314f78ddc0e0d"

require tzdata.inc
