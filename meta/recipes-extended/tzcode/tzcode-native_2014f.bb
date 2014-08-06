# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014f.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "1e15be52900cd49e93f093d4731fec96"
SRC_URI[tzcode.sha256sum] = "8c12b56abf614722f0ab7cfc502492063b2c7c5de19563540132b81709ac2555"
SRC_URI[tzdata.md5sum] = "f333b2e8f876221a97871cae0c188aa5"
SRC_URI[tzdata.sha256sum] = "eed690a72124f380bcb14947d398a7a482acb9ab792ae78bd4554e52c5ca2001"

require tzcode-native.inc
