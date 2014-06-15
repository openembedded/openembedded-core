# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2014e.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "552251600ee3571c244d33c7196ec2ed"
SRC_URI[tzcode.sha256sum] = "43ebc426ab4911e222a4487f24289adfd2bc5eb09363a5e77cdabf56374a8c1c"
SRC_URI[tzdata.md5sum] = "8a49fdedd4e832da8e85f7f9bcaedc33"
SRC_URI[tzdata.sha256sum] = "08e243a79051f838d86ac1311a78b74047951302d448024e823b5b7aa71f86c5"

require tzcode-native.inc
