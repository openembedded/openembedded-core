SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
          "

SRC_URI[md5sum] = "5d12410a398ec9907e105e8734561ba0"
SRC_URI[sha256sum] = "3b834ce69366d4681f295307fce36ee14e122c4ee68a4d1291b62b0b26755a77"

require wget.inc
