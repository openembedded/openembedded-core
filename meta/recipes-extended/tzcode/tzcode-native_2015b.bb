# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015b.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "f073a7c9bca7f9fe408491f826e92968"
SRC_URI[tzcode.sha256sum] = "e668f1235b998c90e43ecc93c8535728c65bb01d6bb93d22467e04b5ffa35d76"
SRC_URI[tzdata.md5sum] = "75571bb17c7b6be88a9f8872e45bc726"
SRC_URI[tzdata.sha256sum] = "556ac1a5b3a371adc1ad4e77138f78ddd7f8ddd7bc2b52545924598c7dc8ad62"
require tzcode-native.inc
