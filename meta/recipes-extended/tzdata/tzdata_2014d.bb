SRC_URI = "ftp://ftp.iana.org/tz/releases/tzdata${PV}.tar.gz;name=tzdata"

SRC_URI[tzdata.md5sum] = "299b86c0368ecfb321f15d5c408a1d9b"
SRC_URI[tzdata.sha256sum] = "4b4966912f5d4a299b0bdf47e6f3103d82dc6a0b5a6b321e2b9d5662597a62f0"

require tzdata.inc
