SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "ed05111948beba8a0f30956baa46b272"
SRC_URI[tzdata.sha256sum] = "e78152f616fb07c1dea124215ffca57d0de66d8897e00896086542e3de30f69e"

require tzdata.inc
