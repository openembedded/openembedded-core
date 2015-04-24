# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015d.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "4008a3abc025a398697b2587c48258b9"
SRC_URI[tzcode.sha256sum] = "221af54ec5c42eaf0101159ffe1256a883d1c14c46228d42774c656a56317128"
SRC_URI[tzdata.md5sum] = "b595bdc4474b8fc1a15cffc67c66025b"
SRC_URI[tzdata.sha256sum] = "8b9f5008277f09e251e97dba7813f56168d691115bda90ade4638d72f296d531"

require tzcode-native.inc
