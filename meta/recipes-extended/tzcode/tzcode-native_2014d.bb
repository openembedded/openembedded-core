# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014d.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "4a0f5f259ba8a2b826fffb1fa26d0134"
SRC_URI[tzcode.sha256sum] = "e9c775bb76c9700521ada77776277c25f8650a22b56d46c9f00f4147970ef13e"
SRC_URI[tzdata.md5sum] = "299b86c0368ecfb321f15d5c408a1d9b"
SRC_URI[tzdata.sha256sum] = "4b4966912f5d4a299b0bdf47e6f3103d82dc6a0b5a6b321e2b9d5662597a62f0"

require tzcode-native.inc
