# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014i.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "7fa413158f01a961348aa6a289b6be21"
SRC_URI[tzcode.sha256sum] = "62591075fb0d47459889b71bac3c8487f09b7417b81dfa541d750b4889e84783"
SRC_URI[tzdata.md5sum] = "00adeb62a0897aac5ba67da838351adb"
SRC_URI[tzdata.sha256sum] = "2af331bdd2b794ec59b8ded7300fb29dc3573a009ec14183f5c9ad38e3886153"
require tzcode-native.inc
