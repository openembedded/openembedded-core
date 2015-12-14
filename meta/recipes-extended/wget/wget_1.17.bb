SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
           file://0001-Unset-need_charset_alias-when-building-for-musl.patch \
          "

SRC_URI[md5sum] = "c4c4727766f24ac716936275014a0536"
SRC_URI[sha256sum] = "3e04ad027c5b6ebd67c616eec13e66fbedb3d4d8cbe19cc29dadde44b92bda55"

require wget.inc
