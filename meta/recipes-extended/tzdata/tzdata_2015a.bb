SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "4ed11c894a74a5ea64201b1c6dbb8831"
SRC_URI[tzdata.sha256sum] = "c52490917d00a8e7fc9b5f0b1b65ef6ec76d612b5b20c81bf86a04147af18e4c"

require tzdata.inc
