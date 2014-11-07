SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
           file://wget_cve-2014-4877.patch \
          "
SRC_URI[md5sum] = "506df41295afc6486662cc47470b4618"
SRC_URI[sha256sum] = "52126be8cf1bddd7536886e74c053ad7d0ed2aa89b4b630f76785bac21695fcd"

require wget.inc
