# note that we allow for us to use data later than our code version
#
SRC_URI =" ftp://ftp.iana.org/tz/releases/tzcode${PV}.tar.gz;name=tzcode \
           ftp://ftp.iana.org/tz/releases/tzdata2015c.tar.gz;name=tzdata"

SRC_URI[tzcode.md5sum] = "182c85e99be2bcc6b77d9956c8c0dcce"
SRC_URI[tzcode.sha256sum] = "7feeb1a60dff8b179c884f6e3aa7684606f4eaeb7f7ecc61a6bb81e3e38af42e"
SRC_URI[tzdata.md5sum] = "4b4a3e344786198c46909e5afde08788"
SRC_URI[tzdata.sha256sum] = "860fac5f5f57f7a2dfc1ba682dbd1f5c0f0b597f761571277b6ed6561e22365a"

require tzcode-native.inc
